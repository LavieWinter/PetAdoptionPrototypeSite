package com.example.PetAdoption.dominio.entidades;

import com.example.PetAdoption.dominio.enums.UserRoles;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "user_admin",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_admin_email", columnNames = "email")
)
public class UserModel {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, columnDefinition = "citext")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "cep")
    private String cep;

    @Column(name = "street")
    private String street;

    @Column(name = "streetNumber")
    private String streetNumber;

    @Column(name = "uf")
    private String uf;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "org_id")
    private UUID orgId;

    @Column(name = "adoptiona_id")
    private UUID adoptionaId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<UserRoles> roles = new HashSet<>();


    public UUID getId() { return id; }
    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
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
    public UUID getOrgId() { return orgId; }
    public void setOrgId(UUID orgId) { this.orgId = orgId; }
    public UUID getAdoptionaId() { return adoptionaId; }
    public void setAdoptionaId(UUID adoptionaId) { this.adoptionaId = adoptionaId; }

    public String getPhone() { return phone; }
    public Set<UserRoles> getRoles() { return roles; }

    public void addRole(UserRoles role) {
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
    }
    public void setRoles(Set<UserRoles> roles) {
        this.roles = roles;
    }


}
