package im.rasak.pokemon.backend.api.dto.pokemon;

import im.rasak.pokemon.backend.api.enums.PokemonType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class SimplePokemonDto {

    private final Integer pokedexId;
    private final String name;
    private final Integer generation;
    private final Set<PokemonType> types;
    private final String description;
    private final String imageUrl;
    private final Double rating;

}
