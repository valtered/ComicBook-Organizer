package com.techelevator.dao;

import com.techelevator.model.Comic;
import org.springframework.stereotype.Component;
import java.util.List;

public interface ComicDao {

    Comic addComicToCollection(Comic comic, int collectionId, int userId);

    int getUserIdByUsername(String username);
    // this  creates a new comic and associates it with a collection

    // Retrieves all comics associated with a user's collections
    List<Comic> getComicsByUserId(int userId);

        // New method to get comics by collectionId
        List<Comic> getComicsByCollectionId(int collectionId);


}
