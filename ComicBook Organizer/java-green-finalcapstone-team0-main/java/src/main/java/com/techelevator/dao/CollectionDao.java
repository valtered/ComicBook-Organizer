package com.techelevator.dao;

import com.techelevator.model.Collection;

import java.util.List;

public interface CollectionDao {
    List<Collection> getCollectionsByUserId(int userId); // Fetch collections by user ID
    Collection getCollectionById(int id); // Fetch a specific collection by its ID
    Collection createCollection(Collection collection); // Create a new collection
    List<Collection> getPublicCollections(); // get public collections only for guest users
    void updateCollection(Collection collection); // Update an existing collection
    void deleteCollection(int id); // Delete a collection by its ID
    int getUserIdByUsername(String username); // Fetch user ID by username
    int getCollectionIdByNameAndUser(String name, int userId); // Fetch collections associated with a specific user
}
