package com.example.ex_testing.controller;

import com.example.ex_testing.Config.Jwt.JwtProvider;
import com.example.ex_testing.Model.User;
import com.example.ex_testing.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByLogin(loginRequest.getUsername());
        String token = jwtProvider.generateToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", token);
        tokens.put("refreshToken", refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User registrationRequest) {
        if (userService.findByLogin(registrationRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Пользователь с таким логином уже существует");
        }

        User newUser = userService.save(registrationRequest);
        if (newUser == null) {
            return ResponseEntity.badRequest().body("Ошибка регистрации пользователя");
        }

        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (jwtProvider.validateToken(refreshToken)) {
            String username = jwtProvider.getUsernameFromToken(refreshToken);
            User user = userService.findByLogin(username);
            String newToken = jwtProvider.generateToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newToken);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body("Invalid refresh token");
    }
}
