-- Flyway V1 — initial schema (PostgreSQL)
-- Notes:
-- - Uses CITEXT and PGCRYPTO (installable by this script if allowed).
-- - Creates base tables in FK-safe order; cycles are wired via ALTER TABLE at the end.

-- === Extensions ===
CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- === Types ===
DO $$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'event_type_enum') THEN
    CREATE TYPE event_type_enum AS ENUM ('HEALTH','RELOCATION','VISIT');
  END IF;
END $$;

-- ========== ORG ==========
CREATE TABLE IF NOT EXISTS org (
  org_id       UUID PRIMARY KEY,
  name         TEXT NOT NULL,
  email        TEXT UNIQUE,
  phone        TEXT,
  pet_owned_id UUID -- FK to pets(id), added after pets exists
);

-- ========== USER_ADMIN ==========
CREATE TABLE IF NOT EXISTS user_admin (
  id           UUID PRIMARY KEY,
  name         TEXT NOT NULL,
  surname      TEXT NOT NULL,
  email        CITEXT UNIQUE,
  phone        TEXT,
  cpf          TEXT NOT NULL UNIQUE,
  cep          TEXT,
  street       TEXT,
  streetNumber TEXT,
  uf           TEXT,
  neighborhood TEXT,
  city         TEXT,
  org_id       UUID,     -- FK to org(org_id)
  password     TEXT NOT NULL,
  adoptiona_id UUID,     -- FK to adoptions(id), added after adoptions exists
  CONSTRAINT fk_useradmin_org
    FOREIGN KEY (org_id) REFERENCES org(org_id)
);

-- ========== USER_ROLES ==========
CREATE TABLE IF NOT EXISTS user_roles (
  actor_id UUID NOT NULL,  -- FK to user_admin(id)
  role     TEXT NOT NULL CHECK (role IN ('USER','ADMIN')),
  PRIMARY KEY (actor_id, role),
  CONSTRAINT fk_roles_user
    FOREIGN KEY (actor_id) REFERENCES user_admin(id) ON DELETE CASCADE
);

-- ========== ADOPTER_PREFERENCES (default per user; will be constrained to 0..1 in V2) ==========
CREATE TABLE IF NOT EXISTS adopter_preferences (
  preference_key UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  adopter_id     UUID NOT NULL,  -- FK to user_admin(id)
  desired_species TEXT,
  desired_size    TEXT,
  accepts_special_needs      BOOLEAN,
  accepts_ongoing_treatment  BOOLEAN,
  accepts_chronic_disease    BOOLEAN,
  has_other_pets             BOOLEAN,
  has_time_for_constant_care BOOLEAN,
  updated_at     TIMESTAMPTZ,
  CONSTRAINT fk_prefs_adopter
    FOREIGN KEY (adopter_id) REFERENCES user_admin(id) ON DELETE CASCADE
);

-- ========== PETS ==========
CREATE TABLE IF NOT EXISTS pets (
  id          UUID PRIMARY KEY,
  name        TEXT,
  species     TEXT NOT NULL,
  breed       TEXT,
  size        TEXT NOT NULL,
  sex         TEXT NOT NULL,
  status      TEXT NOT NULL CHECK (status IN ('AVAILABLE','ADOPTED','LOST','DECEASED')),
  rescued_by_id UUID,  -- FK -> user_admin(id)
  has_special_needs     TEXT,
  has_ongoing_treatment BOOLEAN,
  has_chronic_disease   TEXT,
  good_with_other_animals BOOLEAN,
  requires_constant_care  BOOLEAN,
  registered_date DATE,
  rescued_at      DATE,
  created_at      TIMESTAMPTZ,
  updated_at      TIMESTAMPTZ,
  pet_description TEXT,
  pet_image       TEXT,
  CONSTRAINT fk_pets_rescuer
    FOREIGN KEY (rescued_by_id) REFERENCES user_admin(id)
);

CREATE INDEX IF NOT EXISTS idx_pets_status ON pets(status);

-- ========== ADOPTION_APPLICATIONS ==========
CREATE TABLE IF NOT EXISTS adoption_applications (
  id           UUID PRIMARY KEY,
  adopter_id   UUID NOT NULL,  -- FK -> user_admin(id)
  pet_id       UUID NOT NULL,  -- FK -> pets(id)
  status       TEXT NOT NULL CHECK (status IN ('DRAFT','SUBMITTED','IN_REVIEW','APPROVED','REJECTED','WITHDRAWN')),
  submitted_at TIMESTAMPTZ,
  reviewed_by  UUID,           -- FK -> user_admin(id)
  review_notes TEXT,
  created_at   TIMESTAMPTZ,
  updated_at   TIMESTAMPTZ,
  CONSTRAINT fk_apps_adopter
    FOREIGN KEY (adopter_id) REFERENCES user_admin(id) ON DELETE CASCADE,
  CONSTRAINT fk_apps_pet
    FOREIGN KEY (pet_id)      REFERENCES pets(id) ON DELETE RESTRICT,
  CONSTRAINT fk_apps_reviewer
    FOREIGN KEY (reviewed_by) REFERENCES user_admin(id)
);

-- ========== ADOPTIONS ==========
CREATE TABLE IF NOT EXISTS adoptions (
  id              UUID PRIMARY KEY,
  application_id  UUID NOT NULL, -- FK -> adoption_applications(id)
  pet_id          UUID NOT NULL, -- FK -> pets(id)
  tutor_id        UUID NOT NULL, -- FK -> user_admin(id)
  status          TEXT NOT NULL CHECK (status IN ('ACTIVE','RETURNED','ENDED','CANCELLED')),
  adoption_date   DATE,
  end_date        DATE,
  end_reason      TEXT,
  created_at      TIMESTAMPTZ,
  CONSTRAINT fk_adopt_app
    FOREIGN KEY (application_id) REFERENCES adoption_applications(id) ON DELETE RESTRICT,
  CONSTRAINT fk_adopt_pet
    FOREIGN KEY (pet_id)         REFERENCES pets(id) ON DELETE RESTRICT,
  CONSTRAINT fk_adopt_tutor
    FOREIGN KEY (tutor_id)       REFERENCES user_admin(id) ON DELETE RESTRICT
);

-- Wire cyclic FKs now that targets exist
ALTER TABLE public.user_admin
  ADD CONSTRAINT fk_useradmin_adoptiona
  FOREIGN KEY (adoptiona_id) REFERENCES public.adoptions(id);

ALTER TABLE public.org
  ADD CONSTRAINT fk_org_pet_owned
  FOREIGN KEY (pet_owned_id) REFERENCES public.pets(id);

-- ========== EVENTS ==========
CREATE TABLE IF NOT EXISTS events (
  id           UUID PRIMARY KEY,
  pet_id       UUID NOT NULL, -- FK -> pets(id)
  adoption_id  UUID,          -- optional -> adoptions(id)
  event_type   event_type_enum NOT NULL,
  phase        TEXT NOT NULL CHECK (phase IN ('RESCUE','SHELTER','POST_ADOPTION')),
  date         DATE,
  incident_type TEXT,
  description  TEXT,
  location     TEXT,
  CONSTRAINT fk_events_pet
    FOREIGN KEY (pet_id)      REFERENCES pets(id) ON DELETE CASCADE,
  CONSTRAINT fk_events_adopt
    FOREIGN KEY (adoption_id) REFERENCES adoptions(id)
);

-- PET_PHOTOS and PET_PHOTOS_INLINE were removed: images are now referenced via `pets.pet_image`.
