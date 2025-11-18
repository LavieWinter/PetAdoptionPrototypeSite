package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.PetPhotoInlineModel;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
// Entity mapping removed: table `pet_photos_inline` will be dropped by migration V4.
// This class is kept as a POJO converter for legacy uses; it is no longer a JPA entity.

import java.time.OffsetDateTime;
import java.util.UUID;

public class PetPhotoInline {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    @Column(name = "is_primary")
    private Boolean isPrimary = Boolean.FALSE;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    protected PetPhotoInline() {}

    @PrePersist
    void onCreate() { this.createdAt = OffsetDateTime.now(); }

    // getters públicos
    public UUID getId() { return id; }
    public UUID getPetId() { return petId; }
    public String getMimeType() { return mimeType; }
    public Long getSizeBytes() { return sizeBytes; }
    public byte[] getImageData() { return imageData; }
    public Boolean getIsPrimary() { return isPrimary; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    // conversores
    public static PetPhotoInline fromModel(PetPhotoInlineModel m) {
        if (m == null) return null;
        PetPhotoInline e = new PetPhotoInline();
        e.id = m.getId();
        e.petId = m.getPetId();
        e.mimeType = m.getMimeType();
        e.sizeBytes = m.getSizeBytes();
        e.imageData = m.getImageData();
        e.isPrimary = m.getIsPrimary();
        e.createdAt = m.getCreatedAt();
        return e;
    }

    public static PetPhotoInlineModel toModel(PetPhotoInline e) {
        if (e == null) return null;
        PetPhotoInlineModel m = new PetPhotoInlineModel();
        m.setId(e.id);
        m.setPetId(e.petId);
        m.setMimeType(e.mimeType);
        m.setSizeBytes(e.sizeBytes);
        m.setImageData(e.imageData);
        m.setIsPrimary(e.isPrimary);
        m.setCreatedAt(e.createdAt);
        return m;
    }
}
