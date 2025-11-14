package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.review.CreateReviewDto;
import im.rasak.pokemon.backend.api.dto.review.DeleteReviewDto;
import im.rasak.pokemon.backend.api.dto.review.ReviewDto;
import im.rasak.pokemon.backend.api.responses.ApiResponse;
import im.rasak.pokemon.backend.api.services.PokemonService;
import im.rasak.pokemon.backend.api.services.ReviewService;
import im.rasak.pokemon.backend.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons/{pokedexId}")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService, PokemonService pokemonService, UserDetailsService userDetailsService, UserService userService) {
        this.reviewService = reviewService;
    }

    @PostMapping("reviews")
    public ApiResponse<ReviewDto> createReview(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("pokedexId") int pokedexId, @RequestBody @Valid CreateReviewDto newReviewDto) {

        ReviewDto newReview = reviewService.createReview(authorizationHeader, pokedexId, newReviewDto);

        return ApiResponse.success(newReview, "Review created");
    }

    @GetMapping("reviews")
    public ApiResponse<List<ReviewDto>> getReviewsForPokemon(@PathVariable("pokedexId") int pokedexId) {

        List<ReviewDto> allReviews = reviewService.getReviewsForPokemon(pokedexId);

        return ApiResponse.success(allReviews);
    }

    @DeleteMapping("reviews")
    public ApiResponse<ReviewDto> deleteReview(@RequestHeader("Authorization") String authorizationHeader, @RequestBody @Valid DeleteReviewDto deleteReviewDto) {
        ReviewDto deletedReview = reviewService.deleteReview(authorizationHeader, deleteReviewDto);

        return ApiResponse.success(deletedReview, "Review deleted");
    }
}
