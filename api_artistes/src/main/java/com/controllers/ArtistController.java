package com.controllers;

import com.dtos.ArtistDto;
import com.dtos.ArtistInputDto;
import com.services.impl.ArtistServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {
	
	private final ArtistServiceImpl artistService;

	public ArtistController(ArtistServiceImpl artistService) {
		this.artistService = artistService;
	}

	/**
	 * <p>Lister tous les artistes</p>
	 * @return Liste des artistes
	 */
	@GetMapping
	public ResponseEntity<List<ArtistDto>> getAllArtists() {
		return ResponseEntity.ok(artistService.getAllArtists());
	}

	/**
	 * Obtenir un artiste par ID
	 * @param id L'identifiant unique de l'artiste
	 * @return L'artiste trouvé
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ArtistDto> getArtist(@PathVariable Long id) {
		return ResponseEntity.ok(artistService.getArtistById(id));
	}

	/**
	 * Créer un nouvel artiste
	 * @param artistInputDto Les données de l'artiste à créer
	 * @return L'artiste créé avec son ID
	 */
	@PostMapping
	public ResponseEntity<ArtistDto> createArtist(@Valid @RequestBody ArtistInputDto artistInputDto) {
		ArtistDto createdArtist = artistService.createArtist(artistInputDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
	}

	/**
	 * Mettre à jour un artiste existant
	 * @param id L'identifiant de l'artiste à mettre à jour
	 * @param artistInputDto Les nouvelles données de l'artiste
	 * @return L'artiste mis à jour
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ArtistDto> updateArtist(@PathVariable Long id, 
	                                               @Valid @RequestBody ArtistInputDto artistInputDto) {
		return ResponseEntity.ok(artistService.updateArtist(id, artistInputDto));
	}

	/**
	 * Supprimer un artiste
	 * @param id L'identifiant de l'artiste à supprimer
	 * @return Réponse vide avec statut 204
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
		artistService.deleteArtist(id);
		return ResponseEntity.noContent().build();
	}
	
}
