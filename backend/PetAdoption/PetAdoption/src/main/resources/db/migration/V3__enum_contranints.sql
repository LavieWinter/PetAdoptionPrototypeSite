-- V3__enum_checks.sql
-- Complementa V1/V2 adicionando CHECK constraints alinhadas aos enums Java,
-- sem remover nada já existente. Idempotente e seguro.

-- Helper: cria CHECK se a coluna existir e a constraint ainda não existir
CREATE OR REPLACE FUNCTION _add_check_if_missing(
  in_schema text, in_table text, constraint_name text, check_sql text
) RETURNS void LANGUAGE plpgsql AS $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.tables
    WHERE table_schema = in_schema AND table_name = in_table
  ) THEN
    IF NOT EXISTS (
      SELECT 1
      FROM pg_constraint c
      JOIN pg_class t ON t.oid = c.conrelid
      JOIN pg_namespace n ON n.oid = t.relnamespace
      WHERE n.nspname = in_schema
        AND t.relname = in_table
        AND c.conname = constraint_name
    ) THEN
      EXECUTE format('ALTER TABLE %I.%I ADD CONSTRAINT %I CHECK (%s) NOT VALID',
                     in_schema, in_table, constraint_name, check_sql);
      -- valida (se já houver dados inválidos, aqui acusará)
      EXECUTE format('ALTER TABLE %I.%I VALIDATE CONSTRAINT %I',
                     in_schema, in_table, constraint_name);
    END IF;
  END IF;
END$$;

-- Convenção: assumindo tudo no schema público
-- Ajuste 'public' abaixo se você usa outro schema.
DO $body$
BEGIN
  /* =======================
     pets (SpeciesPet, PetSize, PetGender, PetStatus)
     ======================= */

  -- species ∈ {CAT, DOG}
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='pets' AND column_name='species') THEN
    PERFORM _add_check_if_missing('public','pets','chk_pets_species_enum',
      $$ species IN ('CAT','DOG') $$);
  END IF;

  -- size ∈ {SMALL, MEDIUM, LARGE}
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='pets' AND column_name='size') THEN
    PERFORM _add_check_if_missing('public','pets','chk_pets_size_enum',
      $$ size IN ('SMALL','MEDIUM','LARGE') $$);
  END IF;

  -- sex ∈ {M, F}
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='pets' AND column_name='sex') THEN
    PERFORM _add_check_if_missing('public','pets','chk_pets_sex_enum',
      $$ sex IN ('M','F') $$);
  END IF;

  -- status (se V1 já colocou um CHECK, não criaremos outro com mesmo nome)
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='pets' AND column_name='status') THEN
    PERFORM _add_check_if_missing('public','pets','chk_pets_status_enum',
      $$ status IN ('AVAILABLE','ADOPTED','LOST','DECEASED') $$);
  END IF;

  /* =======================
     adoption_applications.status (ApplicationStatus)
     ======================= */
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='adoption_applications' AND column_name='status') THEN
    PERFORM _add_check_if_missing('public','adoption_applications','chk_applications_status_enum',
      $$ status IN ('DRAFT','SUBMITTED','IN_REVIEW','APPROVED','REJECTED','WITHDRAWN') $$);
  END IF;

  /* =======================
     adoptions.status (AdoptionStatus)
     ======================= */
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='adoptions' AND column_name='status') THEN
    PERFORM _add_check_if_missing('public','adoptions','chk_adoptions_status_enum',
      $$ status IN ('ACTIVE','RETURNED','ENDED','CANCELLED') $$);
  END IF;

  /* =======================
     events.incident_type (IncidentType)
     (event_type já é ENUM em V1; aqui só a subcategoria textual, se existir)
     ======================= */
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema='public' AND table_name='events' AND column_name='incident_type') THEN
    PERFORM _add_check_if_missing('public','events','chk_events_incident_type_enum',
      $$ incident_type IN ('VACCINATION','NEUTERING','DISEASE','OTHER') $$);
  END IF;

  /* =======================
     application_preferences snapshot
     desired_species (SpeciesPet), desired_size (PetSize),
     aceite_cronicas (AceiteCronicas), aceite_necessidades (AceiteNecessidades)
     ======================= */
  IF EXISTS (SELECT 1 FROM information_schema.tables
             WHERE table_schema='public' AND table_name='application_preferences') THEN

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='application_preferences' AND column_name='desired_species') THEN
      PERFORM _add_check_if_missing('public','application_preferences','chk_ap_desired_species_enum',
        $$ desired_species IN ('CAT','DOG') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='application_preferences' AND column_name='desired_size') THEN
      PERFORM _add_check_if_missing('public','application_preferences','chk_ap_desired_size_enum',
        $$ desired_size IN ('SMALL','MEDIUM','LARGE') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='application_preferences' AND column_name='aceite_cronicas') THEN
      PERFORM _add_check_if_missing('public','application_preferences','chk_ap_aceite_cronicas_enum',
        $$ aceite_cronicas IN ('SOMENTE_SEM_CRONICAS','ACEITA_CRONICAS_OU_INCURAVEIS') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='application_preferences' AND column_name='aceite_necessidades') THEN
      PERFORM _add_check_if_missing('public','application_preferences','chk_ap_aceite_necessidades_enum',
        $$ aceite_necessidades IN ('ACEITA_NECESSIDADES_OU_TRATAMENTO','SOMENTE_SEM_NECESSIDADES') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='application_preferences' AND column_name='pet_gender') THEN
      PERFORM _add_check_if_missing('public','application_preferences','chk_ap_pet_gender_enum',
        $$ pet_gender IN ('M','F') $$);
    END IF;
  END IF;

  /* =======================
     adopter_preferences (preferências padrão do adotante)
     desired_species, desired_size, aceite_cronicas, aceite_necessidades
     ======================= */
  IF EXISTS (SELECT 1 FROM information_schema.tables
             WHERE table_schema='public' AND table_name='adopter_preferences') THEN

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='adopter_preferences' AND column_name='desired_species') THEN
      PERFORM _add_check_if_missing('public','adopter_preferences','chk_dp_desired_species_enum',
        $$ desired_species IN ('CAT','DOG') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='adopter_preferences' AND column_name='desired_size') THEN
      PERFORM _add_check_if_missing('public','adopter_preferences','chk_dp_desired_size_enum',
        $$ desired_size IN ('SMALL','MEDIUM','LARGE') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='adopter_preferences' AND column_name='aceite_cronicas') THEN
      PERFORM _add_check_if_missing('public','adopter_preferences','chk_dp_aceite_cronicas_enum',
        $$ aceite_cronicas IN ('SOMENTE_SEM_CRONICAS','ACEITA_CRONICAS_OU_INCURAVEIS') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='adopter_preferences' AND column_name='aceite_necessidades') THEN
      PERFORM _add_check_if_missing('public','adopter_preferences','chk_dp_aceite_necessidades_enum',
        $$ aceite_necessidades IN ('ACEITA_NECESSIDADES_OU_TRATAMENTO','SOMENTE_SEM_NECESSIDADES') $$);
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema='public' AND table_name='adopter_preferences' AND column_name='pet_gender') THEN
      PERFORM _add_check_if_missing('public','adopter_preferences','chk_dp_pet_gender_enum',
        $$ pet_gender IN ('M','F') $$);
    END IF;
  END IF;

END;
$body$;

-- limpeza: função helper pode ficar, mas se quiser remover após a migração:
-- DROP FUNCTION IF EXISTS _add_check_if_missing(text,text,text,text);
