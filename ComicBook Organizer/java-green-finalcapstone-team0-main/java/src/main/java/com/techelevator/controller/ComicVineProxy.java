package com.techelevator.controller;

import com.techelevator.comicvineimports.ComicVineService;
import com.techelevator.model.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ComicVineProxy {

    @Autowired
    private ComicVineService comicVineService;

    @RequestMapping(path="/search", method= RequestMethod.GET)
    public List<Comic> search(@RequestParam String query) {
        comicVineService.search(query);
        return null;
    }

}
