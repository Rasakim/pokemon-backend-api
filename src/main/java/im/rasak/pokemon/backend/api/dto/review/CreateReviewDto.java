package im.rasak.pokemon.backend.api.dto.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateReviewDto {
    private final String content;
    private final Double rating;
}
