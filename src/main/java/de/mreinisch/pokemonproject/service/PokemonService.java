package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.model.Pokemon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class PokemonService {
    public Pokemon findPokeByName(String name){
        return new Pokemon("1", "35", null, name,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/35.png",
                6, 75,
                new ArrayList<>(Collections.singleton("fairy")));
    }
}
