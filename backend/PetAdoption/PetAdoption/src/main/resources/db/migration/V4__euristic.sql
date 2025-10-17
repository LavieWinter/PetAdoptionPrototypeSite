CREATE OR REPLACE FUNCTION compute_compatibility_score(
  p_adopter_id     UUID,
  p_pet_id         UUID,
  p_application_id UUID DEFAULT NULL
) RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
  pet   pets%ROWTYPE;
  prefs adopter_preferences%ROWTYPE;           -- <== tipo concreto (estrutura conhecida)
  aprec application_preferences%ROWTYPE;
  score INT := 0;

  -- Pesos
  w_species CONSTANT INT := 30;
  w_size    CONSTANT INT := 20;
  w_otherpets CONSTANT INT := 20;
  w_care      CONSTANT INT := 15;
  w_ongoing   CONSTANT INT := 5;
  w_chronic   CONSTANT INT := 15;
  w_special   CONSTANT INT := 5;
BEGIN
  -- 1) Carrega pet
  SELECT * INTO pet FROM pets WHERE id = p_pet_id LIMIT 1;
  IF NOT FOUND THEN
    RAISE EXCEPTION 'Pet % não encontrado', p_pet_id;
  END IF;

  -- 2) Se pet não está AVAILABLE, retorna NULL (mantendo sua regra)
  IF pet.status IS DISTINCT FROM 'AVAILABLE' THEN
    RETURN NULL;
  END IF;

  -- 3) PREFERÊNCIAS: tenta snapshot da aplicação; senão, padrão do adotante
  IF p_application_id IS NOT NULL THEN
    SELECT * INTO aprec FROM application_preferences WHERE application_id = p_application_id LIMIT 1;
  END IF;

  IF FOUND THEN
    -- Copia campos do snapshot para o "prefs"
    prefs.desired_species            := aprec.desired_species;
    prefs.desired_size               := aprec.desired_size;
    prefs.accepts_special_needs      := aprec.accepts_special_needs;
    prefs.accepts_ongoing_treatment  := aprec.accepts_ongoing_treatment;
    prefs.accepts_chronic_disease    := aprec.accepts_chronic_disease;
    prefs.has_other_pets             := aprec.has_other_pets;
    prefs.has_time_for_constant_care := aprec.has_time_for_constant_care;
  ELSE
    SELECT *
      INTO prefs
      FROM adopter_preferences
     WHERE adopter_id = p_adopter_id
     ORDER BY updated_at DESC NULLS LAST
     LIMIT 1;
    -- Se não houver linha, "prefs" continua com estrutura conhecida (campos NULL) — seguro.
  END IF;

  -- 4) Impeditivos (zeram score) — só avalia se ambos os lados tiverem valor
  IF (pet.has_special_needs IS NOT NULL AND prefs.accepts_special_needs IS NOT NULL)
     AND (pet.has_special_needs <> '' AND prefs.accepts_special_needs = FALSE) THEN
    RETURN 0;
  END IF;

  IF (pet.has_ongoing_treatment IS NOT NULL AND prefs.accepts_ongoing_treatment IS NOT NULL)
     AND (pet.has_ongoing_treatment = TRUE AND prefs.accepts_ongoing_treatment = FALSE) THEN
    RETURN 0;
  END IF;

  IF (pet.has_chronic_disease IS NOT NULL AND prefs.accepts_chronic_disease IS NOT NULL)
     AND (pet.has_chronic_disease <> '' AND prefs.accepts_chronic_disease = FALSE) THEN
    RETURN 0;
  END IF;

  IF (pet.requires_constant_care IS NOT NULL AND prefs.has_time_for_constant_care IS NOT NULL)
     AND (pet.requires_constant_care = TRUE AND prefs.has_time_for_constant_care = FALSE) THEN
    RETURN 0;
  END IF;

  IF (prefs.has_other_pets IS NOT NULL AND pet.good_with_other_animals IS NOT NULL)
     AND (prefs.has_other_pets = TRUE AND pet.good_with_other_animals = FALSE) THEN
    RETURN 0;
  END IF;

  -- 5) Pontuação positiva
  IF prefs.desired_species IS NOT NULL AND pet.species IS NOT NULL
     AND prefs.desired_species = pet.species THEN
    score := score + w_species;
  END IF;

  IF prefs.desired_size IS NOT NULL AND pet.size IS NOT NULL
     AND prefs.desired_size = pet.size THEN
    score := score + w_size;
  END IF;

  IF prefs.has_other_pets IS TRUE AND pet.good_with_other_animals IS TRUE THEN
    score := score + w_otherpets;
  END IF;

  IF pet.requires_constant_care IS TRUE AND prefs.has_time_for_constant_care IS TRUE THEN
    score := score + w_care;
  END IF;

  IF pet.has_ongoing_treatment IS TRUE AND prefs.accepts_ongoing_treatment IS TRUE THEN
    score := score + w_ongoing;
  END IF;

  IF (pet.has_chronic_disease IS NOT NULL AND pet.has_chronic_disease <> '')
     AND prefs.accepts_chronic_disease IS TRUE THEN
    score := score + w_chronic;
  END IF;

  IF (pet.has_special_needs IS NOT NULL AND pet.has_special_needs <> '')
     AND prefs.accepts_special_needs IS TRUE THEN
    score := score + w_special;
  END IF;

  -- 6) Clampa 0..100
  IF score < 0 THEN score := 0; END IF;
  IF score > 100 THEN score := 100; END IF;

  RETURN score;
END;
$$;
