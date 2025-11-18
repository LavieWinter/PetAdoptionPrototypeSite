
-- WITH fada AS (
--   SELECT org_id FROM org WHERE lower(name) = 'fada'
-- ), created AS (
--   INSERT INTO org (org_id, name, email, phone)
--   SELECT gen_random_uuid(), 'FADA', 'contato@fada.org.br', '+55 51 99999-0001'
--   WHERE NOT EXISTS (SELECT 1 FROM fada)
--   RETURNING org_id
-- )
-- SELECT COALESCE((SELECT org_id FROM fada), (SELECT org_id FROM created)) AS org_id;


-- -- Admin da FADA
-- INSERT INTO user_admin (user_id, org_id, name, email)
-- VALUES (
--   gen_random_uuid(),
--   (SELECT org_id FROM org WHERE name = 'FADA'),
--   'Admin FADA',
--   'admin@fada.org.br'
-- );
-- INSERT INTO user_admin (user_id, org_id, name, email)
-- VALUES (
--   gen_random_uuid(),
--   (SELECT org_id FROM org WHERE name = 'FADA'),
--   'Operações FADA',
--   'ops@fada.org.br'
-- );
-- --PETS
-- INSERT INTO pets (id, org_id, name, species, size, sex, status)
-- VALUES
--   (gen_random_uuid(), (SELECT org_id FROM org WHERE name = 'FADA'), 'Luna', 'CAT', 'SMALL',  'F', 'AVAILABLE'),
--   (gen_random_uuid(), (SELECT org_id FROM org WHERE name = 'FADA'), 'Thor', 'DOG', 'LARGE',  'M', 'AVAILABLE'),
--   (gen_random_uuid(), (SELECT org_id FROM org WHERE name = 'FADA'), 'Maya', 'DOG', 'MEDIUM', 'F', 'ADOPTED');

-- --pessoa fisica
-- INSERT INTO user_admin (user_id, org_id, name, email)
-- VALUES
--   (gen_random_uuid(), NULL, 'Carla Silva', 'carla@example.com'),
--   (gen_random_uuid(), NULL, 'Diego Souza', 'diego@example.com');

-- INSERT INTO adopter_preferences
--   (adopter_id, desired_species, desired_size, aceite_cronicas, aceite_necessidades)
-- VALUES
--   ((SELECT user_id FROM user_admin WHERE email = 'carla@example.com'),
--    'DOG', 'MEDIUM', 'ACEITA_CRONICAS_OU_INCURAVEIS', 'ACEITA_NECESSIDADES_OU_TRATAMENTO'),

--   ((SELECT user_id FROM user_admin WHERE email = 'diego@example.com'),
--    'CAT', 'SMALL',  'SOMENTE_SEM_CRONICAS',           'SOMENTE_SEM_NECESSIDADES');

-- -- user roles
-- -- Admin principal (já existente)
-- INSERT INTO user_roles (user_id, role)
-- VALUES (
--   (SELECT user_id FROM user_admin WHERE email = 'admin@fada.org.br'),
--   'ADMIN'
-- );

-- INSERT INTO user_roles (user_id, role)
-- VALUES (
--   (SELECT user_id FROM user_admin WHERE email = 'ops@fada.org.br'),
--   'ADMIN'
-- );

-- --
-- -- Carla
-- INSERT INTO user_roles (user_id, role)
-- VALUES (
--   (SELECT user_id FROM user_admin WHERE email = 'carla@example.com'),
--   'ADOTANTE'
-- );

-- -- Diego
-- INSERT INTO user_roles (user_id, role)
-- VALUES (
--   (SELECT user_id FROM user_admin WHERE email = 'diego@example.com'),
--   'ADOTANTE'
-- );

-- INSERT INTO adoption_applications
--   (application_id, adopter_id, pet_id, status, use_default_preferences, created_at)
-- VALUES
--   (
--     gen_random_uuid(),
--     (SELECT user_id FROM user_admin WHERE email = 'carla@example.com'),
--     (SELECT id      FROM pets       WHERE name  = 'Thor'),
--     'SUBMITTED',
--     TRUE,
--     NOW()
--   );


-- -- coloca a application em andamento (ex.: IN_REVIEW ou APPROVED)
-- UPDATE adoption_applications
-- SET status = 'IN_REVIEW'
-- WHERE adopter_id = (SELECT user_id FROM user_admin WHERE email = 'carla@example.com')
--   AND pet_id     = (SELECT id      FROM pets       WHERE name  = 'Thor');

-- INSERT INTO adoptions (adoption_id, application_id, status, started_at)
-- VALUES (
--   gen_random_uuid(),
--   (SELECT application_id
--      FROM adoption_applications
--     WHERE adopter_id = (SELECT user_id FROM user_admin WHERE email = 'carla@example.com')
--       AND pet_id     = (SELECT id      FROM pets       WHERE name  = 'Thor')),
--   'ACTIVE',
--   NOW()
-- );


-- -- Evento de saúde (já feito)
-- INSERT INTO events (event_id, pet_id, event_type, incident_type, notes, occurred_at)
-- VALUES (
--   gen_random_uuid(),
--   (SELECT id FROM pets WHERE name = 'Thor'),
--   'HEALTH', 'VACCINATION',
--   'Vacina V10 aplicada na triagem',
--   NOW() - INTERVAL '1 day'
-- );

-- -- Visita domiciliar (agendada)
-- INSERT INTO events (event_id, pet_id, event_type, incident_type, notes, occurred_at)
-- VALUES (
--   gen_random_uuid(),
--   (SELECT id FROM pets WHERE name = 'Thor'),
--   'VISIT', 'OTHER',
--   'Visita domiciliar agendada com a Carla',
--   NOW() + INTERVAL '3 days'
-- );

-- -- Visita ao abrigo (agendada)
-- INSERT INTO events (event_id, pet_id, event_type, incident_type, notes, occurred_at)
-- VALUES (
--   gen_random_uuid(),
--   (SELECT id FROM pets WHERE name = 'Thor'),
--   'VISIT', 'OTHER',
--   'Visita ao abrigo para integração com o Thor',
--   NOW() + INTERVAL '7 days'
-- );
