package de.mreinisch.pokemonproject.controller;

import de.mreinisch.pokemonproject.dto.FavoriteDTO;
import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.service.CollectionService;
import de.mreinisch.pokemonproject.service.PokemonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PokeController {
    private final PokemonService pokeService;
    private final CollectionService favoriteService;

    public PokeController(PokemonService pokeService, CollectionService favoriteService) {
        this.pokeService = pokeService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/pokemon/{name}")
    public Pokemon getPokemonByName(@PathVariable String name){
        return pokeService.findPokeByName(name);
    }

    @PostMapping("/collection")
    public Pokemon savePokemonAsFavorite(@RequestBody FavoriteDTO favorite){
        return favoriteService.generateFavorite(favorite);
    }
}
