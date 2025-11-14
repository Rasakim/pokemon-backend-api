package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.dto.review.CreateReviewDto;
import im.rasak.pokemon.backend.api.dto.review.DeleteReviewDto;
import im.rasak.pokemon.backend.api.dto.review.ReviewDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ReviewService {
    ReviewDto createReview(String authorizationHeader, int pokedexId, @Valid CreateReviewDto newReview);

    List<ReviewDto> getReviewsForPokemon(int pokedexId);

    ReviewDto deleteReview(String authorizationHeader, @Valid DeleteReviewDto deleteReviewDto);
}
