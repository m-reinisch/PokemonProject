package de.mreinisch.pokemonproject.service;

import de.mreinisch.pokemonproject.exception.NameNotFound;
import de.mreinisch.pokemonproject.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PokemonServiceTest {

    @Test
    void findPokeByName_shouldReturnPokemon_whenFoundInPokeApi() throws NameNotFound {
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        PokemonService service= new PokemonService(restClientBuilder);
        String name= "clefairy";
        Pokemon expected= new Pokemon(null,
                "35", null, name,
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/35.png",
                6, 75, "fairy".lines().toList());
        Pokemon actual;

        mockRestServiceServer.expect(
                requestTo("https://pokeapi.co/api/v2/pokemon/clefairy"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
                        "height": "6",
                        "id": "35",
                        "name": "clefairy",
                        "sprites": {
                            "other": {
                              "official-artwork": {
                                "front_default": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/35.png"
                              }
                            }
                        },
                        "types": [
                            {
                               "type": {
                                  "name": "fairy"
                               }
                            }
                        ],
                        "weight": "75"
                    }
                """, MediaType.APPLICATION_JSON));
        actual= service.findPokeByName(name);
        assertEquals(expected, actual);
    }

    @Test
    void findPokeByName_shouldThrowException_whenNotFoundInPokeApi(){
        RestClient.Builder restClientBuilder= RestClient.builder();
        MockRestServiceServer mockRestServiceServer= MockRestServiceServer
                .bindTo(restClientBuilder).build();
        PokemonService service= new PokemonService(restClientBuilder);
        String name= "clefair";

        mockRestServiceServer.expect(
                requestTo("https://pokeapi.co/api/v2/pokemon/clefair"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        assertThatExceptionOfType(NameNotFound.class)
                .isThrownBy( () -> service.findPokeByName(name) )
                .withMessage("Searched name: clefair not found!");
    }
}