package com.techelevator.controller;

import com.techelevator.dao.CollectionDao;
import com.techelevator.dao.ComicDao;
import com.techelevator.model.Collection;
import com.techelevator.model.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@CrossOrigin
@RequestMapping("/collections")
public class CollectionController {

    @Autowired
    private CollectionDao collectionDao;

    @Autowired
    private ComicDao comicDao;

    // Get collections by the authenticated user
    @GetMapping
    public List<Collection> getCollectionsByUserId(Principal principal) {
        String username = principal.getName();
        int userId = collectionDao.getUserIdByUsername(username);
        return collectionDao.getCollectionsByUserId(userId);
    }

    // Get a specific collection by ID
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollectionById(@PathVariable int id) {
        Collection collection = collectionDao.getCollectionById(id);
        if (collection == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if not found
        }
        return ResponseEntity.ok(collection); // Return collection if found
    }

    // Create a new collection for the authenticated user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Collection createCollection(@RequestBody Collection collection, Principal principal) {
        String username = principal.getName();
        int userId = collectionDao.getUserIdByUsername(username);
        collection.setUserId(userId);
        return collectionDao.createCollection(collection);
    }

    // Update an existing collection by ID
    @PutMapping("/{id}")
    public void updateCollection(@PathVariable int id, @RequestBody Collection collection) {
        collection.setId(id);
        collectionDao.updateCollection(collection);
    }

    // Delete a collection by its ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(@PathVariable int id) {
        collectionDao.deleteCollection(id);
    }


    // path to add comic to a collection
   // @RequestMapping(path="/{collectionId}/comics", method = RequestMethod.POST)

    // Add a comic to a collection
    @PostMapping("/{collectionId}/comics")

    @ResponseStatus(HttpStatus.CREATED)
    public Comic addComicToCollection(@PathVariable int collectionId, @RequestBody Comic comic, Principal principal) {
        int userId = comicDao.getUserIdByUsername(principal.getName());
        return comicDao.addComicToCollection(comic, collectionId, userId);
    }

    // Fetch collections for the authenticated user


    @GetMapping("/user")
   public List<Collection> getUserCollections(Principal principal) {
        String username = principal.getName();
      int userId = collectionDao.getUserIdByUsername(username);
       return collectionDao.getCollectionsByUserId(userId);
   }







    // Viewing public collections for Guest Users

    @GetMapping("/collections/view")
    public List<Collection> getCollections(Principal principal) {
        if (principal == null) {
            // Guest user (not authenticated), get only public collections
            return collectionDao.getPublicCollections();
        } else {
            // Authenticated user, get all collections
            String username = principal.getName();
            int userId = collectionDao.getUserIdByUsername(username);
            return collectionDao.getCollectionsByUserId(userId);
        }
    }


    @GetMapping("/{collectionId}/comics")
    public List<Comic> getComicsByCollection(@PathVariable int collectionId) {
        // Fetch comics from the collection by collectionId
        return comicDao.getComicsByCollectionId(collectionId);
    }




}
