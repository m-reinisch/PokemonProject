package de.mreinisch.pokemonproject.controller;

import de.mreinisch.pokemonproject.dto.FavoriteDTO;
import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.service.CollectionService;
import de.mreinisch.pokemonproject.service.PokemonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class PokeController {
    private final PokemonService pokeService;
    private final CollectionService favoriteService;

    public PokeController(PokemonService pokeService, CollectionService favoriteService) {
        this.pokeService = pokeService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/pokemon/{name}")
    public Pokemon getPokemonByName(@PathVariable
                                        @Size(min= 2, message= "The PokémonName must contain at least 2 characters.")
                                        String name){
        return pokeService.findPokeByName(name);
    }

    @PostMapping("/collection")
    @ResponseStatus(HttpStatus.CREATED)
    public Pokemon savePokemonAsFavorite(@Valid @RequestBody FavoriteDTO favorite){
        return favoriteService.generateFavorite(favorite);
    }
}
