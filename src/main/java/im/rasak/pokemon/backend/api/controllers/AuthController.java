package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.AuthResponseDto;
import im.rasak.pokemon.backend.api.dto.LoginDto;
import im.rasak.pokemon.backend.api.dto.RegisterDto;
import im.rasak.pokemon.backend.api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {

        authService.registerUser(registerDto);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        String token = authService.loginUser(loginDto);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
