package com.devsouzx.auth.controllers;

import com.devsouzx.auth.domain.user.AuthenticationDTO;
import com.devsouzx.auth.domain.user.LoginResponseDTO;
import com.devsouzx.auth.domain.user.RegisterDTO;
import com.devsouzx.auth.domain.user.User;
import com.devsouzx.auth.infra.security.TokenService;
import com.devsouzx.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        if (this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User user = new User(data.login(), encryptedPassword, data.role());

        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
