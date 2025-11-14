package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.exceptions.UserNotFoundException;
import im.rasak.pokemon.backend.api.models.UserEntity;
import im.rasak.pokemon.backend.api.repository.UserRepository;
import im.rasak.pokemon.backend.api.security.JwtUtil;
import im.rasak.pokemon.backend.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserEntity getUserEntityFromToken(String jwtToken) {
        String username = jwtUtil.getUsernameFromJWT(jwtToken);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with name: " + username + " not found"));

        return user;
    }
}
