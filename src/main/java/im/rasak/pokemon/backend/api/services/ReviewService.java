package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.dto.ReviewEntityDTO;
import im.rasak.pokemon.backend.api.models.ReviewEntity;

import java.util.List;

public interface ReviewService {

    ReviewEntityDTO createReview(int pokedexId, ReviewEntityDTO reviewEntityDTO);

    ReviewEntityDTO getReviewById(int id);

    List<ReviewEntityDTO> getReviewsByPokdexId(int pokedexId);

    void deleteReviewById(int id);
}
