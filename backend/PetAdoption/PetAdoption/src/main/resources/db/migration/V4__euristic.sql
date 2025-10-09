CREATE OR REPLACE FUNCTION compute_compatibility_score(
  p_adopter_id    UUID,
  p_pet_id        UUID,
  p_application_id UUID DEFAULT NULL
) RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
  pet RECORD;
  prefs RECORD;
  score INT := 0;
  -- Pesos (tornando fácil de ajustar depois)
  w_species CONSTANT INT := 30;
  w_size    CONSTANT INT := 20;
  w_otherpets CONSTANT INT := 20;
  w_care      CONSTANT INT := 15;
  w_ongoing   CONSTANT INT := 5;
  w_chronic   CONSTANT INT := 15;
  w_special   CONSTANT INT := 5;
BEGIN
  -- 1) Carrega o pet
  SELECT *
    INTO pet
    FROM pets
   WHERE id = p_pet_id
   LIMIT 1;

  IF NOT FOUND THEN
    RAISE EXCEPTION 'Pet % não encontrado', p_pet_id;
  END IF;

  -- (Opcional) se quiser ignorar pets não disponíveis, descomente:
  IF pet.status IS DISTINCT FROM 'AVAILABLE' THEN
    RETURN NULL;
  END IF;

  -- 2) Carrega preferências: primeiro tenta APPLICATION_PREFERENCES, senão ADOPTER_PREFERENCES
  IF p_application_id IS NOT NULL THEN
    SELECT *
      INTO prefs
      FROM application_preferences
     WHERE application_id = p_application_id
     LIMIT 1;
  END IF;

  IF NOT FOUND THEN
    SELECT *
      INTO prefs
      FROM adopter_preferences
     WHERE adopter_id = p_adopter_id
     ORDER BY updated_at DESC NULLS LAST
     LIMIT 1;
  END IF;

  -- 3) Impeditivos (zeram a pontuação)
  -- Trate nulos como "desconhecido" (não bloqueia)
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

  -- 4) Pontuação (somamos apenas quando ambos os lados permitem avaliar)
  -- Espécie
  IF prefs.desired_species IS NOT NULL AND pet.species IS NOT NULL
     AND prefs.desired_species = pet.species THEN
    score := score + w_species;
  END IF;

  -- Porte
  IF prefs.desired_size IS NOT NULL AND pet.size IS NOT NULL
     AND prefs.desired_size = pet.size THEN
    score := score + w_size;
  END IF;

  -- Convivência (só pontua se adotante tem outros pets E o pet convive bem)
  IF prefs.has_other_pets IS TRUE AND pet.good_with_other_animals IS TRUE THEN
    score := score + w_otherpets;
  END IF;

  -- Cuidado constante (só faz sentido pontuar quando o pet exige e o adotante tem tempo)
  IF pet.requires_constant_care IS TRUE AND prefs.has_time_for_constant_care IS TRUE THEN
    score := score + w_care;
  END IF;

  -- Tratamento contínuo
  IF pet.has_ongoing_treatment IS TRUE AND prefs.accepts_ongoing_treatment IS TRUE THEN
    score := score + w_ongoing;
  END IF;

  -- Doença crônica
  IF (pet.has_chronic_disease IS NOT NULL AND pet.has_chronic_disease <> '')
     AND prefs.accepts_chronic_disease IS TRUE THEN
    score := score + w_chronic;
  END IF;

  -- Necessidades especiais
  IF (pet.has_special_needs IS NOT NULL AND pet.has_special_needs <> '')
     AND prefs.accepts_special_needs IS TRUE THEN
    score := score + w_special;
  END IF;

  -- 5) Clampa em 0..100 por segurança
  IF score < 0 THEN score := 0; END IF;
  IF score > 100 THEN score := 100; END IF;

  RETURN score;
END;
$$;
