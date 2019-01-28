package au.com.pact.demo.configuration;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Value("${info.version}")
    private String version;

    @Value("${info.app.name}")
    private String name;

    @Value("${info.app.description}")
    private String description;

    @Value("${info.contact.author}")
    private String author;

    @Value("${info.contact.email}")
    private String email;

    @Value("${info.app.protocol:https}")
    private String protocol;

    @Bean
    public Docket createDocket() {
        TypeResolver typeResolver = new TypeResolver();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(name)
                .select()
                .apis(RequestHandlerSelectors.basePackage("au.com.pact.demo.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .directModelSubstitute(Collection.class, List.class)
                .alternateTypeRules(newRule(
                        typeResolver.resolve(Collection.class, WildcardType.class),
                        typeResolver.resolve(List.class, WildcardType.class)))
                .protocols(newHashSet(protocol));
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(author, "", email);
        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .contact(contact)
                .version(version)
                .build();

    }
}