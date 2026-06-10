package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.dto.PokeApiDTO;
import de.mreinisch.pokemonproject.exception.NameNotFound;
import de.mreinisch.pokemonproject.model.Pokemon;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonService {
    private final RestClient restClient;

    public PokemonService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://pokeapi.co/api/v2/pokemon")
                .build();
    }

    /** Shows Pokémon with the requested name.
     *
     * @param name to search for
     * @return found Pokémon
     * @throws NameNotFound when Pokémon not found
     */
    public Pokemon findPokeByName(String name) throws NameNotFound {
        PokeApiDTO pokeApiDTO= searchPokeAPIforName(name);
        List<String> types= new ArrayList<>();

        pokeApiDTO.types()
                .forEach(t -> types.add(t.getType().getName()));
        return new Pokemon(null,
                pokeApiDTO.id(),
                null,
                pokeApiDTO.name(),
                pokeApiDTO.sprites()
                        .getOther().getOfficialArtwork()
                        .getFront_default(),
                pokeApiDTO.height(),
                pokeApiDTO.weight(),
                types);
    }

    /** Searches for Pokémon by name in the PokeAPI.
     *
     * @param name to search for
     * @return found Pokémon
     * @throws NameNotFound when Pokémon not in API
     */
    private PokeApiDTO searchPokeAPIforName(String name) throws NameNotFound {
        PokeApiDTO apiDTO;

        try {
            apiDTO= restClient.get()
                    .uri("/" + name)
                    .retrieve()
                    .body(PokeApiDTO.class);
            return apiDTO;
        } catch (HttpClientErrorException ex) {
            throw new NameNotFound("Searched name: " + name + " not found!");
        }
    }
}
