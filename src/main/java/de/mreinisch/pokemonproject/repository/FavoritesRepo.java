package de.mreinisch.pokemonproject.repository;

import de.mreinisch.pokemonproject.model.Pokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepo extends MongoRepository<Pokemon, String> {
}
