package nl.ing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("nl.ing"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo())
                .useDefaultResponseMessages(false);
    }
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Authentication API")
                .description("An API for creating and authenticating users")
                .contact(new Contact("Stephen Eaton", "", "steaton@gmail.com"))
                .build();
    }
}
