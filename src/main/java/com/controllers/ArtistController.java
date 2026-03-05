package com.controllers;

import com.dtos.ArtistDto;
import com.dtos.ArtistInputDto;
import com.entities.Artist;
import com.mappers.ArtistMapper;
import com.repositories.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistController(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    @GetMapping
    public ResponseEntity<List<ArtistDto>> getAllArtists(
            @RequestParam(value = "role", required = false) String role) {

        List<Artist> artistEntities = (role == null || role.isBlank())
                ? artistRepository.findAll()
                : artistRepository.findByRoleIgnoreCase(role);

        List<ArtistDto> artists = artistEntities.stream()
                .map(artistMapper::toDto)
                .toList();
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artiste introuvable avec l'ID: " + id));
        return ResponseEntity.ok(artistMapper.toDto(artist));
    }

    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody ArtistInputDto artistInputDto) {
        Artist artist = artistMapper.inputDtoToEntity(artistInputDto);
        Artist savedArtist = artistRepository.save(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(artistMapper.toDto(savedArtist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@PathVariable Long id, @RequestBody ArtistInputDto artistInputDto) {
        Artist existingArtist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artiste introuvable avec l'ID: " + id));

        existingArtist.setName(artistInputDto.getName());
        existingArtist.setGenre(artistInputDto.getGenre());
        existingArtist.setRole(artistInputDto.getRole());
        existingArtist.setBiography(artistInputDto.getBiography());

        Artist updatedArtist = artistRepository.save(existingArtist);
        return ResponseEntity.ok(artistMapper.toDto(updatedArtist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("Artiste introuvable avec l'ID: " + id);
        }

        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
