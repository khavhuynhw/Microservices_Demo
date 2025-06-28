package org.kh.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
    private String secretKey;
    private Long expirationMs;
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    
    public Long getExpirationMs() {
        return expirationMs;
    }
    
    public void setExpirationMs(Long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
