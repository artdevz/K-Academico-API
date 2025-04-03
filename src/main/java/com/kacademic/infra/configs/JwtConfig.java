package com.kacademic.infra.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtConfig {
    
    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    public static final long EXPIRATION_TIME_MS = 3600000L;

    @Value("${jwt.secret:DEFAULT_SECRET_KEY_WITH_32_BYTES}")
    private String secretKey;

    @PostConstruct
    private void validateSecretKey() {
        if ("DEFAULT_SECRET_KEY_WITH_32_BYTES".equals(secretKey)) log.warn("The JWT Secret Key is using the Default Value");
    }

    public String getSecretKey() {
        return secretKey;
    }

}