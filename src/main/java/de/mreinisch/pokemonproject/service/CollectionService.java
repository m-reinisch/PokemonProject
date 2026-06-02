package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.dto.FavoriteDTO;
import de.mreinisch.pokemonproject.dto.PokeApiDTO;
import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.repository.FavoritesRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionService {
    private final RestClient restClient;
    private final FavoritesRepo repo;
    private final IdService idService;

    public CollectionService(RestClient.Builder builder, FavoritesRepo repo, IdService idService) {
        this.restClient = builder
                .baseUrl("https://pokeapi.co/api/v2/pokemon")
                .build();
        this.repo = repo;
        this.idService = idService;
    }

    /** Saves the Pokémon with the specified name as a favorite.
     *
     * @param favorite to save
     * @return saved favorite
     */
    public Pokemon generateFavorite(FavoriteDTO favorite){
        PokeApiDTO pokeApiDTO= searchPokeAPIforName(favorite.pokemonName());
        List<String> types= new ArrayList<>();
        Pokemon pokemon;

        pokeApiDTO.types()
                .forEach(t -> types.add(t.getType().getName()));
        pokemon= new Pokemon(idService.generateId(),
                pokeApiDTO.id(),
                favorite.nickname(),
                pokeApiDTO.name(),
                null,
                pokeApiDTO.height(),
                pokeApiDTO.weight(),
                types);
        repo.save(pokemon);
        return pokemon;
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
