package com.techelevator.model;

import java.util.Objects;

public class Collection {
    private int id;
    private String name;
    private String description;
    private String privacy;
    private int userId;



    // Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPrivacy() {return privacy;}

    public void setPrivacy(String privacy) {this.privacy = privacy;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return id == that.id &&
                userId == that.userId &&
                Objects.equals(name, that.name) &&
                Objects.equals(privacy, that.privacy) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, privacy,userId);
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", publisher='" + name + '\'' +
                ", description='" + description + '\'' +
                ", privacy='" + privacy + '\'' +
                ", userId=" + userId +
                '}';
    }
}
