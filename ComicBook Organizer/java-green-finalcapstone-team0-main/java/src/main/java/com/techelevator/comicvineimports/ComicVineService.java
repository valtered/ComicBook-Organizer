package com.techelevator.comicvineimports;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.Comic;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ComicVineService {

    private String url = "https://comicvine.gamespot.com/api/search?api_key=";
    private String key = "e20f346962ac962d7611f4614fa65d313933f54f";

    private RestTemplate restTemplate = new RestTemplate();

    public List<Comic> search(String query) {
        List<Comic> comicsList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "LarryTheCow");
        HttpEntity<Void> entity = new HttpEntity(headers);


        ResponseEntity<String> response = restTemplate.exchange(url + key + "&limit=10&format=json&resources=issue&query=" + query,
                HttpMethod.GET,
                entity,
                String.class
                );


//        System.out.println(response.getBody());
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.getBody());

                JsonNode dataArray = jsonNode.path("results");

                for(int i=0; i < dataArray.size(); i++) {

                    Comic comic = new Comic();
                    String title = dataArray.path(i).path("volume").path("name").asText();
                    comic.setTitle(title);

                    int issueNumber = dataArray.path(i).path("issue_number").asInt(0);
                    comic.setIssueNumber(issueNumber);

                    String imageUrl = dataArray.path(i).path("image").path("medium_url").asText();
                    comic.setImageUrl(imageUrl);

                    comicsList.add(comic);

                    //TODO: Invoke and use the remaining setters.
                        // place all these comic objects into a List<Comic>
                    // return that list at the very end

                    System.out.println(comic);
                }

            } catch(Exception e) {

            }

            return comicsList;

    }
}
