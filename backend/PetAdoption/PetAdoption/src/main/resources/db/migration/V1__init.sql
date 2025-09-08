
-- Generated from the provided Mermaid ERD + photo tables (PostgreSQL)
-- Conventions:
-- - Keep types as declared (e.g., TEXT vs BOOLEAN) even when booleans seem expected.
-- - Implement value-sets like "A|B|C" as CHECKs on TEXT.
-- - True ENUM used only where 'enum' is specified in Mermaid.
-- - Resolve FK cycles with ALTER TABLE after all participants exist.

CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ===============================
-- Drop in dependency-safe order
-- ===============================
DROP TABLE IF EXISTS PET_PHOTOS_INLINE CASCADE;
DROP TABLE IF EXISTS PET_PHOTOS CASCADE;
DROP TABLE IF EXISTS EVENTS CASCADE;
DROP TABLE IF EXISTS ADOPTIONS CASCADE;
DROP TABLE IF EXISTS ADOPTION_APPLICATIONS CASCADE;
DROP TABLE IF EXISTS PETS CASCADE;
DROP TABLE IF EXISTS ADOPTER_PREFERENCES CASCADE;
DROP TABLE IF EXISTS USER_ROLES CASCADE;
DROP TABLE IF EXISTS USER_ADMIN CASCADE;
DROP TABLE IF EXISTS ORG CASCADE;

-- ======== ORG ========
CREATE TABLE ORG (
  org_id       UUID PRIMARY KEY,
  name         TEXT NOT NULL,
  email        TEXT UNIQUE,
  phone        TEXT,
  pet_owned_id UUID -- FK to PETS(id) will be added after PETS exists
);

-- ======== USER_ADMIN ========
CREATE TABLE USER_ADMIN (
  id           UUID PRIMARY KEY,
  name         TEXT NOT NULL,
  email        CITEXT UNIQUE,
  phone        TEXT,
  org_id       UUID,     -- FK to ORG(org_id)
  password     TEXT NOT NULL,
  adoptiona_id UUID,     -- FK to ADOPTIONS(id) will be added later
  CONSTRAINT fk_useradmin_org
    FOREIGN KEY (org_id) REFERENCES ORG(org_id)
);

-- ======== USER_ROLES ========
CREATE TABLE USER_ROLES (
  actor_id UUID NOT NULL,  -- FK to USER_ADMIN(id)
  role     TEXT NOT NULL CHECK (role IN ('ADOTANTE','DOADOR','ADMIN')),
  PRIMARY KEY (actor_id, role),
  CONSTRAINT fk_roles_user
    FOREIGN KEY (actor_id) REFERENCES USER_ADMIN(id) ON DELETE CASCADE
);

-- ======== ADOPTER_PREFERENCES ========
CREATE TABLE ADOPTER_PREFERENCES (
  preference_key UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  adopter_id     UUID NOT NULL,  -- FK to USER_ADMIN(id)
  desired_species TEXT,
  desired_size    TEXT,
  accepts_special_needs      BOOLEAN,
  accepts_ongoing_treatment  BOOLEAN,
  accepts_chronic_disease    BOOLEAN,
  has_other_pets             BOOLEAN,
  has_time_for_constant_care BOOLEAN,
  updated_at     TIMESTAMPTZ,
  CONSTRAINT fk_prefs_adopter
    FOREIGN KEY (adopter_id) REFERENCES USER_ADMIN(id) ON DELETE CASCADE
);

-- ======== PETS ========
CREATE TABLE PETS (
  id          UUID PRIMARY KEY,
  name        TEXT,
  species     TEXT NOT NULL,
  breed       TEXT,
  size        TEXT NOT NULL,
  sex         TEXT NOT NULL,
  status      TEXT NOT NULL CHECK (status IN ('AVAILABLE','ADOPTED','LOST','DECEASED')),
  rescued_by_id UUID,  -- FK -> USER_ADMIN(id)
  has_special_needs   TEXT,
  has_ongoing_treatment BOOLEAN,
  has_chronic_disease TEXT,
  good_with_other_animals BOOLEAN,
  requires_constant_care  BOOLEAN,
  registered_date DATE,
  rescued_at      DATE,
  created_at      TIMESTAMPTZ,
  updated_at      TIMESTAMPTZ,
  CONSTRAINT fk_pets_rescuer
    FOREIGN KEY (rescued_by_id) REFERENCES USER_ADMIN(id)
);

-- Useful indexes
CREATE INDEX idx_pets_status ON PETS(status);

-- ======== ADOPTION_APPLICATIONS ========
CREATE TABLE ADOPTION_APPLICATIONS (
  id           UUID PRIMARY KEY,
  adopter_id   UUID NOT NULL,  -- FK -> USER_ADMIN(id)
  pet_id       UUID NOT NULL,  -- FK -> PETS(id)
  status       TEXT NOT NULL CHECK (status IN ('DRAFT','SUBMITTED','IN_REVIEW','APPROVED','REJECTED','WITHDRAWN')),
  submitted_at TIMESTAMPTZ,
  reviewed_by  UUID,           -- FK -> USER_ADMIN(id)
  review_notes TEXT,
  created_at   TIMESTAMPTZ,
  updated_at   TIMESTAMPTZ,
  CONSTRAINT fk_apps_adopter
    FOREIGN KEY (adopter_id) REFERENCES USER_ADMIN(id) ON DELETE CASCADE,
  CONSTRAINT fk_apps_pet
    FOREIGN KEY (pet_id)      REFERENCES PETS(id) ON DELETE RESTRICT,
  CONSTRAINT fk_apps_reviewer
    FOREIGN KEY (reviewed_by) REFERENCES USER_ADMIN(id)
);

-- ======== ADOPTIONS ========
CREATE TABLE ADOPTIONS (
  id              UUID PRIMARY KEY,
  application_id  UUID NOT NULL, -- FK -> ADOPTION_APPLICATIONS(id)
  pet_id          UUID NOT NULL, -- FK -> PETS(id)
  tutor_id        UUID NOT NULL, -- FK -> USER_ADMIN(id)
  status          TEXT NOT NULL CHECK (status IN ('ACTIVE','RETURNED','ENDED','CANCELLED')),
  adoption_date   DATE,
  end_date        DATE,
  end_reason      TEXT,
  created_at      TIMESTAMPTZ,
  CONSTRAINT fk_adopt_app
    FOREIGN KEY (application_id) REFERENCES ADOPTION_APPLICATIONS(id) ON DELETE RESTRICT,
  CONSTRAINT fk_adopt_pet
    FOREIGN KEY (pet_id)         REFERENCES PETS(id) ON DELETE RESTRICT,
  CONSTRAINT fk_adopt_tutor
    FOREIGN KEY (tutor_id)       REFERENCES USER_ADMIN(id) ON DELETE RESTRICT
);

-- Now that ADOPTIONS & PETS exist, wire the deferred FKs
ALTER TABLE USER_ADMIN
  ADD CONSTRAINT fk_useradmin_adoptiona
  FOREIGN KEY (adoptiona_id) REFERENCES ADOPTIONS(id);

ALTER TABLE ORG
  ADD CONSTRAINT fk_org_pet_owned
  FOREIGN KEY (pet_owned_id) REFERENCES PETS(id);

-- ======== EVENTS ========
DO $$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'event_type_enum') THEN
    CREATE TYPE event_type_enum AS ENUM ('HEALTH','RELOCATION','VISIT');
  END IF;
END $$;

CREATE TABLE EVENTS (
  id           UUID PRIMARY KEY,
  pet_id       UUID NOT NULL, -- FK -> PETS(id)
  adoption_id  UUID,          -- opcional -> ADOPTIONS(id)
  event_type   event_type_enum NOT NULL,
  phase        TEXT NOT NULL CHECK (phase IN ('RESCUE','SHELTER','POST_ADOPTION')),
  date         DATE,
  incident_type TEXT,
  description  TEXT,
  location     TEXT,
  CONSTRAINT fk_events_pet
    FOREIGN KEY (pet_id)      REFERENCES PETS(id) ON DELETE CASCADE,
  CONSTRAINT fk_events_adopt
    FOREIGN KEY (adoption_id) REFERENCES ADOPTIONS(id)
);

-- ======== PET_PHOTOS (metadata + storage ref) ========
CREATE TABLE PET_PHOTOS (
  id               UUID PRIMARY KEY,
  pet_id           UUID NOT NULL REFERENCES PETS(id) ON DELETE CASCADE,
  storage_provider TEXT NOT NULL CHECK (storage_provider IN ('S3','GCS','AZURE','LOCAL','R2')),
  object_key       TEXT NOT NULL,
  public_url       TEXT,
  mime_type        TEXT NOT NULL,
  width_px         INT,
  height_px        INT,
  size_bytes       BIGINT,
  checksum_sha256  BYTEA,
  is_primary       BOOLEAN NOT NULL DEFAULT FALSE,
  sort_order       INT NOT NULL DEFAULT 0,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_pet_photos_primary ON PET_PHOTOS(pet_id) WHERE is_primary;
CREATE INDEX idx_pet_photos_pet ON PET_PHOTOS(pet_id, sort_order);

-- ======== PET_PHOTOS_INLINE (image blob) ========
CREATE TABLE PET_PHOTOS_INLINE (
  id          UUID PRIMARY KEY,
  pet_id      UUID NOT NULL REFERENCES PETS(id) ON DELETE CASCADE,
  mime_type   TEXT NOT NULL,
  size_bytes  BIGINT,
  image_data  BYTEA NOT NULL,
  is_primary  BOOLEAN NOT NULL DEFAULT FALSE,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_ppi_primary ON PET_PHOTOS_INLINE(pet_id) WHERE is_primary;
CREATE INDEX idx_ppi_pet ON PET_PHOTOS_INLINE(pet_id);
