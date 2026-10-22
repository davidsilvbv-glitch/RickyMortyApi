package com.example.rickandmorty.data

import com.example.rickandmorty.data.local.FavoritoEntity
import com.example.rickandmorty.data.local.PersonajeCacheEntity
import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.data.remote.dto.EpisodeDto
import com.example.rickandmorty.data.remote.dto.LocationDto
import com.example.rickandmorty.data.remote.dto.LocationRefDto
import com.example.rickandmorty.data.remote.dto.OriginRefDto
import com.example.rickandmorty.data.remote.dto.PageInfoDto
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationRef
import com.example.rickandmorty.model.OriginRef
import com.example.rickandmorty.model.PageInfo

fun PageInfoDto.toDomain(): PageInfo {
    return PageInfo(
        count = count,
        pages = pages,
        next = next,
        prev = prev
    )
}

fun OriginRefDto.toDomain(): OriginRef {
    return OriginRef(
        name = name.orEmpty().ifEmpty { "Unknown" },
        url = url.orEmpty()
    )
}

fun LocationRefDto.toDomain(): LocationRef {
    return LocationRef(
        name = name.orEmpty().ifEmpty { "Unknown" },
        url = url.orEmpty()
    )
}

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type.orEmpty(),
        gender = gender,
        origin = origin?.toDomain() ?: OriginRef(),
        location = location?.toDomain() ?: LocationRef(),
        image = image,
        episode = episode.orEmpty(),
        url = url.orEmpty(),
        created = created.orEmpty()
    )
}

fun EpisodeDto.toDomain(): Episode {
    return Episode(
        id = id,
        name = name,
        air_date = airDate.orEmpty().ifEmpty { "Desconocida" },
        episode = episodeCode.orEmpty().ifEmpty { "S00E00" },
        characters = characters.orEmpty(),
        url = url.orEmpty(),
        created = created.orEmpty()
    )
}

fun LocationDto.toDomain(): Location {
    return Location(
        id = id,
        name = name,
        type = type.orEmpty().ifEmpty { "Unknown" },
        dimension = dimension.orEmpty().ifEmpty { "Unknown" },
        residents = residents.orEmpty(),
        url = url.orEmpty(),
        created = created.orEmpty()
    )
}

fun FavoritoEntity.toCharacter(): Character =
    Character(
        id = id,
        name = nombre,
        status = "",
        species = "",
        type = "",
        gender = "",
        origin = OriginRef(),
        location = LocationRef(),
        image = imagenUrl,
        episode = emptyList(),
        url = "",
        created = ""
    )

fun Character.toFavoritoEntity(): FavoritoEntity =
    FavoritoEntity(id = id, nombre = name, imagenUrl = image)

fun PersonajeCacheEntity.toCharacter(): Character =
    Character(
        id = id,
        name = nombre,
        status = status,
        species = species,
        type = "",
        gender = "",
        origin = OriginRef(),
        location = LocationRef(),
        image = image,
        episode = emptyList(),
        url = "",
        created = ""
    )

fun Character.toCacheEntity(): PersonajeCacheEntity =
    PersonajeCacheEntity(id = id, nombre = name, status = status, species = species, image = image)
