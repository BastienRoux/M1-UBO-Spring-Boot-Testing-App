package com.services;

import com.dtos.ArtistDto;
import com.dtos.ArtistInputDto;

import java.util.List;

/**
 * Interface définissant les opérations métier disponibles pour la gestion des artistes.
 * Cette interface suit le principe d'Interface Segregation (SOLID).
 */
public interface ArtisteService {
	/**
	 * Crée un nouvel artiste dans le système
	 * @param artistInputDto les données de l'artiste à créer
	 * @return l'artiste créé avec son ID généré
	 */
	ArtistDto createArtist(ArtistInputDto artistInputDto);

	/**
	 * Récupère un artiste par son identifiant
	 * @param artistId l'identifiant de l'artiste recherché
	 * @return l'artiste trouvé
	 * @throws jakarta.persistence.EntityNotFoundException si l'artiste n'existe pas
	 */
	ArtistDto getArtistById(Long artistId);

	/**
	 * Met à jour un artiste existant
	 * @param artistId l'identifiant de l'artiste à mettre à jour
	 * @param artistInputDto les nouvelles données de l'artiste
	 * @return l'artiste mis à jour
	 * @throws jakarta.persistence.EntityNotFoundException si l'artiste n'existe pas
	 */
	ArtistDto updateArtist(Long artistId, ArtistInputDto artistInputDto);

	/**
	 * Supprime un artiste du système
	 * @param artistId l'identifiant de l'artiste à supprimer
	 */
	void deleteArtist(Long artistId);

	/**
	 * Récupère tous les artistes du système
	 * @return la liste des artistes
	 */
	List<ArtistDto> getAllArtists();
}
