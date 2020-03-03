package com.fzm.common.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

	@Value("${swagger.is.enable}")
	private boolean swagger_is_enable;

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.enable(swagger_is_enable)
                .apiInfo(new ApiInfoBuilder()
                        .title("模板接口文档")
                        .description("模板接口文档")
                        .contact(new Contact("A", null, null))
                        .version("版本号:0.1")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fzm.teamplate"))
                .paths(PathSelectors.any())
                .build();
    }
}
