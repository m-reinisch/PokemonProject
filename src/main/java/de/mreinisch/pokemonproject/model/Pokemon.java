package de.mreinisch.pokemonproject.model;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/** Favorite Pokémon to save
 *
 * @param id (self-generated) as the database key
 * @param pokemonId from the Pokémon API
 * @param nickname as a personal description
 * @param pokemonName from the Pokémon APIc
 * @param pictureUrl from the Pokémon API
 * @param height from the Pokémon API
 * @param weight from the Pokémon API
 * @param types from the Pokémon API
 */
@Document("Favorites")
public record Pokemon(@Id String id,
              String pokemonId,
              String nickname,
              String pokemonName,
              String pictureUrl,
              Integer height,
              Integer weight,
              List<String> types) {
}
