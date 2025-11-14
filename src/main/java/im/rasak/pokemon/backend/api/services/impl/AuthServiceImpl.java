package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.dto.auth.LoginDto;
import im.rasak.pokemon.backend.api.dto.auth.RegisterDto;
import im.rasak.pokemon.backend.api.exceptions.AuthenticationLoginException;
import im.rasak.pokemon.backend.api.exceptions.EmailAlreadyTakenException;
import im.rasak.pokemon.backend.api.exceptions.UsernameAlreadyTakenException;
import im.rasak.pokemon.backend.api.models.Role;
import im.rasak.pokemon.backend.api.models.UserEntity;
import im.rasak.pokemon.backend.api.repository.RoleRepository;
import im.rasak.pokemon.backend.api.repository.UserRepository;
import im.rasak.pokemon.backend.api.security.JwtUtil;
import im.rasak.pokemon.backend.api.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenServiceImpl tokenService;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            log.warn("Registration failed: username '{}' is already taken", registerDto.getUsername());
            throw new UsernameAlreadyTakenException("Username '" + registerDto.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            log.warn("Registration failed: email '{}' is already taken", registerDto.getEmail());
            throw new EmailAlreadyTakenException("Email '" + registerDto.getEmail() + "' is already registered");
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
    }

    @Override
    public String loginUser(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authentication);

            log.info("User '{}' logged in successfully", loginDto.getUsername());

            return token;
        } catch (AuthenticationException ex) {
            log.warn("Failed login attempt for username '{}': {}", loginDto.getUsername(), ex.getMessage());
            throw new AuthenticationLoginException(ex.getMessage(), Map.of("username", loginDto.getUsername()));
        }
    }

    @Override
    public void logoutUser(String token) {
        try {
            tokenService.blacklistToken(token);
            log.info("User '{}' logged out successfully", jwtUtil.getUsernameFromJWT(token));
        } catch (Exception ex) {
            log.warn("Failed logout attempt for username '{}': {}", jwtUtil.getUsernameFromJWT(token), ex.getMessage());
        }
    }
}
