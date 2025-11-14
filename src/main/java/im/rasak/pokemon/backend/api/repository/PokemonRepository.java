package im.rasak.pokemon.backend.api.repository;

import im.rasak.pokemon.backend.api.models.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

    Optional<Pokemon> findByName(String name);

    Optional<Pokemon> findByPokedexId(int pokedexId);
}
