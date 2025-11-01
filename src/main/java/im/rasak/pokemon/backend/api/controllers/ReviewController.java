package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.ReviewEntityDTO;
import im.rasak.pokemon.backend.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("{pokedexId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewEntityDTO> createReview(@PathVariable("pokedexId") int pokedexId, @RequestBody ReviewEntityDTO review) {
        return new ResponseEntity<>(reviewService.createReview(pokedexId, review), HttpStatus.CREATED);
    }

    @GetMapping("{pokedexId}")
    public ResponseEntity<List<ReviewEntityDTO>> getReviewsByPokedexId(@PathVariable("pokedexId") int pokedexId) {
        return new ResponseEntity<>(reviewService.getReviewsByPokdexId(pokedexId), HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<ReviewEntityDTO> getReviewById(@PathVariable("id") int id) {
        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<String> deleteReview(@PathVariable("id") int id) {
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok("Review with id: {" + id + "} deleted successfully!");
    }
}
