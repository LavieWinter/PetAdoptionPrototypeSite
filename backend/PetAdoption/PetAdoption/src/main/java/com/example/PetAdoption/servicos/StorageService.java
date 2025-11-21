package com.example.PetAdoption.servicos;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Serviço de storage para imagens de pet.
 * Recebe o arquivo e devolve a string que vai ser gravada em pets.pet_image.
 */
public interface StorageService {

    /**
     * Salva a imagem de um pet e retorna a referência (ex: /static/pets/{id}/arquivo.jpg).
     */
    String storePetImage(UUID petId, MultipartFile file);
}
