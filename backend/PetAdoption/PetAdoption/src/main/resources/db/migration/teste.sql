-- /tmp/teste.sql

-- 0) IDs temporários para encadear o teste
CREATE TEMP TABLE IF NOT EXISTS tmp_ids (
  adopter_id UUID,
  pet_ok     UUID,
  pet_nao_ok UUID,
  app_id     UUID
);
TRUNCATE tmp_ids;

-- 1) Parâmetros com email único (evita erro de UNIQUE)
WITH params AS (
  SELECT ('tester_'||substr(gen_random_uuid()::text,1,8)||'@example.com')::citext AS email
),

-- 2) USER upsert por email (pega id mesmo se já existia)
u AS (
  INSERT INTO user_admin (id, name, email, password)
  SELECT gen_random_uuid(), 'Tester', p.email, 'x'
  FROM params p
  ON CONFLICT (email) DO UPDATE SET name = EXCLUDED.name
  RETURNING id AS adopter_id
),

-- 3) PREFERÊNCIAS padrão (upsert por adopter_id)
pdefs AS (
  INSERT INTO adopter_preferences (
    preference_key, adopter_id,
    desired_species, desired_size,
    accepts_special_needs, accepts_ongoing_treatment, accepts_chronic_disease,
    has_other_pets, has_time_for_constant_care, updated_at
  )
  SELECT gen_random_uuid(), adopter_id,
         'DOG','MEDIUM',
         TRUE, TRUE, TRUE,
         TRUE, TRUE, now()
  FROM u
  ON CONFLICT (adopter_id)
  DO UPDATE SET
    desired_species = EXCLUDED.desired_species,
    desired_size = EXCLUDED.desired_size,
    accepts_special_needs = EXCLUDED.accepts_special_needs,
    accepts_ongoing_treatment = EXCLUDED.accepts_ongoing_treatment,
    accepts_chronic_disease = EXCLUDED.accepts_chronic_disease,
    has_other_pets = EXCLUDED.has_other_pets,
    has_time_for_constant_care = EXCLUDED.has_time_for_constant_care,
    updated_at = now()
  RETURNING adopter_id
),

-- 4) PET compatível (AVAILABLE)
pet_ok AS (
  INSERT INTO pets (
    id, name, species, size, sex, status,
    good_with_other_animals, requires_constant_care,
    has_ongoing_treatment, has_chronic_disease, has_special_needs,
    created_at, updated_at
  )
  VALUES (
    gen_random_uuid(), 'Bolt', 'DOG', 'MEDIUM', 'M', 'AVAILABLE',
    TRUE, TRUE,
    TRUE, 'ALLERGY', 'WHEELCHAIR',
    now(), now()
  )
  RETURNING id AS pet_id
),

-- 5) PET não disponível (ADOPTED) → função deve retornar NULL
pet_nao_ok AS (
  INSERT INTO pets (id, name, species, size, sex, status, created_at, updated_at)
  VALUES (gen_random_uuid(), 'Mimi', 'CAT', 'SMALL', 'F', 'ADOPTED', now(), now())
  RETURNING id AS pet_id
)

INSERT INTO tmp_ids(adopter_id, pet_ok, pet_nao_ok)
SELECT (SELECT adopter_id FROM pdefs),
       (SELECT pet_id FROM pet_ok),
       (SELECT pet_id FROM pet_nao_ok);

-- 6) Caso bom → 100
SELECT 'OK_compativel' AS caso,
       public.compute_compatibility_score(t.adopter_id, t.pet_ok, NULL) AS score
FROM tmp_ids t;

-- 7) Pet não AVAILABLE → NULL
SELECT 'PET_nao_AVAILABLE' AS caso,
       public.compute_compatibility_score(t.adopter_id, t.pet_nao_ok, NULL) AS score
FROM tmp_ids t;

-- 8) Impeditivo: other_pets = TRUE do adotante x pet NÃO convive → 0
UPDATE pets
   SET good_with_other_animals = FALSE
 WHERE id = (SELECT pet_ok FROM tmp_ids LIMIT 1);

SELECT 'Impeditivo_other_pets' AS caso,
       public.compute_compatibility_score(t.adopter_id, t.pet_ok, NULL) AS score
FROM tmp_ids t;

-- 9) Reverte imped. → volta a 100
UPDATE pets
   SET good_with_other_animals = TRUE
 WHERE id = (SELECT pet_ok FROM tmp_ids LIMIT 1);

SELECT 'OK_compativel_de_novo' AS caso,
       public.compute_compatibility_score(t.adopter_id, t.pet_ok, NULL) AS score
FROM tmp_ids t;

-- 10) Snapshot via trigger → 100
WITH app AS (
  INSERT INTO adoption_applications
    (id, adopter_id, pet_id, status, use_default_preferences, created_at)
  SELECT gen_random_uuid(), t.adopter_id, t.pet_ok, 'SUBMITTED', TRUE, now()
  FROM tmp_ids t
  RETURNING id
)
UPDATE tmp_ids
   SET app_id = (SELECT id FROM app LIMIT 1);

SELECT 'Score_com_snapshot' AS caso,
       public.compute_compatibility_score(t.adopter_id, t.pet_ok, t.app_id) AS score
FROM tmp_ids t;
