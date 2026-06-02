package de.mreinisch.pokemonproject.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Data Transfer Object for mapping the external PokéAPI
 *
 * @param height
 * @param id pokemonId
 * @param name pokemonName
 * @param sprites object with pictureUrl inside
 * @param types list of objects with type inside
 * @param weight
 */
public record PokeApiDTO(Integer height,
                         String id,
                         String name,
                         SpritesDTO sprites,
                         List<TypesDTO> types,
                         Integer weight) {
}
