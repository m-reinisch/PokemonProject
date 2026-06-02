package de.mreinisch.pokemonproject.controller;

import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PokeController {
    private final PokemonService pokeService;

    public PokeController(PokemonService pokeService) {
        this.pokeService = pokeService;
    }

    @GetMapping("/pokemon/{name}")
    public Pokemon getPokemonByName(@PathVariable String name){
        return pokeService.findPokeByName(name);
    }
}
