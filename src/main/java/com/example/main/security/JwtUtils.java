package com.example.main.security;

import com.example.main.model.RoleType;
import com.example.main.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final String JWT_SECRET = "jwtSecret12312435212312asdasdsaffdgfdger12e12123e123";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7;

    private final Key signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());


    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles().stream()
                        .map(RoleType::name)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(signingKey,SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public Boolean validateToken(String token) throws AuthenticationException {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
