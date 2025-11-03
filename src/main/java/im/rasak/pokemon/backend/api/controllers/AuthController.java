package im.rasak.pokemon.backend.api.controllers;

import im.rasak.pokemon.backend.api.dto.RegisterDto;
import im.rasak.pokemon.backend.api.models.Role;
import im.rasak.pokemon.backend.api.models.UserEntity;
import im.rasak.pokemon.backend.api.repository.RoleRepository;
import im.rasak.pokemon.backend.api.repository.UserRepository;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken.", HttpStatus.BAD_REQUEST);
        }

        UserEntity newUser = new UserEntity(registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();

        if (registerDto.getUsername().equals("Rasakim")) {
            roles.add(roleRepository.findByName("ADMIN").get());
        }

        roles.add(roleRepository.findByName("USER").get());

        newUser.setRoles(roles);

        userRepository.save(newUser);

        return new ResponseEntity<>("User registered successfully.", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody RegisterDto loginDto) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(loginDto.getUsername());

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>("Wrong username or password.", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userOpt.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Wrong username or password.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User logged in successfully.", HttpStatus.OK);
    }
}
