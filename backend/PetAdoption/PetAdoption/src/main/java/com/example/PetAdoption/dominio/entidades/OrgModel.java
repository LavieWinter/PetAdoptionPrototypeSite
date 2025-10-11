package com.example.PetAdoption.dominio.entidades;
import java.util.UUID;

/** Domain Model: sem JPA/Spring. */
public class OrgModel {

    private UUID id;        // org_id
    private String name;
    private String email;   // citext no banco
    private String phone;
    private UUID petOwnedId; // FK opcional -> pets.id

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UUID getPetOwnedId() { return petOwnedId; }
    public void setPetOwnedId(UUID petOwnedId) { this.petOwnedId = petOwnedId; }
}