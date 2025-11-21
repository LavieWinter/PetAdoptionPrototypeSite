package com.example.PetAdoption.servicos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    // pasta base no servidor (raiz do projeto por padrão)
    @Value("${app.storage.base-dir:uploads}")
    private String baseDir;

    // prefixo público usado na URL que vai pra pets.pet_image
    @Value("${app.storage.public-base-url:/static}")
    private String publicBaseUrl;

    @Override
    public String storePetImage(UUID petId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem obrigatório");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo de arquivo inválido: " + contentType);
        }

        try {
            String originalName = file.getOriginalFilename();
            String ext = "bin";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf('.') + 1);
            }

            String filename = UUID.randomUUID() + "." + ext;
            String relativePath = "pets/" + petId + "/" + filename;

            Path root = Paths.get(baseDir).toAbsolutePath().normalize();
            Path dest = root.resolve(relativePath).normalize();

            Files.createDirectories(dest.getParent());
            Files.write(dest, file.getBytes());

            // isso aqui é o que vai parar em pets.pet_image
            String urlPath = publicBaseUrl + "/" + relativePath.replace("\\", "/");
            return urlPath;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem do pet", e);
        }
    }
}