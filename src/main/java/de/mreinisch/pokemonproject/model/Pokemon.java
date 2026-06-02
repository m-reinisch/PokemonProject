package de.mreinisch.pokemonproject.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("Favorites")
@RequiredArgsConstructor
@AllArgsConstructor
public class Pokemon{
    @Id private final String id;
    @With private String pokemonId;
    @With private String nickname;
    @With private String pokemonName;
    @With private String pictureUrl;
    @With private Integer height;
    @With private Integer weight;
    @With private List<String> types;
}
