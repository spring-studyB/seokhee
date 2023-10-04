package oauthlearn.hello.global.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1-definition")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI oauthlearnOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("oauth2 learn API")
                        .description("잇타 4기 spring study B API 명세서")
                        .version("v0.0.1"));
    }
}
