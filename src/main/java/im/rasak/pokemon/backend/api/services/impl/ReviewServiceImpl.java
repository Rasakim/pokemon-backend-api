package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.dto.ReviewEntityDTO;
import im.rasak.pokemon.backend.api.exceptions.PokemonNotFoundException;
import im.rasak.pokemon.backend.api.models.PokemonEntity;
import im.rasak.pokemon.backend.api.models.ReviewEntity;
import im.rasak.pokemon.backend.api.repository.PokemonRepository;
import im.rasak.pokemon.backend.api.repository.ReviewRepository;
import im.rasak.pokemon.backend.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final PokemonRepository pokemonRepository;

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(PokemonRepository pokemonRepository, ReviewRepository reviewRepository) {
        this.pokemonRepository = pokemonRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewEntityDTO createReview(int pokedexId, ReviewEntityDTO reviewEntityDTO) {

        PokemonEntity pokemon = pokemonRepository.findByPokedexId(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: {" + pokedexId + "} not found!"));

        ReviewEntity review = mapToEntity(reviewEntityDTO);

        review.setPokemonEntity(pokemon);

        ReviewEntity newReview = reviewRepository.save(review);

        return mapToDTO(newReview);
    }

    @Override
    public List<ReviewEntityDTO> getReviewsByPokdexId(int pokedexId) {

        List<ReviewEntity> findAllReviewsByPokedexId = reviewRepository.findAllByPokemonEntity_PokedexId(pokedexId);
        List<ReviewEntityDTO> allReviews = new ArrayList<>();

        for (ReviewEntity r : findAllReviewsByPokedexId) {
            allReviews.add(mapToDTO(r));
        }

        return allReviews;
    }

    private ReviewEntity mapToEntity(ReviewEntityDTO reviewEntityDTO) {
        ReviewEntity mappedEntity = new ReviewEntity();

        mappedEntity.setTitle(reviewEntityDTO.getTitle());
        mappedEntity.setContent(reviewEntityDTO.getContent());
        mappedEntity.setStars(reviewEntityDTO.getStars());

        return mappedEntity;
    }

    private ReviewEntityDTO mapToDTO(ReviewEntity reviewEntity) {
        ReviewEntityDTO mappedEntity = new ReviewEntityDTO();

        mappedEntity.setId(reviewEntity.getId());
        mappedEntity.setTitle(reviewEntity.getTitle());
        mappedEntity.setContent(reviewEntity.getContent());
        mappedEntity.setStars(reviewEntity.getStars());
        mappedEntity.setPokedexId(reviewEntity.getPokemonEntity().getPokedexId());

        return mappedEntity;
    }
}
