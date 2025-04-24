package com.techelevator.controller;

import com.techelevator.comicvineimports.ComicVineService;
import com.techelevator.comicvineimports.PublishersImporter;
import com.techelevator.comicvineimports.IssuesImporter;
import com.techelevator.model.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImportController {

    @Autowired
    private ComicVineService comicVineService;

    @Autowired
    private IssuesImporter issuesImporter;



        @GetMapping("/search/issues")
         public ResponseEntity<List<Comic>> searchIssues(@RequestParam(name = "searchTerm") String searchTerm) {
            List<Comic> comics = comicVineService.search(searchTerm);
            return ResponseEntity.ok(comics);
        }
        @GetMapping("/import/issues")
        public List<Comic> importIssues(@RequestParam String query) {
            return comicVineService.search(query);
        }
    }

