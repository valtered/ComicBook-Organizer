package com.techelevator.comicvineimports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

@Service
public class PublishersImporter {

    private static final String API_KEY = "e20f346962ac962d7611f4614fa65d313933f54f";
    private static final String BASE_URL = "https://comicvine.gamespot.com/api/publishers/";
    private static final int LIMIT = 100;

    @Autowired
    private DataSource dataSource;

    public static class Publisher {
        public long id;
        public String name;
        public JsonNode image;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class APIResponse {
        public int number_of_total_results;
        public List<Publisher> results;
    }

    public List<Publisher> fetchAllPublishers() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        List<Publisher> publishersList = new ArrayList<>();

        int offset = 0;
        int totalResults = 1;
        while(offset < totalResults) {
            String uri = String.format(
                    "%s?api_key=%s&format=json&field_list=id,name,image&limit=%d&offset=%d",
                    BASE_URL, URLEncoder.encode(API_KEY, StandardCharsets.UTF_8), LIMIT, offset
            );

            System.out.println("Fetching: " + uri);
            System.out.flush();

            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder().uri(URI.create(uri)).GET().build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Full API Response: " + response.body());
            System.out.flush();

            if (response.statusCode() != 200) {
                throw new RuntimeException("API Error: " + response.statusCode());
            }


                APIResponse apiResponse = mapper.readValue(response.body(), APIResponse.class);

                if (offset == 0) {
                    totalResults = apiResponse.number_of_total_results;
                    System.out.println("Total Publishers Available: " + totalResults);
                }

                publishersList.addAll(apiResponse.results);

                offset += LIMIT;
            System.out.println("Next Offset: " + offset);
            System.out.flush();

                Thread.sleep(2000);
            }

            return publishersList;

    }


        public void insertPublisher(Connection conn, Publisher publisher) throws SQLException {
            String sql = "INSERT INTO publishers (id, name, image) VALUES (?, ?, ?::json) ON CONFLICT (id) DO NOTHING";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, publisher.id);
                stmt.setString(2, publisher.name);
                stmt.setString(3, publisher.image != null && !publisher.image.isNull() ? publisher.image.toString() : null);
                stmt.executeUpdate();
                System.out.println("Inserted publisher: " + publisher.name);
            }
        }

        public String importPublishers () {
            System.out.println("Starting publisher import process");
            try {
                List<Publisher> publishers = fetchAllPublishers();
                System.out.println("Fetched " + publishers.size() + " publishers from the API.");

                try (Connection conn = dataSource.getConnection()) {
                    for (Publisher publisher : publishers) {
                        insertPublisher(conn, publisher);
                    }
                }
                return "Publisher import completed successfully";
            } catch (Exception e) {
                return "Error importing publishers: " + e.getMessage();
            }
        }
    }
