package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.dto.ReviewEntityDTO;
import im.rasak.pokemon.backend.api.dto.ReviewPageResponseDTO;
import im.rasak.pokemon.backend.api.exceptions.PokemonNotFoundException;
import im.rasak.pokemon.backend.api.exceptions.ReviewNotFoundException;
import im.rasak.pokemon.backend.api.models.PokemonEntity;
import im.rasak.pokemon.backend.api.models.ReviewEntity;
import im.rasak.pokemon.backend.api.repository.PokemonRepository;
import im.rasak.pokemon.backend.api.repository.ReviewRepository;
import im.rasak.pokemon.backend.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ReviewEntityDTO getReviewById(int pokedexId, int reviewId) {

        PokemonEntity pokemonEntity = pokemonRepository.findByPokedexId(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: {" + pokedexId + "} not found!"));

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: {" + reviewId + "} not found!"));

        if (pokemonEntity.getPokedexId() != reviewEntity.getPokemonEntity().getPokedexId()) {
            throw new ReviewNotFoundException("PokedexId: {" + pokedexId + "} does not belong to this ReviewId: {" + reviewId + "}!");
        } else {
            return mapToDTO(reviewEntity);
        }
    }

    @Override
    public ReviewPageResponseDTO getAllReviewsForPokemonByPokedexId(int pokedexId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ReviewEntity> allReviewsForPokemon = reviewRepository.findAllReviewsForPokemonByPokedexId(pokedexId, pageable);
        List<ReviewEntity> listOfAllReviewsForPokemon = allReviewsForPokemon.getContent();

        return mapToReviewPageResponseDTO(listOfAllReviewsForPokemon, allReviewsForPokemon);
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

    @Override
    public void deleteReviewById(int pokedexId, int reviewId) {

        PokemonEntity pokemonEntity = pokemonRepository.findByPokedexId(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: {" + pokedexId + "} not found!"));

        ReviewEntity reviewEntityToDelete = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: {" + reviewId + "} not found!"));

        if (pokemonEntity.getPokedexId() != reviewEntityToDelete.getPokemonEntity().getPokedexId()) {
            throw new ReviewNotFoundException("PokedexId: {" + pokedexId + "} does not belong to this ReviewId: {" + reviewId + "}!");
        } else {
            reviewRepository.delete(reviewEntityToDelete);
        }
    }

    @Override
    public ReviewEntityDTO updateReviewById(ReviewEntityDTO reviewEntityDTO, int pokedexId, int reviewId) {

        PokemonEntity pokemonEntity = pokemonRepository.findByPokedexId(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: {" + pokedexId + "} not found!"));

        ReviewEntity reviewEntityToUpdate = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: {" + reviewId + "} not found!"));

        if (pokemonEntity.getPokedexId() != reviewEntityToUpdate.getPokemonEntity().getPokedexId()) {
            throw new ReviewNotFoundException("PokedexId: {" + pokedexId + "} does not belong to this ReviewId: {" + reviewId + "}!");
        } else {
            updateEntityFromDTO(reviewEntityToUpdate, reviewEntityDTO);
            reviewRepository.save(reviewEntityToUpdate);

            return mapToDTO(reviewEntityToUpdate);
        }
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

    private void updateEntityFromDTO(ReviewEntity reviewEntityToUpdate, ReviewEntityDTO reviewEntityDTO) {
        if ((reviewEntityDTO.getPokedexId() != null) && (reviewEntityToUpdate.getPokemonEntity().getPokedexId() != reviewEntityDTO.getPokedexId())) {
            PokemonEntity newPokemonEntity = pokemonRepository.findByPokedexId(reviewEntityDTO.getPokedexId())
                    .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: {" + reviewEntityDTO.getPokedexId() + "} not found!"));
            reviewEntityToUpdate.setPokemonEntity(newPokemonEntity);
        }

        if (reviewEntityDTO.getTitle() != null && !reviewEntityDTO.getTitle().isEmpty()) {
            reviewEntityToUpdate.setTitle(reviewEntityDTO.getTitle());
        }

        if (reviewEntityDTO.getContent() != null && !reviewEntityDTO.getContent().isEmpty()) {
            reviewEntityToUpdate.setContent(reviewEntityDTO.getContent());
        }

        if (reviewEntityDTO.getStars() >= 0 && reviewEntityDTO.getStars() <= 5) {
            reviewEntityToUpdate.setStars(reviewEntityDTO.getStars());
        }
    }

    private ReviewPageResponseDTO mapToReviewPageResponseDTO(List<ReviewEntity> listOfAllReviewsForPokemon, Page<ReviewEntity> allReviewsForPokemon) {

        List<ReviewEntityDTO> content = new ArrayList<>();

        for (ReviewEntity review : listOfAllReviewsForPokemon) {
            content.add(mapToDTO(review));
        }
        ReviewPageResponseDTO reviewPageResponseDTO = new ReviewPageResponseDTO();
        reviewPageResponseDTO.setContent(content);
        reviewPageResponseDTO.setPageNumber(allReviewsForPokemon.getNumber());
        reviewPageResponseDTO.setPageSize(allReviewsForPokemon.getSize());
        reviewPageResponseDTO.setTotalElements(allReviewsForPokemon.getTotalElements());
        reviewPageResponseDTO.setTotalPages(allReviewsForPokemon.getTotalPages());
        reviewPageResponseDTO.setLastPage(allReviewsForPokemon.isLast());
        return reviewPageResponseDTO;
    }
}
