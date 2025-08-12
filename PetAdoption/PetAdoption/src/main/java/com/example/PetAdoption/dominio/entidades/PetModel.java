package com.example.PetAdoption.dominio.entidades;

import com.example.PetAdoption.dominio.enums.*;

public class PetModel {
    private Long id;
    private String nome;

    private EspeciePet especie; // cachorro, gato, ...
    private PortePet porte;
    private RacaPet raca; // SRD, outros ...
    private SexoPet sexo; //M ou F
    private SocialPet social; //nao usado
    private StatusPet status;
    // Categoria saude
    private SaudePet necessidadeOuTratamento;
    private SaudePet cronicasOuIncuraveis;

    // Categoria “Social/Tempo” (Essas 3 sao indepen)
    private boolean sociavel; // +5 se adotante tem outros animais
    private boolean naoSociavel; // IMPEDITIVO se adotante tem outros animais
    private boolean exigeCuidadosConstantes; // +5 se adotante tem tempo; senão IMPEDITIVO

    public PetModel() {
    }

   public PetModel(Long id,
                    String nome,
                    EspeciePet especie,
                    PortePet porte,
                    RacaPet raca,
                    SexoPet sexo,
                    SocialPet social,
                    StatusPet status,
                    SaudePet semNecessidadeOuTratamento,
                    SaudePet semDoencas,
                    boolean sociavel,
                    boolean naoSociavel,
                    boolean exigeCuidadosConstantes) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.porte = porte;
        this.raca = raca;
        this.sexo = sexo;
        this.social = social;
        this.status = status;
        this.necessidadeOuTratamento = semNecessidadeOuTratamento;
        this.cronicasOuIncuraveis = semDoencas;
        this.sociavel = sociavel;
        this.naoSociavel = naoSociavel;
        this.exigeCuidadosConstantes = exigeCuidadosConstantes;
        validarInvariantes();
    }

    
    public void validarInvariantes() {
        // Social: não pode ser sociável e não sociável ao mesmo tempo
        if (sociavel && naoSociavel) {
            throw new IllegalStateException("Pet não pode ser sociável e não sociável simultaneamente.");
        }
        // Saúde (eixo 1): apenas os dois valores válidos
        if (necessidadeOuTratamento != SaudePet.comNecessidadesOuTratamentoContinuo
                && necessidadeOuTratamento != SaudePet.semNecessidadesSemTratamento) {
            throw new IllegalStateException(
                "semNecessidadeOuTratamento deve ser comNecessidadesOuTratamentoContinuo OU semNecessidadesSemTratamento.");
        }
        // Saúde (eixo 2): apenas os dois valores válidos
        if (cronicasOuIncuraveis != SaudePet.comDoencas
                && cronicasOuIncuraveis != SaudePet.semDoencas) {
            throw new IllegalStateException(
                "semDoencas deve ser comDoencas OU semDoencas.");
        }
    }

    // ——  usados pelo serviço de compatibilidade —— //
    public boolean temNecessidadesOuTratamento() {
        return necessidadeOuTratamento == SaudePet.comNecessidadesOuTratamentoContinuo;
    }
    public boolean semNecessidadesSemTratamento() {
        return necessidadeOuTratamento == SaudePet.semNecessidadesSemTratamento;
    }
    public boolean temDoencas() {
        return cronicasOuIncuraveis == SaudePet.comDoencas;
    }
    public boolean isSemDoencas() {
        return cronicasOuIncuraveis == SaudePet.semDoencas;
    }

    // —— Getters/Setters —— //
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public EspeciePet getEspecie() { return especie; }
    public void setEspecie(EspeciePet especie) { this.especie = especie; }

    public PortePet getPorte() { return porte; }
    public void setPorte(PortePet porte) { this.porte = porte; }

    public RacaPet getRaca() { return raca; }
    public void setRaca(RacaPet raca) { this.raca = raca; }

    public SexoPet getSexo() { return sexo; }
    public void setSexo(SexoPet sexo) { this.sexo = sexo; }

    public SocialPet getSocial() { return social; }
    public void setSocial(SocialPet social) { this.social = social; }

    public StatusPet getStatus() { return status; }
    public void setStatus(StatusPet status) { this.status = status; }

    public SaudePet getNecessidadeOuTratamento() { return necessidadeOuTratamento; }
    public void setNecessidadeOuTratamento(SaudePet v) { this.necessidadeOuTratamento = v; }

    public SaudePet getCronicasOuIncuraveis() { return cronicasOuIncuraveis; }
    public void setCronicasOuIncuraveis(SaudePet v) { this.cronicasOuIncuraveis = v; }

    public boolean isSociavel() { return sociavel; }
    public void setSociavel(boolean sociavel) { this.sociavel = sociavel; }

    public boolean isNaoSociavel() { return naoSociavel; }
    public void setNaoSociavel(boolean naoSociavel) { this.naoSociavel = naoSociavel; }

    public boolean isExigeCuidadosConstantes() { return exigeCuidadosConstantes; }
    public void setExigeCuidadosConstantes(boolean exigeCuidadosConstantes) { this.exigeCuidadosConstantes = exigeCuidadosConstantes; }
}