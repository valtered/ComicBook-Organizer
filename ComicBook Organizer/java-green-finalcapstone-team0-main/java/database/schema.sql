BEGIN TRANSACTION;

DROP TABLE IF EXISTS collection_comics;
DROP TABLE IF EXISTS collections;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS comics;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE collections (
    collection_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    user_id INT NOT NULL,
    privacy VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


CREATE TABLE comics (
    comic_id SERIAL PRIMARY KEY,
    title VARCHAR(150),
    description TEXT,
    author VARCHAR(150),
    publisher VARCHAR(150),
    issue_number INT NOT NULL,
    character_name VARCHAR(50),
    collection_id INT REFERENCES collections(collection_id),
    user_id INT REFERENCES users(user_id),
    image_url VARCHAR
);

CREATE TABLE collection_comics (
    collection_id INT NOT NULL,
    comic_id INT NOT NULL,
    PRIMARY KEY (collection_id, comic_id),
    FOREIGN KEY (collection_id) REFERENCES collections(collection_id),
    FOREIGN KEY (comic_id) REFERENCES comics(comic_id)
);



COMMIT TRANSACTION;
