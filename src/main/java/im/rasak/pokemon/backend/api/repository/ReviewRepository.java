package im.rasak.pokemon.backend.api.repository;

import im.rasak.pokemon.backend.api.models.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    List<ReviewEntity> findAllByPokemonEntity_PokedexId(int pokedexId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.pokemonEntity.pokedexId = :pokedexId")
    Page<ReviewEntity> findAllReviewsForPokemonByPokedexId(@Param("pokedexId") int pokedexId, Pageable pageable);

}
