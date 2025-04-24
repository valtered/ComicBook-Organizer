package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Comic;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository



@Component


public class JdbcComicDao implements ComicDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcComicDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comic addComicToCollection(Comic comic, int collectionId, int userId) {
        String sql = "INSERT INTO comics (title, publisher, issue_number, character_name, author, collection_id, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING comic_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    comic.getTitle(), comic.getPublisher(), comic.getIssueNumber(),
                    comic.getCharacterName(), comic.getAuthor(), collectionId, userId);
            if (newId != null) {
                comic.setComicId(newId);
                return comic;
            } else {
                throw new DaoException("Failed to insert comic");
            }
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing the database", e);
        }
    }

    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (DataAccessException e) {
            throw new DaoException("Error retrieving user ID", e);
        }

       

    }


    @Override
    public List<Comic> getComicsByUserId(int userId) {
        List<Comic> comics = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM comics c " +
                "JOIN collection_comics cc ON c.comic_id = cc.comic_id " +
                "JOIN collections col ON cc.collection_id = col.collection_id " +
                "WHERE col.user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                comics.add(mapRowToComic(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error fetching comics for user", e);
        }
        return comics;
    }

    // Implement the method to fetch comics by collectionId
    @Override
    public List<Comic> getComicsByCollectionId(int collectionId) {
        List<Comic> comics = new ArrayList<>();
        String sql = "SELECT * FROM comics WHERE collection_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, collectionId);
            while (results.next()) {
                comics.add(mapRowToComic(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error fetching comics for collection", e);
        }
        return comics;
    }

    private Comic mapRowToComic(SqlRowSet rs) {
        Comic comic = new Comic();
        comic.setComicId(rs.getInt("comic_id"));
        comic.setTitle(rs.getString("title"));
        comic.setDescription(rs.getString("description"));
        comic.setAuthor(rs.getString("author"));
        comic.setPublisher(rs.getString("publisher"));
        comic.setIssueNumber(rs.getInt("issue_number"));
        comic.setCharacterName(rs.getString("character_name"));
        return comic;
    }
}

