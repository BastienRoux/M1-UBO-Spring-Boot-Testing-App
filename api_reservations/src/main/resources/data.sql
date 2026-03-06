-- Insertion de données de test dans la base de données H2
-- Ces données seront chargées automatiquement au démarrage de l'application

-- Insertion d'utilisateurs de test
INSERT INTO users (id, pseudo, first_name, last_name, age, address, password, role, created_at) VALUES
(1, 'john_doe', 'John', 'Doe', 30, '123 Main Street, Paris', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', CURRENT_TIMESTAMP()),
(2, 'jane_smith', 'Jane', 'Smith', 25, '456 Oak Avenue, Lyon', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', CURRENT_TIMESTAMP()),
(3, 'admin', 'Admin', 'User', 35, '789 Admin Road, Marseille', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', CURRENT_TIMESTAMP()),
(4, 'alice_wonder', 'Alice', 'Wonder', 28, '321 Wonder Lane, Toulouse', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', CURRENT_TIMESTAMP()),
(5, 'bob_martin', 'Bob', 'Martin', 32, '654 Martin Street, Nice', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', CURRENT_TIMESTAMP());
-- Note: Le mot de passe pour tous les utilisateurs est "password" (hashé avec bcrypt)

-- Insertion de films de test
INSERT INTO Movie (id, title, release_year, director) VALUES
(1, 'Inception', 2010, 'Christopher Nolan'),
(2, 'The Shawshank Redemption', 1994, 'Frank Darabont'),
(3, 'The Dark Knight', 2008, 'Christopher Nolan'),
(4, 'Pulp Fiction', 1994, 'Quentin Tarantino'),
(5, 'Forrest Gump', 1994, 'Robert Zemeckis'),
(6, 'The Matrix', 1999, 'The Wachowskis'),
(7, 'Interstellar', 2014, 'Christopher Nolan'),
(8, 'The Godfather', 1972, 'Francis Ford Coppola'),
(9, 'The Lord of the Rings: The Return of the King', 2003, 'Peter Jackson'),
(10, 'Fight Club', 1999, 'David Fincher');
