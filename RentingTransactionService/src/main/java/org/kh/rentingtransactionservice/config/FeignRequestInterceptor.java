package org.kh.rentingtransactionservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);
    
    @Value("${application.jwt.secret-key:zYxfmMUEe8h1N9y6cYbAa6GvLjExF4z/YM+R9U56GNqslZfMGUINub49KZ1mD6CUKntDaKlZsKU9KZIgwl9qTw==}")
    private String jwtSecret;

    @Override
    public void apply(RequestTemplate template) {
        try {
            // Get the current request attributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String authHeader = attributes.getRequest().getHeader("Authorization");
                
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.replace("Bearer ", "");
                    
                    // Parse JWT token to extract user information
                    Claims claims = parseJwtToken(token);
                    
                    if (claims != null) {
                        // Extract user information from JWT claims
                        String userEmail = claims.getSubject(); // username/email
                        String userId = claims.getId(); // jti claim contains user ID
                        @SuppressWarnings("unchecked")
                        List<String> roles = claims.get("roles", List.class);
                        
                        // Add headers expected by downstream services
                        if (userEmail != null) {
                            template.header("X-User-Email", userEmail);
                        }
                        
                        if (userId != null) {
                            template.header("X-User-Id", userId);
                        }
                        
                        if (roles != null && !roles.isEmpty()) {
                            // Remove ROLE_ prefix if present and convert to simple role names
                            String rolesString = roles.stream()
                                .map(role -> role.replace("ROLE_", ""))
                                .reduce((a, b) -> a + "," + b)
                                .orElse("");
                            template.header("X-User-Role", rolesString);
                        }
                        
                        logger.debug("Added user headers - Email: {}, UserId: {}, Roles: {}", 
                                   userEmail, userId, roles);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error processing JWT token for Feign request", e);
        }
    }
    
    private Claims parseJwtToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Failed to parse JWT token", e);
            return null;
        }
    }
} 