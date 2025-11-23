package com.example.PetAdoption.servicos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootDir;
    private final String publicBaseUrl;

    public StorageService(
            @Value("${app.storage.base-dir:uploads}") String baseDir,
            @Value("${app.storage.public-base-url:/static}") String publicBaseUrl
    ) {
        this.rootDir = Paths.get(baseDir).toAbsolutePath().normalize();
        this.publicBaseUrl = publicBaseUrl;

        try {
            // cria a pasta raiz + subpasta "pets"
            Files.createDirectories(this.rootDir.resolve("pets"));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar diretório de uploads em " + this.rootDir, e);
        }
    }

    public String storePetImage(UUID petId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem não pode ser vazio");
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null) {
            int dot = originalName.lastIndexOf('.');
            if (dot != -1) {
                ext = originalName.substring(dot); // inclui o ponto
            }
        }

        // valida extensões mais comuns (ajusta se quiser mais tipos)
        if (!ext.toLowerCase().matches("\\.(png|jpg|jpeg|gif|webp)$")) {
            throw new IllegalArgumentException("Tipo de arquivo não suportado: " + ext);
        }

        Path destination = rootDir
                .resolve("pets")
                .resolve(petId.toString() + ext)
                .normalize();

        try {
            Files.createDirectories(destination.getParent());
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem em " + destination, e);
        }

        // isto é o que vai para o campo petImage no banco
        // com o teu docker-compose: "/static/pets/<id>.jpg"
        return publicBaseUrl + "/pets/" + petId + ext;
    }
}
