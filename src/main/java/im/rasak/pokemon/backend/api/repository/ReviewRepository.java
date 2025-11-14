package im.rasak.pokemon.backend.api.repository;

import im.rasak.pokemon.backend.api.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.pokemon.pokedexId = :pokedexId")
    List<Review> findAllByPokedexId(int pokedexId);
}
