package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.dto.PokemonEntityDTO;
import im.rasak.pokemon.backend.api.dto.PokemonPageResponseDTO;

public interface PokemonService {

    PokemonEntityDTO createPokemon(PokemonEntityDTO pokemonEntityDTO);

    PokemonPageResponseDTO getAllPokemons(int pageNumber, int pageSize);

    PokemonEntityDTO getPokemonById(int id);

    PokemonEntityDTO getPokemonByPokedexId(int pokedexId);

    PokemonEntityDTO getPokemonByName(String name);

    PokemonEntityDTO updatePokemonById(PokemonEntityDTO pokemonEntityDTO, int id);

    void deletePokemonById(int id);

    void deleteAllPokemon();
}
