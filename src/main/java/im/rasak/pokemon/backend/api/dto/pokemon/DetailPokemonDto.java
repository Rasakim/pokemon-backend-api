package im.rasak.pokemon.backend.api.dto.pokemon;

import im.rasak.pokemon.backend.api.dto.review.ReviewDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class DetailPokemonDto {

    private final SimplePokemonDto simplePokemonDto;
    private final Set<ReviewDto> reviews;

}
