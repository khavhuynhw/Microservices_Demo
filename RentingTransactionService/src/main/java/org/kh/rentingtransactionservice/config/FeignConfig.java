package org.kh.rentingtransactionservice.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.kh.rentingtransactionservice.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            if (response.status() == 404) {
                return new ResourceNotFoundException("Resource not found in external service", "id", "unknown");
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
} 