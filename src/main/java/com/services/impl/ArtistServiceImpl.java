package com.services.impl;

import com.dtos.ArtistDto;
import com.dtos.ArtistInputDto;
import com.entities.Artist;
import com.repositories.ArtistRepository;
import com.services.ArtisteService;
import com.mappers.ArtistMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implémentation des opérations métier pour la gestion des artistes.
 * Cette classe suit le principe de Single Responsibility (SOLID).
 */
@Service("artistService")
@Transactional
public class ArtistServiceImpl implements ArtisteService {

	private final ArtistRepository artistRepository;
	private final ArtistMapper artistMapper;

	/**
	 * Constructeur avec injection des dépendances
	 * L'injection par constructeur est préférée à @Autowired car :
	 * - Elle rend les dépendances obligatoires
	 * - Elle facilite les tests unitaires
	 * - Elle permet l'immutabilité
	 */
	public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper) {
		this.artistRepository = artistRepository;
		this.artistMapper = artistMapper;
	}

	/**
	 * {@inheritDoc}
	 * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la classe
	 */
	@Override
	public ArtistDto createArtist(ArtistInputDto artistInputDto) {
		var artist = artistMapper.toEntity(artistInputDto);
		var savedArtist = artistRepository.save(artist);
		return artistMapper.toDto(savedArtist);
	}

	/**
	 * {@inheritDoc}
	 * Utilisation de la méthode orElseThrow pour une gestion élégante des cas d'erreur
	 */
	@Override
	@Transactional(readOnly = true)
	public ArtistDto getArtistById(Long artistId) {
		var artist = artistRepository.findById(artistId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("L'artiste avec l'ID %d n'existe pas", artistId)));
		return artistMapper.toDto(artist);
	}

	/**
	 * {@inheritDoc}
	 * Met à jour un artiste existant avec de nouvelles données
	 */
	@Override
	public ArtistDto updateArtist(Long artistId, ArtistInputDto artistInputDto) {
		var existingArtist = artistRepository.findById(artistId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("L'artiste avec l'ID %d n'existe pas", artistId)));
		
		// Mise à jour des champs
		existingArtist.setName(artistInputDto.getName());
		existingArtist.setGenre(artistInputDto.getGenre());
		existingArtist.setBiography(artistInputDto.getBiography());
		
		var updatedArtist = artistRepository.save(existingArtist);
		return artistMapper.toDto(updatedArtist);
	}

	/**
	 * {@inheritDoc}
	 * La méthode deleteById ne lève pas d'exception si l'entité n'existe pas
	 */
	@Override
	public void deleteArtist(Long artistId) {
		artistRepository.deleteById(artistId);
	}

	/**
	 * {@inheritDoc}
	 * Utilisation de l'API Stream pour une transformation fonctionnelle des données
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ArtistDto> getAllArtists() {
		return artistRepository.findAll().stream()
				.map(artistMapper::toDto)
				.toList();
	}
}
