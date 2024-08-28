package com.vermau2k01.notes_application.security.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpiration;

    // todo extract token from auth header
    public String getJwtHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.debug("authHeader: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // todo create a token with the help of username
    public String generateToken(UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // todo Get username from token
    public String getUserNameFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // todo Validate Jwt token
    public boolean validateToken(String token) {
        try{
            Jwts
                    .parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch(MalformedJwtException e)
        {
         logger.error("Invalid JWT token: {}", e.getMessage());
        }
        catch(ExpiredJwtException e)
        {
            logger.error("Expired JWT token: {}", e.getMessage());
        }
        catch(UnsupportedJwtException e)
        {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch(IllegalArgumentException e)
        {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        catch(Exception e)
        {
            logger.error("Unexpected error: {}", e.getMessage());
        }

        return false;
    }

}
