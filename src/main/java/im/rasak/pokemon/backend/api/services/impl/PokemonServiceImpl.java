package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.dto.PokemonEntityDTO;
import im.rasak.pokemon.backend.api.dto.PokemonPageResponseDTO;
import im.rasak.pokemon.backend.api.exceptions.PokemonNotFoundException;
import im.rasak.pokemon.backend.api.models.PokemonEntity;
import im.rasak.pokemon.backend.api.repository.PokemonRepository;
import im.rasak.pokemon.backend.api.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonServiceImpl(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public PokemonEntityDTO createPokemon(PokemonEntityDTO pokemonEntityDTO) {
        PokemonEntity pokemon = mapToEntity(pokemonEntityDTO);
        PokemonEntity newPokemon = pokemonRepository.save(pokemon);

        return mapToDTO(newPokemon);
    }

    @Override
    public PokemonPageResponseDTO getAllPokemons(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PokemonEntity> allPokemons = pokemonRepository.findAll(pageable);
        List<PokemonEntity> listOfPokemons = allPokemons.getContent();

        List<PokemonEntityDTO> content = new ArrayList<>();

        for (PokemonEntity pokemon : listOfPokemons) {
            content.add(mapToDTO(pokemon));
        }

        PokemonPageResponseDTO pokemonPageResponseDTO = new PokemonPageResponseDTO();
        pokemonPageResponseDTO.setContent(content);
        pokemonPageResponseDTO.setPageNumber(allPokemons.getNumber());
        pokemonPageResponseDTO.setPageSize(allPokemons.getSize());
        pokemonPageResponseDTO.setTotalElements(allPokemons.getTotalElements());
        pokemonPageResponseDTO.setTotalPages(allPokemons.getTotalPages());
        pokemonPageResponseDTO.setLastPage(allPokemons.isLast());

        return pokemonPageResponseDTO;
    }

    @Override
    public PokemonEntityDTO getPokemonByPokedexId(int pokedexId) {
        PokemonEntity pokemonEntity = pokemonRepository.findByPokedexId(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with pokedexId: {" + pokedexId + "} not found!"));

        return mapToDTO(pokemonEntity);
    }

    @Override
    public PokemonEntityDTO getPokemonByName(String name) {
        PokemonEntity pokemonEntity = pokemonRepository.findByName(name)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with name: {" + name + "} not found!"));

        return mapToDTO(pokemonEntity);
    }

    @Override
    public PokemonEntityDTO updatePokemonByPokedexId(PokemonEntityDTO pokemonEntityDTO, int pokedexId) {
        PokemonEntity pokemonEntity = pokemonRepository.findById(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with id: {" + pokedexId + "} not found!"));

        updateEntityFromDTO(pokemonEntity, pokemonEntityDTO);

        PokemonEntity updatedEntity = pokemonRepository.save(pokemonEntity);

        return mapToDTO(updatedEntity);
    }

    @Override
    public void deletePokemonByPokedexId(int pokedexId) {
        PokemonEntity pokemonEntity = pokemonRepository.findById(pokedexId)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon with id: {" + pokedexId + "} not found!"));

        pokemonRepository.delete(pokemonEntity);
    }

    @Override
    public void deleteAllPokemon() {
        List<PokemonEntity> allPokemon = pokemonRepository.findAll();

        for (PokemonEntity pokemonEntity : allPokemon) {
            pokemonRepository.delete(pokemonEntity);
        }
    }

    private PokemonEntityDTO mapToDTO(PokemonEntity pokemonEntity) {

        PokemonEntityDTO pokemonEntityDTO = new PokemonEntityDTO();
        pokemonEntityDTO.setPokedexId(pokemonEntity.getPokedexId());
        pokemonEntityDTO.setName(pokemonEntity.getName());
        pokemonEntityDTO.setTypes(pokemonEntity.getTypes());
        pokemonEntityDTO.setImageUrl(pokemonEntity.getImageUrl());

        return pokemonEntityDTO;
    }

    private PokemonEntity mapToEntity(PokemonEntityDTO pokemonEntityDTO) {
        PokemonEntity pokemonEntity = new PokemonEntity();

        pokemonEntity.setPokedexId(pokemonEntityDTO.getPokedexId());
        pokemonEntity.setName(pokemonEntityDTO.getName());
        pokemonEntity.setTypes(pokemonEntityDTO.getTypes());
        pokemonEntity.setImageUrl(pokemonEntityDTO.getImageUrl());

        return pokemonEntity;
    }

    private void updateEntityFromDTO(PokemonEntity pokemonEntity, PokemonEntityDTO pokemonEntityDTO) {
        if (pokemonEntityDTO.getPokedexId() != null) {
            pokemonEntity.setPokedexId(pokemonEntityDTO.getPokedexId());
        }

        if (pokemonEntityDTO.getName() != null && !pokemonEntityDTO.getName().isEmpty()) {
            pokemonEntity.setName(pokemonEntityDTO.getName());
        }

        if (pokemonEntityDTO.getTypes() != null && !pokemonEntityDTO.getTypes().isEmpty()) {
            pokemonEntity.setTypes(pokemonEntityDTO.getTypes());
        }

        if (pokemonEntityDTO.getImageUrl() != null && !pokemonEntityDTO.getImageUrl().isEmpty()) {
            pokemonEntity.setImageUrl(pokemonEntityDTO.getImageUrl());
        }
    }
}
