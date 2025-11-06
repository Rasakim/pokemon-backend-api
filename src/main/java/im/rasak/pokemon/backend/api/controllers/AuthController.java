package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.AuthResponseDto;
import im.rasak.pokemon.backend.api.dto.LoginDto;
import im.rasak.pokemon.backend.api.dto.RegisterDto;
import im.rasak.pokemon.backend.api.models.Role;
import im.rasak.pokemon.backend.api.models.UserEntity;
import im.rasak.pokemon.backend.api.repository.RoleRepository;
import im.rasak.pokemon.backend.api.repository.UserRepository;
import im.rasak.pokemon.backend.api.security.JwtHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())) {
            log.warn("Registration failed: username '{}' is already taken", registerDto.getUsername());
            return new ResponseEntity<>("Registration failed: username '" + registerDto.getUsername() + "' is already taken", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(registerDto.getEmail())) {
            log.warn("Registration failed: email '{}' is already taken", registerDto.getEmail());
            return new ResponseEntity<>("Registration failed: email '" + registerDto.getEmail() + "' is registered", HttpStatus.BAD_REQUEST);
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setEmail(registerDto.getEmail());

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Default Role 'USER' not found")));
        newUser.setRoles(roles);

        userRepository.save(newUser);

        log.info("New user registration completed: username='{}', email='{}'", newUser.getUsername(), newUser.getEmail());

        return new ResponseEntity<>("User registered successfully.", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtHelper.generateToken(authentication);

            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
        } catch (AuthenticationException ex) {
            log.warn("Failed login attempt for username '{}': {}", loginDto.getUsername(), ex.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
