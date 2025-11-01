package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.ReviewEntityDTO;
import im.rasak.pokemon.backend.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("{pokedexId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewEntityDTO> createReview(@PathVariable("pokedexId") int pokedexId, @RequestBody ReviewEntityDTO review) {
        return new ResponseEntity<>(reviewService.createReview(pokedexId, review), HttpStatus.CREATED);
    }

    @GetMapping("{pokedexId}/reviews")
    public ResponseEntity<List<ReviewEntityDTO>> getReviewsByPokedexId(@PathVariable("pokedexId") int pokedexId) {
        return new ResponseEntity<>(reviewService.getReviewsByPokdexId(pokedexId), HttpStatus.OK);
    }

    @GetMapping("{pokedexId}/reviews/{reviewId}")
    public ResponseEntity<ReviewEntityDTO> getReviewById(@PathVariable("pokedexId") int pokedexId, @PathVariable("reviewId") int reviewId) {
        return new ResponseEntity<>(reviewService.getReviewById(pokedexId, reviewId), HttpStatus.OK);
    }

    @DeleteMapping("{pokedexId}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("pokedexId") int pokedexId, @PathVariable("reviewId") int reviewId) {
        reviewService.deleteReviewById(pokedexId, reviewId);
        return ResponseEntity.ok("Review with id: {" + reviewId + "} deleted successfully!");
    }
}
