package de.mreinisch.pokemonproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OtherDTO {
    @JsonProperty("official-artwork")
    private OaDTO officialArtwork;
}
