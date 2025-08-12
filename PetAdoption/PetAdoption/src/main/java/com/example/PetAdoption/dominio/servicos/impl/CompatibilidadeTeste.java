package com.example.PetAdoption.dominio.servicos.impl;

import com.example.PetAdoption.dominio.entidades.*;
import com.example.PetAdoption.dominio.enums.*;
import com.example.PetAdoption.dominio.servicos.CompatibilidadeService;
//import com.example.PetAdoption.dominio.servicos.impl.CompatibilidadeTabelaService;

public class CompatibilidadeTeste {

    public static void main(String[] args) {
        // ===== Cenário A (deve dar score alto, sem impeditivo) =====
        PetModel petA = new PetModel();
        petA.setId(1L);
        petA.setNome("Luna");
        petA.setEspecie(EspeciePet.cachorro);
        petA.setRaca(RacaPet.SRD);
        petA.setPorte(PortePet.pequeno);
        petA.setSexo(SexoPet.F);
        petA.setStatus(StatusPet.disponivel);
        petA.setNecessidadeOuTratamento(SaudePet.comNecessidadesOuTratamentoContinuo);
        petA.setCronicasOuIncuraveis(SaudePet.semDoencas);
        petA.setSociavel(true);
        petA.setNaoSociavel(false);
        petA.setExigeCuidadosConstantes(true);

        PreferenciasAdotante prefA = new PreferenciasAdotante();
        prefA.setEspecieDesejada(EspeciePet.cachorro);
        prefA.setRacaDesejada(RacaPet.SRD);
        prefA.setPorteDesejado(PortePet.pequeno);
        prefA.setSexoDesejado(SexoPet.F);
        prefA.setAceiteNecessidades(AceiteNecessidades.ACEITA_NECESSIDADES_OU_TRATAMENTO);
        prefA.setAceiteCronicas(AceiteCronicas.SOMENTE_SEM_CRONICAS);
        prefA.setPossuiOutrosAnimais(true);
        prefA.setTemTempoParaCuidadosConstantes(true);

        AdotanteModel adotanteA = new AdotanteModel();
        adotanteA.setId(100L);
        adotanteA.setNome("Kamilla");
        adotanteA.setPreferencias(prefA);

        // ===== Cenário B (deve marcar impeditivo) =====
        PetModel petB = new PetModel();
        petB.setId(2L);
        petB.setNome("Neco");
        petB.setEspecie(EspeciePet.gato);
        petB.setRaca(RacaPet.outros);
        petB.setPorte(PortePet.medio);
        petB.setSexo(SexoPet.M);
        petB.setStatus(StatusPet.disponivel);
        petB.setNecessidadeOuTratamento(SaudePet.semNecessidadesSemTratamento);
        petB.setCronicasOuIncuraveis(SaudePet.comDoencas);
        petB.setSociavel(false);
        petB.setNaoSociavel(true);          // não convive com outros
        petB.setExigeCuidadosConstantes(true);

        PreferenciasAdotante prefB = new PreferenciasAdotante();
        // Sem preferência de espécie/raça/porte/sexo (null não pontua)
        prefB.setAceiteNecessidades(AceiteNecessidades.SOMENTE_SEM_NECESSIDADES);
        prefB.setAceiteCronicas(AceiteCronicas.SOMENTE_SEM_CRONICAS);
        prefB.setPossuiOutrosAnimais(true);               // vai causar impeditivo com naoSociavel
        prefB.setTemTempoParaCuidadosConstantes(false);   // vai causar impeditivo com exigeCuidadosConstantes

        AdotanteModel adotanteB = new AdotanteModel();
        adotanteB.setId(101L);
        adotanteB.setNome("João");
        adotanteB.setPreferencias(prefB);

        // ===== Rodar serviço =====
        CompatibilidadeService svc = new CompatibilidadeTabelaService();

        ResultadoCompatibilidade rA = svc.avaliar(adotanteA, petA);
        ResultadoCompatibilidade rB = svc.avaliar(adotanteB, petB);

        // ===== Imprimir resultados =====
        System.out.println("=== CENÁRIO A ===");
        imprimir(petA, adotanteA, rA);

        System.out.println("\n=== CENÁRIO B ===");
        imprimir(petB, adotanteB, rB);
    }

    private static void imprimir(PetModel pet, AdotanteModel adotante, ResultadoCompatibilidade r) {
        System.out.printf("Pet: %s (%s, %s, %s)%n",
                pet.getNome(), pet.getEspecie(), pet.getRaca(), pet.getPorte());
        System.out.printf("Adotante: %s%n", adotante.getNome());
        System.out.printf("Score: %d%n", r.getScore());
        System.out.printf("Impeditivo: %s%n", r.isImpeditivo() ? "SIM" : "NÃO");
        System.out.println("Motivos:");
        for (String m : r.getMotivos()) System.out.println(" - " + m);
    }
}