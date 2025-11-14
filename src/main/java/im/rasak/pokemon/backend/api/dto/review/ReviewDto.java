package im.rasak.pokemon.backend.api.dto.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReviewDto {
    private final Integer reviewId;
    private final Integer pokedexId;
    private final UUID authorId;
    private final String authorName;
    private final Date createdAt;
    private final String content;
    private final Double rating;
}
