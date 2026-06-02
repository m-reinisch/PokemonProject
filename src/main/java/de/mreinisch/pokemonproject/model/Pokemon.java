package de.mreinisch.pokemonproject.model;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("Favorites")
public record Pokemon(@Id String id,
                      @With String pokemonId,
                      @With String nickname,
                      @With String pokemonName,
                      @With String pictureUrl,
                      @With Integer height,
                      @With Integer weight,
                      @With List<String> types) {
}
