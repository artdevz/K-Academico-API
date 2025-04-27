package com.kacademico.infra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    
    private static final String SECURITY_SCHEME_NAME = "JWT";
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String API_TITLE = "K-Academico API";
    private static final String API_VERSION = "BETA 3.0";
    private static final String API_DESCRPTION = "K-Academico API Documentation";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRPTION)
            )
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, createJwtSecurityScheme())
        );
    }

    private SecurityScheme createJwtSecurityScheme() {
        return new SecurityScheme()
            .name(AUTH_HEADER_NAME)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }

}