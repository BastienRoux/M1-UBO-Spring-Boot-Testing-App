-- Script d'initialisation pour la base de données MariaDB
-- Ces données seront chargées automatiquement au démarrage de l'application

-- Suppression et recréation des tables si elles existent (optionnel - à commenter si ddl-auto=update)
-- DROP TABLE IF EXISTS movie_artist;
-- DROP TABLE IF EXISTS movie_genres;
-- DROP TABLE IF EXISTS reservations;
-- DROP TABLE IF EXISTS artist;
-- DROP TABLE IF EXISTS movie;
-- DROP TABLE IF EXISTS users;

-- Insertion d'utilisateurs de test
-- Mot de passe pour tous : "password" (hashé avec bcrypt)
INSERT INTO users (pseudo, first_name, last_name, age, address, password, role, created_at) VALUES
('john_doe', 'John', 'Doe', 30, '123 Main Street, Paris', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', NOW()),
('jane_smith', 'Jane', 'Smith', 25, '456 Oak Avenue, Lyon', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', NOW()),
('admin', 'Admin', 'User', 35, '789 Admin Road, Marseille', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', NOW()),
('alice_wonder', 'Alice', 'Wonder', 28, '321 Wonder Lane, Toulouse', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', NOW()),
('bob_martin', 'Bob', 'Martin', 20, '654 Martin Street, Nice', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', NOW())
ON DUPLICATE KEY UPDATE pseudo = pseudo;

-- Insertion d'artistes de test
INSERT INTO artist (name, genre, role, biography) VALUES
('Leonardo DiCaprio', 'Drama', 'ACTOR', 'Leonardo Wilhelm DiCaprio est un acteur et producteur américain. Connu pour ses rôles dans les films biographiques et d''époque.'),
('Scarlett Johansson', 'Action', 'ACTOR', 'Scarlett Ingrid Johansson est une actrice américaine. Elle a été l''actrice la mieux payée au monde en 2018 et 2019.'),
('Tom Hanks', 'Drama', 'ACTOR', 'Thomas Jeffrey Hanks est un acteur et réalisateur américain. Connu pour ses rôles comiques et dramatiques.'),
('Meryl Streep', 'Drama', 'ACTOR', 'Mary Louise "Meryl" Streep est une actrice américaine. Souvent décrite comme "la meilleure actrice de sa génération".'),
('Denzel Washington', 'Thriller', 'ACTOR', 'Denzel Hayes Washington Jr. est un acteur, réalisateur et producteur américain.'),
('Christopher Nolan', 'Science Fiction', 'DIRECTOR', 'Christopher Edward Nolan est un réalisateur, scénariste et producteur britannico-américain.'),
('Quentin Tarantino', 'Action', 'DIRECTOR', 'Quentin Jerome Tarantino est un réalisateur, scénariste, acteur et producteur américain.'),
('Steven Spielberg', 'Adventure', 'DIRECTOR', 'Steven Allan Spielberg est un réalisateur, scénariste et producteur américain.'),
('Martin Scorsese', 'Crime', 'DIRECTOR', 'Martin Charles Scorsese est un réalisateur, scénariste, producteur et acteur américain.'),
('Cate Blanchett', 'Drama', 'ACTOR', 'Catherine Élise Blanchett est une actrice et productrice australienne.')
ON DUPLICATE KEY UPDATE name = name;

-- Insertion de films de test (avec isOpen = true pour permettre les réservations)
INSERT INTO movie (title, release_year, director, is_open, price, min_age) VALUES
('Inception', 2010, 'Christopher Nolan', true, 4.99, 13),
('The Shawshank Redemption', 1994, 'Frank Darabont', true, 3.99, 16),
('The Dark Knight', 2008, 'Christopher Nolan', true, 4.99, 13),
('Pulp Fiction', 1994, 'Quentin Tarantino', true, 4.49, 18),
('Forrest Gump', 1994, 'Robert Zemeckis', true, 3.99, 10),
('The Matrix', 1999, 'The Wachowskis', true, 4.49, 13),
('Interstellar', 2014, 'Christopher Nolan', true, 5.99, 13),
('The Godfather', 1972, 'Francis Ford Coppola', true, 4.49, 16),
('The Lord of the Rings: The Return of the King', 2003, 'Peter Jackson', true, 5.49, 13),
('Fight Club', 1999, 'David Fincher', true, 4.49, 18)
ON DUPLICATE KEY UPDATE title = title;

-- Insertion des genres pour les films
-- Note: Les IDs des films peuvent varier selon l'auto-increment
-- Ces insertions utilisent des sous-requêtes pour trouver les bons IDs

-- Inception - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Science Fiction' FROM movie WHERE title = 'Inception'
UNION ALL
SELECT id, 'Action' FROM movie WHERE title = 'Inception'
UNION ALL
SELECT id, 'Thriller' FROM movie WHERE title = 'Inception'
ON DUPLICATE KEY UPDATE genre = genre;

-- The Shawshank Redemption - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Drama' FROM movie WHERE title = 'The Shawshank Redemption'
UNION ALL
SELECT id, 'Crime' FROM movie WHERE title = 'The Shawshank Redemption'
ON DUPLICATE KEY UPDATE genre = genre;

-- The Dark Knight - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Action' FROM movie WHERE title = 'The Dark Knight'
UNION ALL
SELECT id, 'Crime' FROM movie WHERE title = 'The Dark Knight'
UNION ALL
SELECT id, 'Thriller' FROM movie WHERE title = 'The Dark Knight'
ON DUPLICATE KEY UPDATE genre = genre;

-- Pulp Fiction - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Crime' FROM movie WHERE title = 'Pulp Fiction'
UNION ALL
SELECT id, 'Drama' FROM movie WHERE title = 'Pulp Fiction'
ON DUPLICATE KEY UPDATE genre = genre;

-- Forrest Gump - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Drama' FROM movie WHERE title = 'Forrest Gump'
UNION ALL
SELECT id, 'Romance' FROM movie WHERE title = 'Forrest Gump'
ON DUPLICATE KEY UPDATE genre = genre;

-- The Matrix - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Science Fiction' FROM movie WHERE title = 'The Matrix'
UNION ALL
SELECT id, 'Action' FROM movie WHERE title = 'The Matrix'
ON DUPLICATE KEY UPDATE genre = genre;

-- Interstellar - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Science Fiction' FROM movie WHERE title = 'Interstellar'
UNION ALL
SELECT id, 'Adventure' FROM movie WHERE title = 'Interstellar'
UNION ALL
SELECT id, 'Drama' FROM movie WHERE title = 'Interstellar'
ON DUPLICATE KEY UPDATE genre = genre;

-- The Godfather - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Crime' FROM movie WHERE title = 'The Godfather'
UNION ALL
SELECT id, 'Drama' FROM movie WHERE title = 'The Godfather'
ON DUPLICATE KEY UPDATE genre = genre;

-- The Lord of the Rings - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Fantasy' FROM movie WHERE title = 'The Lord of the Rings: The Return of the King'
UNION ALL
SELECT id, 'Adventure' FROM movie WHERE title = 'The Lord of the Rings: The Return of the King'
UNION ALL
SELECT id, 'Drama' FROM movie WHERE title = 'The Lord of the Rings: The Return of the King'
ON DUPLICATE KEY UPDATE genre = genre;

-- Fight Club - Genres
INSERT INTO movie_genres (movie_id, genre)
SELECT id, 'Drama' FROM movie WHERE title = 'Fight Club'
UNION ALL
SELECT id, 'Thriller' FROM movie WHERE title = 'Fight Club'
ON DUPLICATE KEY UPDATE genre = genre;

-- Association films-artistes (Many-to-Many)
-- Inception avec Leonardo DiCaprio
INSERT INTO movie_artist (movie_id, artist_id)
SELECT m.id, a.id FROM movie m, artist a WHERE m.title = 'Inception' AND a.name = 'Leonardo DiCaprio'
ON DUPLICATE KEY UPDATE movie_id = movie_id;

-- The Matrix avec Tom Hanks (exemple - à adapter selon les vrais acteurs)
INSERT INTO movie_artist (movie_id, artist_id)
SELECT m.id, a.id FROM movie m, artist a WHERE m.title = 'The Matrix' AND a.name = 'Tom Hanks'
ON DUPLICATE KEY UPDATE movie_id = movie_id;

-- Pulp Fiction avec Quentin Tarantino (réalisateur)
INSERT INTO movie_artist (movie_id, artist_id)
SELECT m.id, a.id FROM movie m, artist a WHERE m.title = 'Pulp Fiction' AND a.name = 'Quentin Tarantino'
ON DUPLICATE KEY UPDATE movie_id = movie_id;
