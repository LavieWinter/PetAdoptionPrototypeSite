package com.example.PetAdoption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.storage.base-dir:uploads}")
    private String baseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(baseDir).toAbsolutePath().toString();
        String location = "file:" + absolutePath + "/";

        registry.addResourceHandler("/static/**")
                .addResourceLocations(location);
    }
}
