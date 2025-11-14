package im.rasak.pokemon.backend.api.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthResponseDto {
    private final String accessToken;
    private String tokenType = "Bearer ";
}
