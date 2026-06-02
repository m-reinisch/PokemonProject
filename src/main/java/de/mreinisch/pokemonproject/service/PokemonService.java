package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.dto.PokeApiDTO;
import de.mreinisch.pokemonproject.model.Pokemon;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
     */
    public Pokemon findPokeByName(String name){
        PokeApiDTO pokeApiDTO= searchPokeAPIforName(name);
        List<String> types= new ArrayList<>();
        Pokemon pokemon= new Pokemon("1");

        pokeApiDTO.types()
                .forEach(t -> types.add(t.getType().getName()));
        return pokemon.withPokemonId(pokeApiDTO.id())
                .withPokemonName(pokeApiDTO.name())
//                .withPictureUrl(pokeApiDTO.sprites()
//                        .getOther().getOfficialartwork()
//                        .getFront_default())
                .withHeight(pokeApiDTO.height())
                .withWeight(pokeApiDTO.weight())
                .withTypes(types);
    }

    /** Searches for Pokémon by name in the PokeAPI.
     *
     * @param name to search for
     * @return found Pokémon
     */
    private PokeApiDTO searchPokeAPIforName(String name){
        return restClient.get()
                .uri("/" + name)
                .retrieve()
                .body(PokeApiDTO.class);
    }
}
