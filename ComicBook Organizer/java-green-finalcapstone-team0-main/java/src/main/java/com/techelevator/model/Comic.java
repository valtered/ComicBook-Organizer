package com.techelevator.model;

import java.util.Objects;

    public class Comic {
        private int comicId;
        private String title;
        private String description;
        private String author;
        private String publisher;
        private int issueNumber;
        private String characterName;
        private String imageUrl;


        // Getters and setters for all fields
        public int getComicId() {
            return comicId;
        }

        public void setComicId(int comicId) {
            this.comicId = comicId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public int getIssueNumber() {
            return issueNumber;
        }

        public void setIssueNumber(int issueNumber) {
            this.issueNumber = issueNumber;
        }

        public String getCharacterName() {
            return characterName;
        }

        public void setCharacterName(String characterName) {
            this.characterName = characterName;
        }

        public String getImageUrl() { return imageUrl; }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Comic comic = (Comic) o;
            return comicId == comic.comicId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(comicId);
        }


        @Override
        public String toString() {
            return "Comic{" +
                    "comicId=" + comicId +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", author='" + author + '\'' +
                    ", publisher='" + publisher + '\'' +
                    ", issueNumber=" + issueNumber +
                    ", characterName='" + characterName +
                    '}';
        }
    }

