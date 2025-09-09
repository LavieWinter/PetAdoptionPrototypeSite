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

    // getters/setters omitidos por brevidade …

    public UUID getId() { return id; }
    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }
    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setPhone(String phone) { this.phone = phone; }
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
