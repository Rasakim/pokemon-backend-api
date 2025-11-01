package im.rasak.pokemon.backend.api.repository;

import im.rasak.pokemon.backend.api.models.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonRepository extends JpaRepository<PokemonEntity, Integer> {

    Optional<PokemonEntity> findByName(String name);

    Optional<PokemonEntity> findByPokedexId(int pokedexId);
}
