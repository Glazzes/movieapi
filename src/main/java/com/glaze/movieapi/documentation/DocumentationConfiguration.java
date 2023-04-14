package com.glaze.movieapi.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfiguration {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI api = new OpenAPI();
        Info info = new Info();
        info.setVersion("0.0.1-SNAP_SHOT");
        info.setTitle("Movie API");
        info.setDescription("""
        A simple API where I intend to practice many "non code specific" related concepts
        """);

        api.setInfo(info);
        return api;
    }

}
