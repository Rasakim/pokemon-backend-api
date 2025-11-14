package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.dto.pokemon.PageResponseSimplePokemonDto;
import im.rasak.pokemon.backend.api.dto.pokemon.SimplePokemonDto;
import im.rasak.pokemon.backend.api.models.Pokemon;

import java.util.List;

public interface PokemonService {

    SimplePokemonDto createPokemon(SimplePokemonDto pokemonDto);

    SimplePokemonDto getPokemonDtoByPokedexId(int pokedexId);

    SimplePokemonDto getPokemonByName(String name);

    List<SimplePokemonDto> getAllPokemons();

    SimplePokemonDto updatePokemonByPokedexId(SimplePokemonDto newPokemonDto, int pokedexIdToUpdate);

    SimplePokemonDto deletePokemonByPokedexId(int pokedexIdToDelete);

    List<SimplePokemonDto> deleteAllPokemon();

    Pokemon getPokemonEntityByPokedexId(int pokedexId);

    // TODO
    PageResponseSimplePokemonDto getAllPokemonPaged(int pageNumber, int pageSize);

}
