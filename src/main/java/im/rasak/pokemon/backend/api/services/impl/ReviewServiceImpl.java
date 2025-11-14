package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.dto.review.CreateReviewDto;
import im.rasak.pokemon.backend.api.dto.review.DeleteReviewDto;
import im.rasak.pokemon.backend.api.dto.review.ReviewDto;
import im.rasak.pokemon.backend.api.exceptions.ReviewNotFoundException;
import im.rasak.pokemon.backend.api.models.Pokemon;
import im.rasak.pokemon.backend.api.models.Review;
import im.rasak.pokemon.backend.api.models.UserEntity;
import im.rasak.pokemon.backend.api.repository.ReviewRepository;
import im.rasak.pokemon.backend.api.services.PokemonService;
import im.rasak.pokemon.backend.api.services.ReviewService;
import im.rasak.pokemon.backend.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final PokemonService pokemonService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService, PokemonService pokemonService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.pokemonService = pokemonService;
    }

    @Override
    public ReviewDto createReview(String authorizationHeader, int pokedexId, CreateReviewDto review) {

        String userToken = authorizationHeader.substring(7);
        UserEntity user = userService.getUserEntityFromToken(userToken);
        Pokemon pokemon = pokemonService.getPokemonEntityByPokedexId(pokedexId);

        Review newReview = new Review(
                pokemon,
                user,
                review.getContent(),
                review.getRating()
        );

        Review createdReview = reviewRepository.save(newReview);

        return mapEntityToDto(createdReview);
    }

    @Override
    public List<ReviewDto> getReviewsForPokemon(int pokedexId) {
        List<Review> findReviews = reviewRepository.findAllByPokedexId(pokedexId);
        List<ReviewDto> allReviews = new ArrayList<>();

        for (Review r : findReviews) {
            allReviews.add(mapEntityToDto(r));
        }

        return allReviews;
    }

    @Override
    public ReviewDto deleteReview(String authorizationHeader, DeleteReviewDto deleteReviewDto) {

        String userToken = authorizationHeader.substring(7);

        UserEntity user = userService.getUserEntityFromToken(userToken);

        Review reviewToDelete = reviewRepository.findById(deleteReviewDto.getId())
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: " + deleteReviewDto.getId() + " not found"));

        if (user.getId().equals(reviewToDelete.getAuthor().getId()) || user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()))) {
            reviewRepository.delete(reviewToDelete);
        } else {
            throw new AuthorizationDeniedException("You can only delete your own reviews");
        }


        return mapEntityToDto(reviewToDelete);
    }

    private ReviewDto mapEntityToDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getPokemon().getPokedexId(),
                review.getAuthor().getId(),
                review.getAuthor().getUsername(),
                review.getCreatedAt(),
                review.getContent(),
                review.getRating()
        );
    }
}
