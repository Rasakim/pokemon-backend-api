package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.dto.pokemon.PageResponseSimplePokemonDto;
import im.rasak.pokemon.backend.api.dto.pokemon.SimplePokemonDto;
import im.rasak.pokemon.backend.api.exceptions.PokemonNotFoundException;
import im.rasak.pokemon.backend.api.models.Pokemon;
import im.rasak.pokemon.backend.api.models.Review;
import im.rasak.pokemon.backend.api.repository.PokemonRepository;
import im.rasak.pokemon.backend.api.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonServiceImpl(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public SimplePokemonDto createPokemon(SimplePokemonDto pokemonDto) {
        Pokemon pokemon = mapDtoToEntity(pokemonDto);
        Pokemon newPokemon = pokemonRepository.save(pokemon);

        return mapEntityToDto(newPokemon);
    }

    @Override
    public SimplePokemonDto getPokemonDtoByPokedexId(int pokedexId) {
        Pokemon pokemon = getPokemonEntityByPokedexId(pokedexId);

        return mapEntityToDto(pokemon);
    }

    @Override
    public SimplePokemonDto getPokemonByName(String name) {
        Pokemon pokemon = pokemonRepository.findByName(name)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with name: " + name + " not found"));

        return mapEntityToDto(pokemon);
    }

    @Override
    public List<SimplePokemonDto> getAllPokemons() {
        List<Pokemon> findAllPokemon = pokemonRepository.findAll();

        if (findAllPokemon.isEmpty()) {
            throw new PokemonNotFoundException("No Pokemon in database");
        } else {
            List<SimplePokemonDto> allPokemon = new ArrayList<>();

            for (Pokemon p : findAllPokemon) {
                allPokemon.add(mapEntityToDto(p));
            }

            return allPokemon;
        }
    }

    @Override
    public SimplePokemonDto updatePokemonByPokedexId(SimplePokemonDto newPokemonDto, int pokedexIdToUpdate) {
        Pokemon pokemonToUpdate = pokemonRepository.findByPokedexId(pokedexIdToUpdate)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: " + pokedexIdToUpdate + " not found"));

        updateEntityFromDto(pokemonToUpdate, newPokemonDto);

        Pokemon updatedPokemon = pokemonRepository.save(pokemonToUpdate);

        return mapEntityToDto(updatedPokemon);
    }

    @Override
    public SimplePokemonDto deletePokemonByPokedexId(int pokedexIdToDelete) {
        Pokemon pokemonToDelete = pokemonRepository.findByPokedexId(pokedexIdToDelete)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: " + pokedexIdToDelete + " not found"));

        try {
            pokemonRepository.delete(pokemonToDelete);
        } catch (Exception ex) {
            throw new PokemonNotFoundException("Could not delete Pokemon with pokedexId: " + pokedexIdToDelete + ". Error: " + ex.getMessage());
        }

        return mapEntityToDto(pokemonToDelete);
    }

    @Override
    public List<SimplePokemonDto> deleteAllPokemon() {
        List<Pokemon> findAllPokemon = pokemonRepository.findAll();

        if (findAllPokemon.isEmpty()) {
            throw new PokemonNotFoundException("No Pokemon in database");
        } else {
            for (Pokemon p : findAllPokemon) {
                try {
                    pokemonRepository.delete(p);
                } catch (Exception ex) {
                    throw new PokemonNotFoundException("Could not delete Pokemon with pokedexId: " + p.getPokedexId() + ". Error: " + ex.getMessage());
                }
            }
        }

        return List.of();
    }

    @Override
    public Pokemon getPokemonEntityByPokedexId(int pokedexId) {
        Pokemon pokemon = pokemonRepository.findByPokedexId(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: " + pokedexId + " not found"));

        return pokemon;
    }

    @Override
    public PageResponseSimplePokemonDto getAllPokemonPaged(int pageNumber, int pageSize) {
        return null;
    }

    // UTIL
    private Pokemon mapDtoToEntity(SimplePokemonDto pokemonDto) {
        return new Pokemon(
                pokemonDto.getPokedexId(),
                pokemonDto.getName(),
                pokemonDto.getGeneration(),
                pokemonDto.getTypes(),
                pokemonDto.getDescription(),
                pokemonDto.getImageUrl()
        );
    }

    private SimplePokemonDto mapEntityToDto(Pokemon pokemon) {
        return new SimplePokemonDto(
                pokemon.getPokedexId(),
                pokemon.getName(),
                pokemon.getGeneration(),
                pokemon.getTypes(),
                pokemon.getDescription(),
                pokemon.getImageUrl(),
                getAverageRating(pokemon)
        );
    }

    private void updateEntityFromDto(Pokemon pokemonToUpdate, SimplePokemonDto newPokemonDto) {
        if (newPokemonDto.getPokedexId() != null) {
            pokemonToUpdate.setPokedexId(newPokemonDto.getPokedexId());
        }

        if (newPokemonDto.getName() != null) {
            pokemonToUpdate.setName(newPokemonDto.getName());
        }

        if (newPokemonDto.getGeneration() != null) {
            pokemonToUpdate.setGeneration(newPokemonDto.getGeneration());
        }

        if (newPokemonDto.getTypes() != null) {
            pokemonToUpdate.setTypes(newPokemonDto.getTypes());
        }

        if (newPokemonDto.getDescription() != null) {
            pokemonToUpdate.setDescription(newPokemonDto.getDescription());
        }

        if (newPokemonDto.getImageUrl() != null) {
            pokemonToUpdate.setImageUrl(newPokemonDto.getImageUrl());
        }
    }

    private Double getAverageRating(Pokemon pokemon) {
        double rating = 0.0;
        Set<Review> reviews = pokemon.getReviews();

        if (reviews.isEmpty()) {
            return rating;
        } else {
            for (Review r : reviews) {
                rating += r.getRating();
            }
            return rating / reviews.size();
        }
    }
}
