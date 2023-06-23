package com.tech.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Emergency Service",
                description = "This is a OpenAPI documentation of a project based on the Hospital Emergency Service " +
                        "and used for Java and Spring Boot tutorials",
                version = "1.0",
                license = @License(
                        name = "Apache License v2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                contact = @Contact(
                        name = "Adam BORN",
                        email = "adamd.born@gmail.com",
                        url = "https://github.com/adambornrepo"
                )
        ),
        servers = {
                @Server(description = "Local", url = "http://localhost:8080")
        },
        security = @SecurityRequirement(name = "Bearer")
)
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPIConfiguration {
}
