package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.PokemonEntityDTO;
import im.rasak.pokemon.backend.api.dto.PokemonPageResponseDTO;
import im.rasak.pokemon.backend.api.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public ResponseEntity<PokemonPageResponseDTO> getAllPokemon(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(pokemonService.getAllPokemons(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("{pokedexId}")
    public ResponseEntity<PokemonEntityDTO> getPokemonByPokedexId(@PathVariable("pokedexId") int pokedexId) {
        return new ResponseEntity<>(pokemonService.getPokemonByPokedexId(pokedexId), HttpStatus.OK);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<PokemonEntityDTO> getPokemonByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(pokemonService.getPokemonByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PokemonEntityDTO> createPokemon(@RequestBody PokemonEntityDTO pokemon) {
        return new ResponseEntity<>(pokemonService.createPokemon(pokemon), HttpStatus.CREATED);
    }

    @PutMapping("{pokedexId}")
    public ResponseEntity<PokemonEntityDTO> updatePokemon(@RequestBody PokemonEntityDTO pokemonEntityDTO, @PathVariable("pokedexId") int pokedexId) {
        return new ResponseEntity<>(pokemonService.updatePokemonByPokedexId(pokemonEntityDTO, pokedexId), HttpStatus.OK);
    }

    @DeleteMapping("{pokedexId}")
    public ResponseEntity<String> deletePokemon(@PathVariable("pokedexId") int pokedexId) {
        pokemonService.deletePokemonByPokedexId(pokedexId);
        return ResponseEntity.ok("Pokemon with id: {" + pokedexId + "} deleted successfully!");
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAllPokemon() {
        pokemonService.deleteAllPokemon();
        return ResponseEntity.ok("All Pokemons were deleted!");
    }
}
