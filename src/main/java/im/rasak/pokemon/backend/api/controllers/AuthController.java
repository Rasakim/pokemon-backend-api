package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.AuthResponseDto;
import im.rasak.pokemon.backend.api.dto.LoginDto;
import im.rasak.pokemon.backend.api.dto.RegisterDto;
import im.rasak.pokemon.backend.api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        authService.logoutUser(token);
        return ResponseEntity.ok("User logged out successfully.");
    }
}
