package org.kh.apigateway.config;

import org.kh.apigateway.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Authentication Service Routes (no JWT filter)
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .uri("lb://authentication-service"))
                
                // Customer Service Routes (with JWT filter)
                .route("customer-service", r -> r
                        .path("/api/v1/customers/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://customer-management-service"))
                
                // Car Management Service Routes (with JWT filter)
                .route("car-service", r -> r
                        .path("/api/v1/cars/**", "/api/v1/manufacturers/**", "/api/v1/suppliers/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://car-management-service"))
                
                // Renting Transaction Service Routes (with JWT filter)
                .route("renting-transaction-service", r -> r
                        .path("/api/v1/rentals/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://renting-transaction-service"))
                
                .build();
    }
} 