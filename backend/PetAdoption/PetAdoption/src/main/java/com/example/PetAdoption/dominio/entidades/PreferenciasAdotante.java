package com.example.PetAdoption.dominio.entidades;
import com.example.PetAdoption.dominio.enums.*;

public class PreferenciasAdotante {
    private EspeciePet especieDesejada;     // pode ser null (sem preferência)
    private RacaPet racaDesejada;           // pode ser null
    private PortePet porteDesejado;         // pode ser null
    private SexoPet sexoDesejado;           // pode ser null

    private AceiteNecessidades aceiteNecessidades; // obrigatório
    private AceiteCronicas aceiteCronicas;         // obrigatório

    private boolean possuiOutrosAnimais;
    private boolean temTempoParaCuidadosConstantes;

    // Getters/Setters
    public EspeciePet getEspecieDesejada() { return especieDesejada; }
    public void setEspecieDesejada(EspeciePet especieDesejada) { this.especieDesejada = especieDesejada; }
    public RacaPet getRacaDesejada() { return racaDesejada; }
    public void setRacaDesejada(RacaPet racaDesejada) { this.racaDesejada = racaDesejada; }
    public PortePet getPorteDesejado() { return porteDesejado; }
    public void setPorteDesejado(PortePet porteDesejado) { this.porteDesejado = porteDesejado; }
    public SexoPet getSexoDesejado() { return sexoDesejado; }
    public void setSexoDesejado(SexoPet sexoDesejado) { this.sexoDesejado = sexoDesejado; }
    public AceiteNecessidades getAceiteNecessidades() { return aceiteNecessidades; }
    public void setAceiteNecessidades(AceiteNecessidades aceiteNecessidades) { this.aceiteNecessidades = aceiteNecessidades; }
    public AceiteCronicas getAceiteCronicas() { return aceiteCronicas; }
    public void setAceiteCronicas(AceiteCronicas aceiteCronicas) { this.aceiteCronicas = aceiteCronicas; }
    public boolean isPossuiOutrosAnimais() { return possuiOutrosAnimais; }
    public void setPossuiOutrosAnimais(boolean possuiOutrosAnimais) { this.possuiOutrosAnimais = possuiOutrosAnimais; }
    public boolean isTemTempoParaCuidadosConstantes() { return temTempoParaCuidadosConstantes; }
    public void setTemTempoParaCuidadosConstantes(boolean temTempoParaCuidadosConstantes) { this.temTempoParaCuidadosConstantes = temTempoParaCuidadosConstantes; }
}