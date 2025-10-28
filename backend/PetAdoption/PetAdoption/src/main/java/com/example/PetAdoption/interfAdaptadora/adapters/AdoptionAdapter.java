package com.example.PetAdoption.interfAdaptadora.adapters;
import com.example.PetAdoption.dominio.InterfRepositories.AdoptionPort;
import com.example.PetAdoption.dominio.entidades.PetRecommendationModel;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaCompatibilityRepository;
import com.example.PetAdoption.interfAdaptadora.interfJPA.projections.PetCompatibilityRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AdoptionAdapter implements AdoptionPort {

    private final JpaCompatibilityRepository jpaCompat;
    private final JdbcTemplate jdbc;

    public AdoptionAdapter(JpaCompatibilityRepository jpaCompat, JdbcTemplate jdbc) {
        this.jpaCompat = jpaCompat;
        this.jdbc = jdbc;
    }

    @Override
    public List<PetRecommendationModel> rankedPets(UUID adopterId, UUID applicationId, int limit, int offset) {
        List<PetCompatibilityRow> rows =
                jpaCompat.findRankedPets(adopterId, applicationId, limit, offset);

        return rows.stream().map(r -> {
            PetRecommendationModel pr = new PetRecommendationModel();
            pr.setPetId(r.getPetId());
            pr.setName(r.getName());
            pr.setSpecies(r.getSpecies());
            pr.setSize(r.getSize());
            pr.setScore(r.getScore());
            return pr;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UUID createApplication(UUID adopterId, UUID petId, boolean useDefaultPreferences) {
        UUID appId = UUID.randomUUID();
        String sql = """
            INSERT INTO adoption_applications
              (id, adopter_id, pet_id, use_default_preferences, created_at, updated_at)
            VALUES
              (?,  ?,          ?,      ?,                      ?,          ?)
            """;
        Instant now = Instant.now();

        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, appId);
            ps.setObject(2, adopterId);
            ps.setObject(3, petId);
            ps.setBoolean(4, useDefaultPreferences);
            ps.setTimestamp(5, Timestamp.from(now));
            ps.setTimestamp(6, Timestamp.from(now));
            return ps;
        });

        return appId;
    }

    @Override
    @Transactional
    public void confirmAdoption(UUID applicationId) {
        var row = jdbc.queryForMap(
            "SELECT pet_id FROM adoption_applications WHERE id = ?",
            applicationId
        );
        UUID petId = (UUID) row.get("pet_id");

        int upd = jdbc.update("""
            UPDATE pets
               SET status = 'ADOPTED', updated_at = now()
             WHERE id = ? AND status = 'AVAILABLE'
            """, petId);

        if (upd == 0) {
            throw new IllegalStateException("Pet não está AVAILABLE ou não existe: " + petId);
        }

        try {
            jdbc.update("""
                INSERT INTO adoption (id, adopter_id, pet_id, application_id, adopted_at)
                SELECT gen_random_uuid(), a.adopter_id, a.pet_id, a.id, now()
                  FROM adoption_applications a
                 WHERE a.id = ?
                """, applicationId);
        } catch (Exception ignore) {
            // Se a tabela 'adoption' não existir, ignoramos.
        }
    }
}