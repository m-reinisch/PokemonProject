package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.dto.FavoriteDTO;
import de.mreinisch.pokemonproject.dto.PokeApiDTO;
import de.mreinisch.pokemonproject.dto.UpdateDTO;
import de.mreinisch.pokemonproject.exception.IdNotFound;
import de.mreinisch.pokemonproject.exception.NameNotFound;
import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.repository.FavoritesRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    /** Displays all favorites.
     *
     * @return list of Pokémons
     */
    public List<Pokemon> readFavorites(){
        return repo.findAll();
    }

    /** Displays the favorite with the specified ID.
     *
     * @param id to search for
     * @return Pokémon
     * @throws IdNotFound when not in database
     */
    public Pokemon findFavorite(String id) throws IdNotFound {
        Pokemon pokemon= repo.findById(id).orElse(null);

        if (pokemon != null) {
            return pokemon;
        } else {
            throw new IdNotFound("Searched Pokémon not found!");
        }
    }

    /** Displays the favorite with the specified ID.
     *
     * @param id to search for
     * @return Pokémon
     * @throws IdNotFound when not in database
     */
    public Pokemon removeFavorite(String id) throws IdNotFound {
        Pokemon pokemon= repo.findById(id).orElse(null);

        if (pokemon != null) {
            repo.deleteById(id);
        } else {
            throw new IdNotFound("Pokémon to be deleted not found!");
        }
        return pokemon;
    }

    /** Saves the Pokémon with the specified name as a favorite.
     *
     * @param favorite to save
     * @return saved favorite
     */
    public Pokemon generateFavorite(FavoriteDTO favorite) throws NameNotFound {
        PokeApiDTO pokeApiDTO= searchPokeAPIforName(favorite.pokemonName());
        List<String> types= new ArrayList<>();
        Pokemon pokemon;

        pokeApiDTO.types()
                .forEach(t -> types.add(t.getType().getName()));
        pokemon= new Pokemon(idService.generateId(),
                pokeApiDTO.id(),
                favorite.nickname(),
                pokeApiDTO.name(),
                pokeApiDTO.sprites().getOther().getOfficialArtwork().getFront_default(),
                pokeApiDTO.height(),
                pokeApiDTO.weight(),
                types);
        repo.save(pokemon);
        return pokemon;
    }

    /** Changes the nickname of the desired Pokémon.
     *
     * @param id to search for
     * @param newNickname to be saved
     * @return  Pokémon
     * @throws IdNotFound when not in database
     */
    public Pokemon updateFavorite(String id, UpdateDTO newNickname) throws IdNotFound {
        Pokemon oldPokemon= findFavorite(id);
        Pokemon newPokemon;

        newPokemon= new Pokemon(id,
                                oldPokemon.pokemonId(),
                                newNickname.nickname(),
                                oldPokemon.pokemonName(),
                                oldPokemon.pictureUrl(),
                                oldPokemon.height(),
                                oldPokemon.weight(),
                                oldPokemon.types());
        repo.deleteById(id);
        repo.save(newPokemon);
        return newPokemon;
    }

    /** Searches for Pokémon by name in the PokeAPI.
     *
     * @param name to search for
     * @return found Pokémon
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
