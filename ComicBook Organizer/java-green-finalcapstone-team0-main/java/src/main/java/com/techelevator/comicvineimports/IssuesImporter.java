package com.techelevator.comicvineimports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


@Service
public class IssuesImporter {

    private static final String API_KEY = "e20f346962ac962d7611f4614fa65d313933f54f";
    private static final String BASE_URL = "https://comicvine.gamespot.com/api/issues/";
    private static final int LIMIT = 100;

    @Autowired
    private DataSource dataSource;

    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Issue{
        public long id;
        public String name;
        public String issue_number;
        public JsonNode image;
        public Volume volume;
        public int publisher_id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Volume {
        public long id;
        public String name;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class APIResponse {
        public int number_of_total_results;
        public List<Issue> results;
    }

    public List<Issue> fetchIssuesForMarvelAndDC() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        List<Issue> issuesList = new ArrayList<>();
        int requestCount = 0;

        List<Integer> publisherIds= List.of(31, 10);

        for (Integer publisherId : publisherIds) {
            int offset = 0, totalResults = 1;

            while (offset < totalResults) {
                if (requestCount >= 200) {
                    System.out.println("200 requests reached. Waiting for 1 hour...");
                    Thread.sleep(3600000);
                    requestCount = 0;
                }

                String uri = String.format(
                        "%s?api_key=%s&format=json&field_list=id,name,issue_number,image,volume&limit=%d&offset=%d&filter=publisher:%d",
                        BASE_URL, URLEncoder.encode(API_KEY, StandardCharsets.UTF_8), LIMIT, offset, publisherId
                );

                System.out.println("Fetching Issues for Publisher " + publisherId + ": " + uri);

                HttpResponse<String> response = client.send(
                        HttpRequest.newBuilder().uri(URI.create(uri)).GET().build(),
                        HttpResponse.BodyHandlers.ofString()
                );

                System.out.println("API Response: " + response.body());

                if (response.statusCode() == 420) {
                    System.out.println("API Rate Limit HIt. Waiting 1 hour.");
                    Thread.sleep(3600000);
                    continue;
                }

                if (response.statusCode() == 503) {
                    System.out.println("API Error 503: Service Unavailable. Retry in 5 mins");
                    Thread.sleep(300000);
                    continue;
                }

                if (response.statusCode() != 200) throw new RuntimeException("API Error: " + response.statusCode());

                APIResponse apiResponse = mapper.readValue(response.body(), APIResponse.class);
                if (offset == 0) {
                    totalResults = apiResponse.number_of_total_results;
                    System.out.println("Total Issues for Publisher " + publisherId + ": " + totalResults);
                }

                issuesList.addAll(apiResponse.results);
                offset += LIMIT;
                requestCount++;

                Thread.sleep(5000);
            }
        }
        return issuesList;
}

public void insertIssue(Connection conn, Issue issue, int publisherId) throws SQLException {
        String sql = " INSERT INTO issues (id, name, issue_number, image, volume, publisher_id) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, issue.id);
            stmt.setString(2, issue.name);
            stmt.setString(3, issue.issue_number);
            stmt.setString(4, (issue.image != null && !issue.image.isNull()) ? issue.image.toString() : null);
            stmt.setString(5, (issue.volume != null) ? issue.volume.name : null);
            stmt.setInt(6, publisherId);
            stmt.executeUpdate();
            System.out.println("Inserted issue: " +issue.name + " (Publisher ID: " + publisherId + ")");
        }
    }

    public String importIssues() {
        System.out.println("Starting issues import.");
        try {
            List<Issue> issues = fetchIssuesForMarvelAndDC();
            System.out.println("Fetched " + issues.size() + " issues from the API.");

            try (Connection conn = dataSource.getConnection()) {
                for (Issue issue : issues) {
                    insertIssue(conn, issue, issue.publisher_id);
                }
            }
                System.out.println("Issue import complete.");
                return "Issue import completed successfully";
            } catch (Exception e) {
                System.out.println("Error importing issues: " + e.getMessage());
                return "Error importing issues: " + e.getMessage();
            }
        }
    }
