package com.example.PetAdoption; // Ajuste o package se necessário

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.storage.base-dir:uploads}") // Pega do application.properties ou usa default
    private String baseDir;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 1. Pega o caminho absoluto da pasta (ex: /app/uploads)
//        Path uploadRoot = Paths.get(baseDir).toAbsolutePath().normalize();
//
//        // 2. Converte para URI (ex: file:///app/uploads)
//        String location = uploadRoot.toUri().toString();
//
//        // 3. CRUCIAL: O Spring EXIGE que termine com "/" para considerar diretório
//        if (!location.endsWith("/")) {
//            location += "/";
//        }
//
//        System.out.println(">>> DEBUG: Mapeando imagens de: " + location);
//
//        // Mapeia ambas as rotas para o mesmo local físico
//        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
//        registry.addResourceHandler("/static/**").addResourceLocations(location);
//    }

    @PostConstruct
    public void init() {
        System.out.println("=================================================");
        System.out.println("✅ WEB CONFIG CARREGADA COM SUCESSO!");
        System.out.println("=================================================");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Caminho EXATO dentro do container Docker
        // O prefixo "file:" e a barra final "/" são OBRIGATÓRIOS.
        String dockerPath = "file:/app/uploads/";

        System.out.println("=============================================");
        System.out.println("CONFIGURANDO MAPEAMENTO DE IMAGENS (DOCKER)");
        System.out.println("Rota: /uploads/** -> Pasta: " + dockerPath);
        System.out.println("=============================================");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(dockerPath);

        // Mantém compatibilidade com links antigos do banco
        registry.addResourceHandler("/static/**")
                .addResourceLocations(dockerPath);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}