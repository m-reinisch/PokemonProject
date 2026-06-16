package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.dto.FavoriteDTO;
import de.mreinisch.pokemonproject.exception.IdNotFound;
import de.mreinisch.pokemonproject.exception.NameNotFound;
import de.mreinisch.pokemonproject.model.Pokemon;
import de.mreinisch.pokemonproject.repository.FavoritesRepo;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class CollectionServiceTest {

    @Test
    void readFavorites_shouldReturnList_whenDatabaseNotEmpty() {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        Pokemon pokemon= new Pokemon("1", "25", "Mein Starter", "pikachu",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                4, 60, "electric".lines().toList());
        List<Pokemon> expected= new ArrayList<>(List.of(pokemon));
        List<Pokemon> actual;

        when(mockingRepro.findAll()).thenReturn(expected);
        actual= service.readFavorites();
        assertEquals(expected, actual);
    }

    @Test
    void readFavorites_shouldReturnEmptyList_whenDatabaseEmpty() {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        List<Pokemon> expected= Collections.emptyList();
        List<Pokemon> actual;

        when(mockingRepro.findAll()).thenReturn(expected);
        actual= service.readFavorites();
        assertEquals(expected, actual);
    }

    @Test
    void findFavorite_shouldThrowException_whenNotFoundInDatabase() {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        String id= "1";

        assertThatExceptionOfType(IdNotFound.class)
                .isThrownBy( () -> service.findFavorite(id) )
                .withMessage("Searched Pokémon not found!");
        verify(mockingRepro, times(1)).findById(id);
        verifyNoMoreInteractions(mockingRepro);
    }

    @Test
    void findFavorite_shouldReturnPokemon_whenFoundInDatabase() throws IdNotFound {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        String id= "1";
        Pokemon expected= new Pokemon(id, "25", "Test Favorite", "pikachu",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                4, 60, "electric".lines().toList());
        Pokemon actual;

        when(mockingRepro.findById(id)).thenReturn(Optional.of(expected));
        actual= service.findFavorite(id);
        assertEquals(expected, actual);
        verify(mockingRepro, times(1)).findById(id);
        verifyNoMoreInteractions(mockingRepro);
    }

    @Test
    void removeFavorite_shouldThrowException_whenNotFoundInDatabase() throws IdNotFound {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        String id= "1";

        assertThatExceptionOfType(IdNotFound.class)
                .isThrownBy( () -> service.removeFavorite(id) )
                .withMessage("Pokémon to be deleted not found!");
        verify(mockingRepro, times(1)).findById(id);
        verify(mockingRepro, times(0)).deleteById(id);
        verifyNoMoreInteractions(mockingRepro);
    }

    @Test
    void removeFavorite_shouldReturnPokemon_whenDeleted() throws IdNotFound {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        String id= "1";
        Pokemon expected= new Pokemon(id, "25", "Test Favorite", "pikachu",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                4, 60, "electric".lines().toList());
        Pokemon actual;

        when(mockingRepro.findById(id)).thenReturn(Optional.of(expected));
        actual= service.removeFavorite(id);
        assertEquals(expected, actual);
        verify(mockingRepro, times(1)).findById(id);
        verify(mockingRepro, times(1)).deleteById(id);
        verifyNoMoreInteractions(mockingRepro);
    }

    @Test
    void generateFavorite_shouldReturnPokemon_whenSaved() throws NameNotFound {
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        String id= "1";
        FavoriteDTO favorite= new FavoriteDTO("pikachu","Test Favorite");
        Pokemon expected= new Pokemon(id, "25", "Test Favorite", "pikachu",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                4, 60, "electric".lines().toList());
        Pokemon actual;

        when(mockingIdService.generateId()).thenReturn(id);
        when(mockingRepro.save(expected)).thenReturn(expected);
        actual= service.generateFavorite(favorite);
        assertEquals(expected, actual);
        verify(mockingRepro, times(1)).save(expected);
        verifyNoMoreInteractions(mockingRepro);
    }

    @Test
    void generateFavorite_shouldThrowException_whenNotFoundInDatabase(){
        FavoritesRepo mockingRepro= mock(FavoritesRepo.class);
        IdService mockingIdService= mock(IdService.class);
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        CollectionService service= new CollectionService(restClientBuilder, mockingRepro, mockingIdService);
        String name= "clefair";
        FavoriteDTO favorite= new FavoriteDTO(name, "Test");

        mockRestServiceServer.expect(
                        requestTo("https://pokeapi.co/api/v2/pokemon/clefair"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        assertThatExceptionOfType(NameNotFound.class)
                .isThrownBy( () -> service.generateFavorite(favorite) )
                .withMessage("Searched name: clefair not found!");
    }
}