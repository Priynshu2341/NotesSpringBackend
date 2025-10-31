package com.example.main.controller;

import com.example.main.dto.LoginRequestDto;
import com.example.main.dto.LoginResponseDto;
import com.example.main.security.JwtUtils;
import com.example.main.service.UserService;
import com.example.main.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {
        User user1 = userService.getUserByUsername(user.getUsername()).orElse(null);
        if (user1 != null) throw new Exception("User already exists");
        userService.register(user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(), loginRequestDto.getPassword()
                    )
            );
            User user = userService.getUserByUsername(loginRequestDto.getUsername()).orElseThrow(() -> new Exception("User not found"));
            String accessToken = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);
            user.setRefreshToken(refreshToken);
            userService.saveUser(user);
            return ResponseEntity.ok(new LoginResponseDto(accessToken,refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) throws Exception {
        try {
            var refreshToken = request.get("refreshToken");

            if (refreshToken == null) throw new Exception("Invalid refresh token");
            if (!jwtUtils.validateToken(refreshToken)) {
                throw new Exception("Invalid refresh token");
            }
            var userName = jwtUtils.extractUsername(refreshToken);

            User user = userService.getUserByUsername(userName).orElseThrow(() -> new Exception("User not found"));
            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new Exception("Invalid refresh token");
            }
            String accessToken = jwtUtils.generateToken(user);
            userService.saveUser(user);

            return ResponseEntity.ok(new LoginResponseDto(accessToken, refreshToken));
        }catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password " + e.getMessage());
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Invalid username or password");
            }

            String userName = authentication.getName();
            User user = userService.getUserByUsername(userName).orElseThrow(() -> new Exception("User not found"));
            user.setRefreshToken(null);
            userService.saveUser(user);
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }



}
