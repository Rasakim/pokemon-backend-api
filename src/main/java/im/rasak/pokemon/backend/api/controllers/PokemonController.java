package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.pokemon.SimplePokemonDto;
import im.rasak.pokemon.backend.api.responses.ApiResponse;
import im.rasak.pokemon.backend.api.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SimplePokemonDto>>> getAllPokemon() {

        List<SimplePokemonDto> allPokemon = pokemonService.getAllPokemons();

        return ResponseEntity.ok(ApiResponse.success(allPokemon));
    }

    @GetMapping("{pokedexId}")
    public ResponseEntity<ApiResponse<SimplePokemonDto>> getPokemonByPokedexId(@PathVariable("pokedexId") int pokedexId) {

        SimplePokemonDto pokemon = pokemonService.getPokemonDtoByPokedexId(pokedexId);

        return ResponseEntity.ok(ApiResponse.success(pokemon));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<ApiResponse<SimplePokemonDto>> getPokemonByName(@PathVariable("name") String name) {

        SimplePokemonDto pokemon = pokemonService.getPokemonByName(name);

        return ResponseEntity.ok(ApiResponse.success(pokemon));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SimplePokemonDto>> createPokemon(@RequestBody SimplePokemonDto pokemon) {

        SimplePokemonDto newPokemon = pokemonService.createPokemon(pokemon);

        return ResponseEntity.ok(ApiResponse.success(newPokemon, "Pokemon created"));
    }

    @PutMapping("{pokedexId}")
    public ResponseEntity<ApiResponse<SimplePokemonDto>> updatePokemon(@RequestBody SimplePokemonDto changedPokemon, @PathVariable("pokedexId") int pokedexId) {

        SimplePokemonDto updatedPokemon = pokemonService.updatePokemonByPokedexId(changedPokemon, pokedexId);

        return ResponseEntity.ok(ApiResponse.success(updatedPokemon, "Pokemon updated"));
    }

    @DeleteMapping("{pokedexId}")
    public ResponseEntity<ApiResponse<SimplePokemonDto>> deletePokemon(@PathVariable("pokedexId") int pokedexId) {
        SimplePokemonDto deletedPokemon = pokemonService.deletePokemonByPokedexId(pokedexId);
        return ResponseEntity.ok(ApiResponse.success(deletedPokemon, "Pokemon deleted"));
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAllPokemon() {
        pokemonService.deleteAllPokemon();
        return ResponseEntity.ok("All Pokemons were deleted!");
    }
}
