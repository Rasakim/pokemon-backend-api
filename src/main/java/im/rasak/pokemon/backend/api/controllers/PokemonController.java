package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.PokemonEntityDTO;
import im.rasak.pokemon.backend.api.dto.PokemonPageResponseDTO;
import im.rasak.pokemon.backend.api.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("")
    public ResponseEntity<PokemonPageResponseDTO> getAllPokemon(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(pokemonService.getAllPokemons(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<PokemonEntityDTO> getPokemonById(@PathVariable("id") int id) {
        return new ResponseEntity<>(pokemonService.getPokemonById(id), HttpStatus.OK);
    }

    @GetMapping("pokedex/{pokedexId}")
    public ResponseEntity<PokemonEntityDTO> getPokemmonByPokedexId(@PathVariable("pokedexId") int pokedexId) {
        return new ResponseEntity<>(pokemonService.getPokemonByPokedexId(pokedexId), HttpStatus.OK);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<PokemonEntityDTO> getPokemonByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(pokemonService.getPokemonByName(name), HttpStatus.OK);
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PokemonEntityDTO> createPokemon(@RequestBody PokemonEntityDTO pokemon) {
        return new ResponseEntity<>(pokemonService.createPokemon(pokemon), HttpStatus.CREATED);
    }

    @PutMapping("id/{id}/update")
    public ResponseEntity<PokemonEntityDTO> updatePokemon(@RequestBody PokemonEntityDTO pokemonEntityDTO, @PathVariable("id") int id) {
        return new ResponseEntity<>(pokemonService.updatePokemonById(pokemonEntityDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("id/{id}/delete")
    public ResponseEntity<String> deletePokemon(@PathVariable("id") int id) {
        pokemonService.deletePokemonById(id);
        return ResponseEntity.ok("Pokemon with id: {" + id + "} deleted successfully!");
    }

    @DeleteMapping("deleteAllPokemon")
    public ResponseEntity<String> deleteAllPokemon() {
        pokemonService.deleteAllPokemon();
        return ResponseEntity.ok("All Pokemons were wiped!");
    }
}
