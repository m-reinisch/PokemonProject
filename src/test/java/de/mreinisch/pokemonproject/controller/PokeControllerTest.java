package de.mreinisch.pokemonproject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.MockServerRestClientCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PokeControllerTest {
    @TestConfiguration()
    static class TestConfig {
        private final MockServerRestClientCustomizer customizer = new MockServerRestClientCustomizer();
        private final RestClient.Builder customizedBuilder = RestClient.builder();

        public TestConfig() {
            customizer.customize(customizedBuilder);
        }

        @Bean
        public RestClient.Builder restClientBuilder() {
            return customizedBuilder;
        }

        @Bean
        public MockRestServiceServer mockRestServiceServer() {
            return customizer.getServer(customizedBuilder);
        }
    }

    @Autowired
    MockMvc mvc;
    @Autowired
    MockRestServiceServer restServiceServer;
    @BeforeEach
    void setUp() {
        restServiceServer.reset();
    }

    @Test
    void getPokemonByName_shouldReturnPokemon_whenCalledWithCorrectName() throws Exception {
        restServiceServer.expect(
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
        mvc.perform(get("/api/pokemon/clefairy"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                  {
                    "id": null,
                    "pokemonId": "35",
                    "nickname": null,
                    "pokemonName": "clefairy",
                    "pictureUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/35.png",
                    "height": 6,
                    "weight": 75,
                    "types": [
                        "fairy"
                    ]
                  }
                """));
    }

    @Test
    void getPokemonByName_shouldThrowException_whenNameNotFound() throws Exception {
        String errorMessage= "An error occurred during the search in PokémonAPI: ";

        errorMessage+= "Searched name: clefair not found!";
        restServiceServer.expect(
                requestTo("https://pokeapi.co/api/v2/pokemon/clefair"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        mvc.perform(get("/api/pokemon/clefair"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void savePokemonAsFavorite_shouldReturnPokemon_whenSavedInDatabase() throws Exception {
        restServiceServer.expect(
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
        mvc.perform(post("/api/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "pokemonName": "clefairy",
                                    "nickname": "Meine liebste Fee"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                  {
                    "pokemonId": "35",
                    "nickname": "Meine liebste Fee",
                    "pokemonName": "clefairy",
                    "pictureUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/35.png",
                    "height": 6,
                    "weight": 75,
                    "types": [
                        "fairy"
                    ]
                  }
                """));
    }

    @Test
    void savePokemonAsFavorite_shouldThrowException_whenInvalidBody() throws Exception {
        restServiceServer.expect(
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
        mvc.perform(post("/api/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "pokemonName": "clefairy",
                                    "nickname": "Me"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                    {
                        "nickname": "The nickname must contain at least 3 characters."
                    }
                """));
    }
}