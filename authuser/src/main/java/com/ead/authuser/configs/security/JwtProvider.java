package com.ead.authuser.configs.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//import io.vavr.control.Either;



import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    Logger logger = LogManager.getLogger(JwtProvider.class);

    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${ead.auth.jwtExpirationMs}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsernameJwt(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;

        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        }catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

/*    public Either<String, Claims> validateJwtToken(String authToken) {

        try {

            Claims claims =
                    Jwts.parser()
                            .verifyWith(getSecretKey())
                            .build()
                            .parseSignedClaims(authToken)
                            .getPayload();

            return Either.right(claims);

        } catch (SecurityException e) {

            logger.error("Invalid JWT signature", e);
                return Either.left("INVALID_SIGNATURE");

        } catch (MalformedJwtException e) {

            logger.error("Invalid JWT token", e);
            return Either.left("MALFORMED_TOKEN");

        } catch (ExpiredJwtException e) {

            logger.error("Expired JWT token", e);
            return Either.left("TOKEN_EXPIRED");

        } catch (UnsupportedJwtException e) {

            logger.error("Unsupported JWT token", e);
            return Either.left("UNSUPPORTED_TOKEN");

        } catch (IllegalArgumentException e) {

            logger.error("JWT claims string is empty", e);
            return Either.left("EMPTY_TOKEN");
        }
    }*/
}
