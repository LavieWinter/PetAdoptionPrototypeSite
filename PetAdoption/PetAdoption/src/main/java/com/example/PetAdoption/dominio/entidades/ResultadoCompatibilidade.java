package com.example.PetAdoption.dominio.entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoCompatibilidade {
    private int score;
    private boolean impeditivo;
    private final List<String> motivos = new ArrayList<>();

    public void addPontos(int pts, String motivo) {
        this.score += pts;
        this.motivos.add(motivo + " (+" + pts + ")");
    }
    public void addPenalidade(int pts, String motivo) {
        this.score -= pts;
        this.motivos.add(motivo + " (-" + pts + ")");
    }
    public void marcarImpedimento(String motivo) {
        this.impeditivo = true;
        this.motivos.add("IMPEDITIVO: " + motivo);
    }

    public int getScore() { return score; }
    public boolean isImpeditivo() { return impeditivo; }
    public List<String> getMotivos() { return Collections.unmodifiableList(motivos); }
}