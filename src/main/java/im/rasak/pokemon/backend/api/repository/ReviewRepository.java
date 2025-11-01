package im.rasak.pokemon.backend.api.repository;

import im.rasak.pokemon.backend.api.models.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    List<ReviewEntity> findAllByPokemonEntity_PokedexId(int pokedexId);

}
