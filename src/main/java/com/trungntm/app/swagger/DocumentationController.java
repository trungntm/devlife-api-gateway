package com.trungntm.app.swagger;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class DocumentationController implements SwaggerResourcesProvider {

    private final List<String> ignoreSwagger = Arrays.asList("api-gateway");

    @Autowired
    EurekaClient eurekaClient;

    @Override
    public List<SwaggerResource> get() {

        List<String> services = eurekaClient
                .getApplications()
                .getRegisteredApplications()
                .stream()
                .map(x -> x.getName().toLowerCase())
                .toList();

        return services
                .stream()
                .filter(service -> !ignoreSwagger.contains(service))
                .map(service -> swaggerResource(service, "/" + service + "/v2/api-docs", "2.0"))
                .toList();


    }

    private SwaggerResource swaggerResource(String name, String location, String version) {

        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}

