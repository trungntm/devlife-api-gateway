package com.trungntm.config.swagger;

import com.trungntm.utils.YamlPropertySourceFactory;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Configuration
@EnableSwagger2
@PropertySource(value = "classpath:swagger.yml", factory = YamlPropertySourceFactory.class)
public class SwaggerConfig {

    @Value("${app.swagger.app-info.title}")
    private String title;

    @Value("${app.swagger.app-info.description}")
    private String description;

    @Value("${app.swagger.app-info.version}")
    private String version;

    @Value("${app.swagger.app-info.term-of-services}")
    private String termOfServices;

    @Value("${app.swagger.app-info.license}")
    private String license;

    @Value("${app.swagger.app-info.license-url}")
    private String licenseUrl;

    @Value("${app.swagger.app-info.contact.name}")
    private String contactName;

    @Value("${app.swagger.app-info.contact.url}")
    private String contactUrl;

    @Value("${app.swagger.app-info.contact.email}")
    private String contactEmail;

    @Bean
    public Docket appApi(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(ZonedDateTime.class, Date.class)
                .directModelSubstitute(LocalDateTime.class, Date.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)
                        )
                )
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(
                        new ApiKey("JWT", "Authorization", "header"))
                )
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "Access everything");

        return Collections.singletonList(new SecurityReference("JWT",
                new AuthorizationScope[]{authorizationScope}));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(title,
                description,
                version,
                termOfServices,
                new Contact(contactName, contactUrl, contactEmail),
                license,
                licenseUrl,
                Collections.emptyList());
    }
}
