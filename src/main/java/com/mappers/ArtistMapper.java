package com.mappers;

import com.dtos.ArtistDto;
import com.dtos.ArtistInputDto;
import com.entities.Artist;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre les entités Artist et les DTOs.
 * Un mapper permet de séparer la couche de persistance de la couche de présentation.
 * 
 * Points clés du pattern Mapper :
 * - Conversion bidirectionnelle entre DTO et Entity
 * - Gestion des null-safety
 * - Pas de logique métier, uniquement de la transformation
 */
@Component
public class ArtistMapper {

	/**
	 * Convertit une entité Artist en DTO ArtistDto
	 * Cette méthode est utilisée pour exposer les données aux clients de l'API
	 * 
	 * @param artist l'entité à convertir
	 * @return le DTO correspondant ou null si l'entité est null
	 */
	public ArtistDto toDto(Artist artist) {
		if (artist == null) {
			return null;
		}
		
		ArtistDto artistDto = new ArtistDto();
		artistDto.setId(artist.getId());
		artistDto.setName(artist.getName());
		artistDto.setGenre(artist.getGenre());
		artistDto.setBiography(artist.getBiography());
		return artistDto;
	}

	/**
	 * Convertit un DTO ArtistInputDto en entité Artist
	 * Cette méthode est utilisée pour créer de nouvelles entités à partir des données reçues
	 * Note: L'ID est généré automatiquement par l'entité
	 * 
	 * @param artistInputDto le DTO à convertir
	 * @return l'entité correspondante ou null si le DTO est null
	 */
	public Artist toEntity(ArtistInputDto artistInputDto) {
		if (artistInputDto == null) {
			return null;
		}
		
		Artist artist = new Artist();
		artist.setName(artistInputDto.getName());
		artist.setGenre(artistInputDto.getGenre());
		artist.setBiography(artistInputDto.getBiography());
		return artist;
	}
}
