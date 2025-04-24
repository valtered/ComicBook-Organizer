package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcCollectionDao implements CollectionDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCollectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Don't need to add privacy property into this method, since this will be used for
    // registered users so they can see both private and public collections
    @Override
    public List<Collection> getCollectionsByUserId(int userId) {
        List<Collection> collections = new ArrayList<>();
        String sql = "SELECT collection_id, name, description, user_id FROM collections WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                collections.add(mapRowToCollection(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return collections;
    }

    // Added this method so that Guest users can only see public collections
    @Override
    public List<Collection> getPublicCollections() {
        List<Collection> collections = new ArrayList<>();
        String sql = "SELECT collection_id, name, description, user_id FROM collections WHERE privacy = 'public'";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                collections.add(mapRowToCollection(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return collections;
    }


    @Override
    public Collection getCollectionById(int id) {
        Collection collection = null;
        String sql = "SELECT collection_id, name, description, user_id FROM collections WHERE collection_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                collection = mapRowToCollection(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return collection;
    }

    @Override
    public Collection createCollection(Collection collection) {
        String sql = "INSERT INTO collections (name, description, user_id) VALUES (?, ?, ?) RETURNING collection_id";
        try {
            int newId = jdbcTemplate.queryForObject(sql, int.class, collection.getName(), collection.getDescription(), collection.getUserId());
            collection.setId(newId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return collection;
    }

    @Override
    public void updateCollection(Collection collection) {
        String sql = "UPDATE collections SET name = ?, description = ? WHERE collection_id = ?";
        try {
            jdbcTemplate.update(sql, collection.getName(), collection.getDescription(),collection.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public void deleteCollection(int id) {
        String sql = "DELETE FROM collections WHERE collection_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, int.class, username);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to retrieve user ID", e);
        }
    }

    private Collection mapRowToCollection(SqlRowSet rs) {
        Collection collection = new Collection();
        collection.setId(rs.getInt("collection_id"));
        collection.setName(rs.getString("name"));
        collection.setDescription(rs.getString("description"));
        collection.setUserId(rs.getInt("user_id"));
        return collection;
    }

    @Override
    public int getCollectionIdByNameAndUser(String name, int userId) {
        String sql = "SELECT collection_id FROM collections WHERE name = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, name, userId);
        } catch (DataAccessException e) {
            throw new DaoException("Collection not found or database error", e);
        }
    }
}
