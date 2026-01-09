package dev.lobzter.fileupload.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI fileUploadOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Upload Service API")
                        .description("RESTful API for uploading and retrieving files")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tomiloba Olowo")
                                .email("olowo15tomiloba@gmail.com")));
    }
}