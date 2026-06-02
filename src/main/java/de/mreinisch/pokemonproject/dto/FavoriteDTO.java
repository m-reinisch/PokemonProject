package de.mreinisch.pokemonproject.dto;

/** Data Transfer Object for mapping to the database entity
 *
 * @param pokemonName
 * @param nickname
 */
public record FavoriteDTO(String pokemonName,
                          String nickname) {
}
