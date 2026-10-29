package com.innovacorp.reliant.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Value("${documentation.application.description}")
    private String applicationDescription;

    @Value("${documentation.application.version}")
    private String applicationVersion;

    @Bean
    public OpenAPI reliantPlatformOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reliant Platform API")
                        .description(applicationDescription)
                        .version(applicationVersion)
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Reliant Platform wiki")
                        .url("https://github.com/upc-pre-202620-1asi0729-7753-innovacorp/reliant-platform"));
    }
}