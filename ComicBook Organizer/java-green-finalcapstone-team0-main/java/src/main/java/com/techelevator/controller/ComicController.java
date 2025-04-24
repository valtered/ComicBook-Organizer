package com.techelevator.controller;

import com.techelevator.dao.ComicDao;
import com.techelevator.model.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin  // Allows cross-origin requests
@PreAuthorize("isAuthenticated()") // Ensures user is authenticated
@RequestMapping("/api")
public class ComicController {

    @Autowired
    private ComicDao comicDao;

    // Endpoint to fetch all comics for a specific user
    @GetMapping("/users/{userId}/comics")
    public List<Comic> getComicsByUserId(@PathVariable int userId) {
        return comicDao.getComicsByUserId(userId);
    }

}
