package com.sb.video.streaming.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Video Streaming API")
                        .version("1.0")
                        .description("API documentation for the Video Streaming application")
                )
                .servers(
                    List.of(new Server().url("http://localhost:8080/").description("local"))
                )
                .tags(
                    List.of(
                        new Tag().name("Home APIs"),
                        new Tag().name("User APIs"),
                        new Tag().name("Video APIs")
                    )
                )
                // Add security requirement for JWT authentication
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                    new Components()
                        .addSecuritySchemes("bearerAuth",
                            new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                        )
                );
                        
    }
}
