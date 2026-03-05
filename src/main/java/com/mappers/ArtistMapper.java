package com.mappers;

import com.dtos.ArtistDto;
import com.dtos.ArtistInputDto;
import com.entities.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

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

    public Artist toEntity(ArtistDto artistDto) {
        if (artistDto == null) {
            return null;
        }

        Artist artist = new Artist();
        if (artistDto.getId() != null) {
            artist.setId(artistDto.getId());
        }
        artist.setName(artistDto.getName());
        artist.setGenre(artistDto.getGenre());
        artist.setBiography(artistDto.getBiography());

        return artist;
    }

    public Artist inputDtoToEntity(ArtistInputDto artistInputDto) {
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
