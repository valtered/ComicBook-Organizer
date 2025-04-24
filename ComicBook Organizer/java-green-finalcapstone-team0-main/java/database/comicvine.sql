-- ********************************************************************************
-- This script retrieves and organizes publisher and issue information from Comic Vine's API
-- ********************************************************************************

CREATE TABLE publishers (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    image JSON
);

CREATE TABLE issues (
    issue_id BIGINT PRIMARY KEY,
    description TEXT,
    image JSON,
    issue_number TEXT,
    volume JSON,
    publisher_id BIGINT,
    FOREIGN KEY (publisher_id) REFERENCES publishers(id)
);

CREATE TABLE issue_details (
    id BIGINT PRIMARY KEY,
    character_credits JSON,
    cover_date DATE,
    deck TEXT,
    image JSON,
    issue_number TEXT,
    name TEXT,
    volume JSON,
    person_credits JSON,
    FOREIGN KEY (id) REFERENCES issues(issue_id)
);