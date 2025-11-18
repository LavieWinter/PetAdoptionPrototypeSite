package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.PetPhotoModel;
import jakarta.persistence.*;
// Entity mapping removed: table `pet_photos` will be dropped by migration V4.
// This class is kept as a POJO converter for legacy uses; it is no longer a JPA entity.
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PetPhoto {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "storage_provider", nullable = false)
    private String storageProvider; // S3 | GCS | AZURE | LOCAL | R2

    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Column(name = "public_url")
    private String publicUrl;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "width_px")
    private Integer widthPx;

    @Column(name = "height_px")
    private Integer heightPx;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "checksum_sha256")
    private byte[] checksumSha256;

    @Column(name = "is_primary")
    private Boolean isPrimary = Boolean.FALSE;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    protected PetPhoto() {}

    @PrePersist
    void onCreate() { this.createdAt = OffsetDateTime.now(); }

    // getters públicos
    public UUID getId() { return id; }
    public UUID getPetId() { return petId; }
    public String getStorageProvider() { return storageProvider; }
    public String getObjectKey() { return objectKey; }
    public String getPublicUrl() { return publicUrl; }
    public String getMimeType() { return mimeType; }
    public Integer getWidthPx() { return widthPx; }
    public Integer getHeightPx() { return heightPx; }
    public Long getSizeBytes() { return sizeBytes; }
    public byte[] getChecksumSha256() { return checksumSha256; }
    public Boolean getIsPrimary() { return isPrimary; }
    public Integer getSortOrder() { return sortOrder; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    // conversores
    public static PetPhoto fromModel(PetPhotoModel m) {
        if (m == null) return null;
        PetPhoto e = new PetPhoto();
        e.id = m.getId();
        e.petId = m.getPetId();
        e.storageProvider = m.getStorageProvider();
        e.objectKey = m.getObjectKey();
        e.publicUrl = m.getPublicUrl();
        e.mimeType = m.getMimeType();
        e.widthPx = m.getWidthPx();
        e.heightPx = m.getHeightPx();
        e.sizeBytes = m.getSizeBytes();
        e.checksumSha256 = m.getChecksumSha256();
        e.isPrimary = m.getIsPrimary();
        e.sortOrder = m.getSortOrder();
        e.createdAt = m.getCreatedAt();
        return e;
    }

    public static PetPhotoModel toModel(PetPhoto e) {
        if (e == null) return null;
        PetPhotoModel m = new PetPhotoModel();
        m.setId(e.id);
        m.setPetId(e.petId);
        m.setStorageProvider(e.storageProvider);
        m.setObjectKey(e.objectKey);
        m.setPublicUrl(e.publicUrl);
        m.setMimeType(e.mimeType);
        m.setWidthPx(e.widthPx);
        m.setHeightPx(e.heightPx);
        m.setSizeBytes(e.sizeBytes);
        m.setChecksumSha256(e.checksumSha256);
        m.setIsPrimary(e.isPrimary);
        m.setSortOrder(e.sortOrder);
        m.setCreatedAt(e.createdAt);
        return m;
    }
}