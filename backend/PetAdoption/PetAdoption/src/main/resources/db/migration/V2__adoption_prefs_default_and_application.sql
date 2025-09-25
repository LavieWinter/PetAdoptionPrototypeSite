-- Flyway V2 — default (per user) + per-application preferences, flag, trigger, backfill

-- 1) Enforce at most one default preferences row per user (0..1)
CREATE UNIQUE INDEX IF NOT EXISTS uq_default_pref_per_user
  ON adopter_preferences(adopter_id);

COMMENT ON TABLE adopter_preferences IS
  'Default adoption preferences per user (optional, 0..1).';

-- 2) Per-application snapshot table (1:1 with ADOPTION_APPLICATIONS)
CREATE TABLE IF NOT EXISTS application_preferences (
  application_id             UUID PRIMARY KEY
  REFERENCES adoption_applications(id) ON DELETE CASCADE,
  desired_species            TEXT,
  desired_size               TEXT,
  accepts_special_needs      BOOLEAN,
  accepts_ongoing_treatment  BOOLEAN,
  accepts_chronic_disease    BOOLEAN,
  has_other_pets             BOOLEAN,
  has_time_for_constant_care BOOLEAN,
  created_at                 TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at                 TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 3) Flag on adoption_applications to indicate cloning from defaults
ALTER TABLE adoption_applications
  ADD COLUMN IF NOT EXISTS use_default_preferences BOOLEAN NOT NULL DEFAULT FALSE;

COMMENT ON COLUMN adoption_applications.use_default_preferences IS
  'If TRUE, clone defaults from adopter_preferences into application_preferences upon application creation.';

-- 4) Trigger: copy defaults into application on INSERT when requested
CREATE OR REPLACE FUNCTION copy_default_prefs_to_application() RETURNS trigger AS $$
DECLARE
  def_rec adopter_preferences%ROWTYPE;
BEGIN
  IF NEW.use_default_preferences THEN
    SELECT *
      INTO def_rec
      FROM adopter_preferences
    WHERE adopter_id = NEW.adopter_id
    ORDER BY updated_at DESC NULLS LAST
    LIMIT 1;

    IF FOUND THEN
      INSERT INTO application_preferences (
        application_id,
        desired_species,
        desired_size,
        accepts_special_needs,
        accepts_ongoing_treatment,
        accepts_chronic_disease,
        has_other_pets,
        has_time_for_constant_care
      )
      VALUES (
        NEW.id,
        def_rec.desired_species,
        def_rec.desired_size,
        def_rec.accepts_special_needs,
        def_rec.accepts_ongoing_treatment,
        def_rec.accepts_chronic_disease,
        def_rec.has_other_pets,
        def_rec.has_time_for_constant_care
      )
      ON CONFLICT (application_id) DO NOTHING;
    END IF;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_copy_default_prefs ON adoption_applications;
CREATE TRIGGER trg_copy_default_prefs
AFTER INSERT ON adoption_applications
FOR EACH ROW
EXECUTE FUNCTION copy_default_prefs_to_application();

-- 5) Optional backfill for existing rows with the flag ON
INSERT INTO application_preferences (
  application_id,
  desired_species,
  desired_size,
  accepts_special_needs,
  accepts_ongoing_treatment,
  accepts_chronic_disease,
  has_other_pets,
  has_time_for_constant_care
)
SELECT a.id,
      d.desired_species,
      d.desired_size,
      d.accepts_special_needs,
      d.accepts_ongoing_treatment,
      d.accepts_chronic_disease,
      d.has_other_pets,
      d.has_time_for_constant_care
FROM adoption_applications a
JOIN adopter_preferences d
  ON d.adopter_id = a.adopter_id
LEFT JOIN application_preferences ap
  ON ap.application_id = a.id
WHERE a.use_default_preferences = TRUE
  AND ap.application_id IS NULL;
