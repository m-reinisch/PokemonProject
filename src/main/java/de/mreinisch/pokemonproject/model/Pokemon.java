package de.mreinisch.pokemonproject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("Favorites")
public record Pokemon(@Id String id,
                      String pokemonId,
                      String nickname,
                      String pokemonName,
                      String pictureUrl,
                      int height,
                      int weight,
                      List<String> types) {
}
