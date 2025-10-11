package com.example.PetAdoption.dominio.entidades;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PetPhotoModel {

    private UUID id;
    private UUID petId;

    private String storageProvider; // S3 | GCS | AZURE | LOCAL | R2
    private String objectKey;
    private String publicUrl;
    private String mimeType;

    private Integer widthPx;
    private Integer heightPx;
    private Long sizeBytes;
    private byte[] checksumSha256;

    private Boolean isPrimary = Boolean.FALSE;
    private Integer sortOrder;

    private OffsetDateTime createdAt;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
    public String getStorageProvider() { return storageProvider; }
    public void setStorageProvider(String storageProvider) { this.storageProvider = storageProvider; }
    public String getObjectKey() { return objectKey; }
    public void setObjectKey(String objectKey) { this.objectKey = objectKey; }
    public String getPublicUrl() { return publicUrl; }
    public void setPublicUrl(String publicUrl) { this.publicUrl = publicUrl; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Integer getWidthPx() { return widthPx; }
    public void setWidthPx(Integer widthPx) { this.widthPx = widthPx; }
    public Integer getHeightPx() { return heightPx; }
    public void setHeightPx(Integer heightPx) { this.heightPx = heightPx; }
    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    public byte[] getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(byte[] checksumSha256) { this.checksumSha256 = checksumSha256; }
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean primary) { isPrimary = primary; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}