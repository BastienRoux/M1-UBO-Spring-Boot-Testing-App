package com.config;

import com.entities.Movie;
import com.entities.Artist;
import com.repositories.MovieRepository;
import com.repositories.ArtistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(MovieRepository movieRepository, ArtistRepository artistRepository) {
        return args -> {
            // Créer des artistes
            Artist artist1 = new Artist();
            artist1.setName("Leonardo DiCaprio");
            artist1.setGenre("Actor");
            artist1.setBiography("Award-winning American actor and producer");
            artistRepository.save(artist1);

            Artist artist2 = new Artist();
            artist2.setName("Christopher Nolan");
            artist2.setGenre("Director");
            artist2.setBiography("British-American film director and screenwriter");
            artistRepository.save(artist2);

            Artist artist3 = new Artist();
            artist3.setName("Margot Robbie");
            artist3.setGenre("Actress");
            artist3.setBiography("Australian actress and producer");
            artistRepository.save(artist3);

            // Créer des films
            Movie movie1 = new Movie();
            movie1.setTitle("Inception");
            movie1.setDirector("Christopher Nolan");
            movie1.setReleaseYear(2010);
            movieRepository.save(movie1);

            Movie movie2 = new Movie();
            movie2.setTitle("The Wolf of Wall Street");
            movie2.setDirector("Martin Scorsese");
            movie2.setReleaseYear(2013);
            movieRepository.save(movie2);

            Movie movie3 = new Movie();
            movie3.setTitle("Once Upon a Time in Hollywood");
            movie3.setDirector("Quentin Tarantino");
            movie3.setReleaseYear(2019);
            movieRepository.save(movie3);

            Movie movie4 = new Movie();
            movie4.setTitle("Interstellar");
            movie4.setDirector("Christopher Nolan");
            movie4.setReleaseYear(2014);
            movieRepository.save(movie4);

            System.out.println("✅ Base de données initialisée avec " + movieRepository.count() + " films et " + artistRepository.count() + " artistes");
        };
    }
}
