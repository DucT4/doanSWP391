package com.swp391.Warranty_EV.service;

import com.swp391.Warranty_EV.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    // 64-byte base64 string (nên để trong application.properties hoặc env)
    private static final String SECRET_B64 = "NGJiNmQxZGZiYWZiNjRhNjgxMTM5ZDE1ODZiNmYxMTYwZDE4MTU5YWZkNTdjOGM3OTEzNmQ3NDkwNjMwNDA3Yw==";
    private static final long EXP_MS = 24 * 60 * 60 * 1000L; // 24h

    private SecretKey key() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_B64);
        return Keys.hmacShaKeyFor(bytes);
    }

    // ====== Generate ======
    public String generateToken(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXP_MS);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", "ROLE_" + user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key())
                .compact();
    }

    // ====== Extract ======
    public String extractUsername(String token) {
        return extractAll(token).getSubject();
    }

    public boolean isExpired(String token) {
        return extractAll(token).getExpiration().before(new Date());
    }

    private Claims extractAll(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ====== Validate ======
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
