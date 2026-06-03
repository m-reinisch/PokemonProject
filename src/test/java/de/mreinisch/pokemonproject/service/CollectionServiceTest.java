package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.repository.FavoritesRepo;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionServiceTest {

    @Test
    void readFavorites_shouldReturnList_whenDatabaseNotEmpty() {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder mockingClientBuilder= mock(RestClient.Builder.class);
//        CollectionService service= new CollectionService(mockingClientBuilder, mockingRepro, mockingIdService);
//        Pokemon pokemon= new Pokemon("1", "25", "Mein Starter", "pikachu",
//                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
//                4, 60, "electric".lines().toList());
//        ArrayList<Pokemon> expected= new ArrayList<>(List.of(pokemon));
//        ArrayList<Pokemon> actual;
//
//        when(mockingRepro.findAll()).thenReturn(expected);
//        actual= (ArrayList<Pokemon>)service.readFavorite();
//        assertEquals(expected, actual);
    }

    @Test
    void testReadFavorite() {
    }

    @Test
    void removeFavorite() {
    }

    @Test
    void generateFavorite() {
    }
}