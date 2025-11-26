package com.example.PetAdoption;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@SpringBootApplication
public class PetAdoptionApplication implements WebMvcConfigurer { // <--- Implementa a interface aqui

    public static void main(String[] args) {
        SpringApplication.run(PetAdoptionApplication.class, args);
    }

    // --- CONFIGURAÇÃO DE IMAGENS (Forçada na Main) ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Caminho absoluto dentro do container
        String dockerPath = "file:///app/uploads/";

        System.out.println("=========================================");
        System.out.println("🚀 CONFIGURANDO ROTAS DE IMAGENS NA MAIN");
        System.out.println("📂 Mapeando para: " + dockerPath);
        System.out.println("=========================================");

        // Mapeia http://localhost:8080/uploads/...
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(dockerPath);

        // Mapeia http://localhost:8080/static/... (compatibilidade)
        registry.addResourceHandler("/static/**")
                .addResourceLocations(dockerPath);
    }
    // --------------------------------------------------

    // Debug para confirmar que o arquivo existe
    @Bean
    CommandLineRunner debugFileAccess() {
        return args -> {
            File folder = new File("/app/uploads/pets");
            if (folder.exists()) {
                System.out.println("✅ DEBUG: Pasta /app/uploads/pets encontrada. Arquivos: " + folder.list().length);
            } else {
                System.out.println("❌ DEBUG: Pasta /app/uploads/pets NÃO ENCONTRADA.");
            }
        };
    }
}