package com.example.PetAdoption.dominio.servicos.impl;


import com.example.PetAdoption.dominio.entidades.AdotanteModel;
import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.entidades.PreferenciasAdotante;
import com.example.PetAdoption.dominio.entidades.ResultadoCompatibilidade;
import com.example.PetAdoption.dominio.enums.AceiteCronicas;
import com.example.PetAdoption.dominio.enums.AceiteNecessidades;
import com.example.PetAdoption.dominio.enums.SaudePet;
import com.example.PetAdoption.dominio.servicos.CompatibilidadeService;

public class CompatibilidadeTabelaService implements CompatibilidadeService {

    @Override
    public ResultadoCompatibilidade avaliar(AdotanteModel adotante, PetModel pet) {
        if (adotante == null || adotante.getPreferencias() == null || pet == null) {
            throw new IllegalArgumentException("Adotante, preferências e pet são obrigatórios.");
        }

        PreferenciasAdotante pref = adotante.getPreferencias();
        ResultadoCompatibilidade res = new ResultadoCompatibilidade();

        // ===== ESPÉCIE =====
        if (pref.getEspecieDesejada() != null) {
            if (pref.getEspecieDesejada() == pet.getEspecie())
                res.addPontos(20, "Espécie igual à desejada");
            else
                res.addPenalidade(20, "Espécie diferente da desejada");
        }

        // ===== RAÇA =====
        if (pref.getRacaDesejada() != null) {
            if (pref.getRacaDesejada() == pet.getRaca())
                res.addPontos(10, "Raça igual à desejada");
            else
                res.addPenalidade(10, "Raça diferente da desejada");
        }

        // ===== PORTE =====
        if (pref.getPorteDesejado() != null) {
            if (pref.getPorteDesejado() == pet.getPorte())
                res.addPontos(10, "Porte igual ao desejado");
            else
                res.addPenalidade(10, "Porte diferente do desejado");
        }

        // ===== SEXO =====
        if (pref.getSexoDesejado() != null) {
            if (pref.getSexoDesejado() == pet.getSexo())
                res.addPontos(5, "Sexo igual ao desejado");
            else
                res.addPenalidade(5, "Sexo diferente do desejado");
        }

        // ===== SAÚDE — necessidades / tratamento =====
        SaudePet n = pet.getNecessidadeOuTratamento(); // comNecessidadesOuTratamentoContinuo | semNecessidadesSemTratamento
        if (n == SaudePet.comNecessidadesOuTratamentoContinuo) {
            if (pref.getAceiteNecessidades() == AceiteNecessidades.ACEITA_NECESSIDADES_OU_TRATAMENTO)
                res.addPontos(10, "Aceita necessidades especiais/tratamento contínuo");
            else if (pref.getAceiteNecessidades() == AceiteNecessidades.SOMENTE_SEM_NECESSIDADES)
                res.addPenalidade(10, "Adotante só aceita sem necessidades/tratamento");
        } else if (n == SaudePet.semNecessidadesSemTratamento) {
            if (pref.getAceiteNecessidades() == AceiteNecessidades.ACEITA_NECESSIDADES_OU_TRATAMENTO)
                res.addPontos(5, "Pet sem necessidades; adotante aceita mesmo assim");
        }

        // ===== SAÚDE — crônicas/incuráveis =====
        SaudePet d = pet.getCronicasOuIncuraveis(); // comDoencas | semDoencas
        if (d == SaudePet.semDoencas && pref.getAceiteCronicas() == AceiteCronicas.SOMENTE_SEM_CRONICAS)
            res.addPontos(5, "Sem doença crônica; adotante só aceita sem crônicas");
        if (d == SaudePet.comDoencas && pref.getAceiteCronicas() == AceiteCronicas.ACEITA_CRONICAS_OU_INCURAVEIS)
            res.addPontos(10, "Aceita doenças crônicas/incuráveis");

        // ===== SOCIAL / TEMPO =====
        if (pet.isSociavel() && pref.isPossuiOutrosAnimais())
            res.addPontos(5, "Pet sociável e adotante possui outros animais");
        if (pet.isNaoSociavel() && pref.isPossuiOutrosAnimais())
            res.marcarImpedimento("Pet não convive com outros e adotante já possui animais");
        if (pet.isExigeCuidadosConstantes()) {
            if (pref.isTemTempoParaCuidadosConstantes())
                res.addPontos(5, "Pet exige cuidados constantes e adotante tem disponibilidade");
            else
                res.marcarImpedimento("Pet exige cuidados constantes e adotante NÃO tem tempo");
        }

        return res;
    }
}