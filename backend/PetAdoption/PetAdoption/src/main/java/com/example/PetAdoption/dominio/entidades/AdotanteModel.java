package com.example.PetAdoption.dominio.entidades;


public class AdotanteModel {
    private Long id;
    private String nome;
    private PreferenciasAdotante preferencias;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public PreferenciasAdotante getPreferencias() { return preferencias; }
    public void setPreferencias(PreferenciasAdotante preferencias) { this.preferencias = preferencias; }
}