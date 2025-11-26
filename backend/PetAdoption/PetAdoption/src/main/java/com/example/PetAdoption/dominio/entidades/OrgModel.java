package com.example.PetAdoption.dominio.entidades;
import jakarta.persistence.Column;

import java.util.UUID;

/** Domain Model: sem JPA/Spring. */
public class OrgModel {

    private UUID id;        // org_id
    private String name;
    private String email;   // citext no banco
    private String phone;
    private UUID petOwnedId; // FK opcional -> pets.id
    private String cep;
    private String street;
    private String streetNumber;
    private String uf;
    private String neighborhood;
    private String city;


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

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getStreetNumber() { return streetNumber; }
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}