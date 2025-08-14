# Project Charter — 13/08/2025

> **Prazo final:** 25/11/2025 · **Code freeze:** 21/11/2025 · **Sprints:** S0 (05–21/08), S1 (26/08–11/09), S2 (16/09–02/10), S3 (07/10–23/10), S4 (28/10–25/11) · **Monorepo** (frontend+backend) ·

---

## Sumário
1. [Escopo inicial de cada Sprint](#1-escopo-inicial-de-cada-sprint)  
2. [Stack Tecnológico](#2-stack-tecnológico)  
3. [Papéis do time & RACI](#3-papéis-do-time--raci)  
4. [Ferramentas de acompanhamento](#4-ferramentas-de-acompanhamento)  
5. [Repositório (monorepo) e estrutura](#5-repositório-monorepo-e-estrutura)  
6. [Mapa de responsabilidades (RACI)](#6-mapa-de-responsabilidades-raci)  
7. [Cronograma macro](#7-cronograma-macro)  
8. [Indicadores de qualidade (KPIs + Gates)](#8-indicadores-de-qualidade-kpis--gates)  
9. [Metodologia](#9-metodologia)  
10. [Definições de Produto (resumo)](#10-definições-de-produto-resumo)  
11. [Anexos e referências rápidas](#11-anexos-e-referências-rápidas)

---

## 1) Escopo inicial de cada Sprint

### Sprint 0 — Planejamento e Setup (05–21/08) · **Entrega E1 em 21/08**
**Objetivo:** preparar produto, time e ambiente para desenvolvimento contínuo.  
**Entregáveis:**  
- **Definições de Produto:** personas, jornada, mapa de empatia, VPC; backlog MVP (user stories + critérios de aceite).  
- **Arquitetura:** monólito Spring Boot; Vue 3 (Vite) + Vuetify + Vuex + Vue Router; PostgreSQL + Flyway; Spring Security + JWT; execução local dockerizada.  
- **Ambiente & Ferramentas:** monorepo no GitHub; GitHub Actions mínimas (build/test); `docker-compose` esqueleto (FE/BE/DB).  
- **Design:** tokens e componentes-base no Figma; fluxos de login e CRUD Pets esboçados.  
- **Processo:** RACI, plano de comunicação, cerimônias, DoR/DoD.  
**DoD:** documento **E1** publicado (OneDrive); repositório criado; `docker-compose up` “hello world” no ar; backlog MVP priorizado.

### Sprint 1 — Autenticação + CRUD Pets (26/08–11/09) · **E2 em 11/09**
**Objetivo:** habilitar acesso seguro e primeiro fluxo fim-a-fim.  
**Entregáveis:**  
- **Segurança (DevOps owner):** login/logout/refresh **JWT**, RBAC, filtros/guards; documentação.  
- **CRUD Pets:** modelo, service, controller; validações; testes de integração (Testcontainers).  
- **Frontend:** Login & Dashboard; CRUD Pets em Vue/Vuetify (lista paginação, form validado).  
- **Dados:** migrações Flyway V1…Vn; índices/constraints essenciais.  
- **CI/CD:** pipelines consolidadas; badge no README.  
**DoD:** CRUD Pets operando via UI autenticada; testes na CI verdes; docs atualizadas.

### Sprint 2 — CRUD Adotantes + Regras & Observabilidade (16/09–02/10)
**Objetivo:** ampliar o domínio e base de operação.  
**Entregáveis:** CRUD Adotantes + relacionamento A↔B; erro padronizado; logs estruturados; **Actuator** e métricas básicas; incremento de cobertura.  
**DoD:** Pets e Adotantes via UI; health/logs ativos; critérios de aceite cumpridos.

### Sprint 3 — UX + Exportações + Performance (07/10–23/10) · **E3 em 23/10**
**Objetivo:** elevar experiência e completar relatórios/exportações.  
**Entregáveis:** Aplicação consistente do **Vuetify**; **CSV/PDF** respeitando filtros; otimizações SQL; início de carga local.  
**DoD:** exportações validadas; melhoria de latência em endpoints críticos.

### Sprint 4 — Qualidade total, Segurança & Release (28/10–25/11)
**Objetivo:** estabilizar, comprovar qualidade e entregar o produto final.  
**Entregáveis:** **E2E (Playwright)** nos fluxos críticos; hardening (CORS, headers, rate limit leve), **backup diário** + **restore testado**; UAT + manual do usuário + runbook; compose final; **tag v1.0**.  
**Gates:** **Code freeze 21/11** → **E4 25/11**.

---

## 2) Stack Tecnológico

### Frontend
- Vue 3 (Vite), **Vuetify**, Vue Router, Vuex.  
- Axios (interceptors p/ JWT).  
- Testes: Vitest + Vue Testing Library; **E2E:** Playwright.  
- ESLint + Prettier.

### Backend
- Java **17** + Spring Boot 3.x (Web, Validation, Data JPA, Actuator).  
- Testes: JUnit 5, Spring Boot Test, Mockito, **Testcontainers** (PostgreSQL).

### Segurança
- Spring Security + **JWT** (login, refresh, RBAC por roles).  
- BCrypt; CORS e headers; guarda/filters configurados; catálogo de rotas públicas/privadas; interceptors FE 401/403.  
- Auditoria mínima em operações sensíveis.

### Banco de Dados
- PostgreSQL; **Flyway**; HikariCP; FKs/índices/unique.

### Infra & DevOps
- Docker multi-stage (FE/BE); `docker-compose` (frontend, backend, postgres).  
- Observabilidade: Actuator + logs estruturados (JSON); Micrometer básico.  
- GitHub Actions: lint → build → tests (unit/integration/E2E) → cobertura → build de imagens → artefatos do compose.  
- Backups (S5+): `pg_dump` + restore documentado.

### Padrões & Documentação
- DoR/DoD; cobertura alvo 70%→80%; Conventional Commits; PR template com checklist de segurança/QA; README, runbook, ADRs.

---

## 3) Papéis do time & RACI

- **Lorran — FE & DevOps/Infra**: Vue/Vuetify, integração FE↔API; CI/CD, compose, observabilidade; **JWT** e RBAC.  
- **Miguel — Backend & Design**: arquitetura/padrões BE; revisão crítica; ADRs; coautor do design system.  
- **Kamilla — Backend**: **CRUD Pets** e testes BE.  
- **Enzo — Backend**: **CRUD Adotantes**, relatórios e otimizações BE.  
- **Lavinia — DB & Design**: modelagem, migrações, performance; co-líder de design.  
- **Pedro — Backend & QA/LGPD**: estratégia de QA (unit/integração/E2E), critérios de aceite, LGPD/conformidade e gates na CI.

---

## 4) Ferramentas de acompanhamento

| Ferramenta | Uso | Dono(s) | Evidências |
|---|---|---|---|
| **GitHub** (Issues, Milestones, PRs, Actions) | Planejamento/execução, revisão, CI | **Lorran**, **Pedro** | Issues com CA, Milestones S0–S4, PRs revisados, pipelines/cobertura |
| **WhatsApp** | Daily 09:15, bloqueios | todos | Decisões replicadas em Issues/OneDrive |
| **OneDrive** | Docs oficiais (E1–E4), atas, ADRs | Miguel, Lavinia, Pedro | PDFs/Docs versionados, links em Issues |
| **Figma** | Protótipos e design system | Lavinia (owner), Miguel (co) | Links de frames/tickets, handoff para FE |

**Regra:** decisões fora do GitHub devem ser replicadas na Issue correspondente.

---

## 5) Repositório (monorepo) e estrutura

**Um único repositório** com pastas `backend/` e `frontend/` e `docker-compose.yml` na raiz.

```
/repo/
|-- backend/
|   |-- src/
|   |-- pom.xml
|   `-- Dockerfile
|-- frontend/
|   |-- public/
|   |-- src/
|   |-- package.json
|   |-- nginx.conf
|   `-- Dockerfile
`-- docker-compose.yml
```

Execução rápida:  
1) Copie `.env.example` → `.env` na raiz.  
2) `docker compose up --build`  
3) FE em `http://localhost:8080` (nginx), BE em `http://localhost:8081` (configurável).

---

## 6) Mapa de responsabilidades (RACI)

**Legenda:** **A** = Accountable · **R** = Responsible · **C** = Consulted · **I** = Informed

| Entregável / Frente                                                                 | Lorran | Miguel | Kamilla | Enzo | Lavinia | Pedro |
|---|---:|---:|---:|---:|---:|---:|
| Arquitetura do backend & padrões                                                    | I | **A/R** | C | C | C | C |
| Frontend (Vue + Vuetify)                                                            | **A/R** | C | I | I | C | C |
| **Segurança** (Spring Security + **JWT**, RBAC, CORS/headers)                       | **A/R** | C | C | C | I | C |
| DevOps/Infra (compose, Actions, imagens, observabilidade)                           | **A/R** | I | I | I | I | C |
| CRUD Pets                                                                  | C | C | **A/R** | C | C | C |
| CRUD Adotantes                                                             | C | C | C | **A/R** | C | C |
| Relatórios/Exportações (CSV/PDF)                                                    | C | C | C | **A/R** | C | C |
| Banco de Dados (modelagem, migrações, índices)                                      | I | C | C | C | **A/R** | I |
| Observabilidade (Actuator, logs estruturados, métricas)                             | **A/R** | C | C | C | I | C |
| Backups & Restore                                                                   | **A/R** | I | I | I | C | C |
| Qualidade & Testes (unit, integração, E2E, cobertura, DoD)                          | C | C | R | R | I | **A/R** |
| Segurança & LGPD (políticas, auditoria mínima)                                      | C | C | C | C | C | **A/R** |
| Design System (tokens, componentes, A11y)                                           | C | **R** | I | I | **A** | C |
| Documentação técnica (READMEs, ADRs, runbooks)                                      | R | **A/R** | C | C | C | C |
| Empacotamento & Release (compose final, tag v1.0)                                   | **A/R** | C | C | C | C | C |

---

## 7) Cronograma macro

- **S0:** 05/08 → 21/08 · **E1 (21/08)**  
- **S1:** 26/08 → 11/09 · **E2 (11/09)**  
- **S2:** 16/09 → 02/10 · **Review/Planning S3 (02/10)**  
- **S3:** 07/10 → 23/10 · **E3 (23/10)**  
- **S4:** 28/10 → 25/11 · **Code freeze 21/11** · **E4 25/11** · apresentações adicionais 27/11  
- **Feriado:** 20/11 (sem aula).

---

## 8) Indicadores de qualidade (KPIs + Gates)

**Exemplos principais (metas por sprint):**
- **Taxa de aceite** (≥70% S1 → ≥90% S4).  
- **Cobertura**: BE ≥40/60/70/80% (S1→S4); FE ≥30/50/60/70%.  
- **Pass rate E2E**: — / — / ≥90% / ≥95%.  
- **Build sucesso**: ≥90/95/98/100%.  
- **Vulns altas/críticas**: 0 (a partir de S2).  
- **Latência p95 API**: ≤500ms (S2) → ≤300ms (S4).

**Quality Gates (CI):** build/test PASS; cobertura mínima da sprint; lint OK; E2E (S3+); 0 vulns altas/críticas; Flyway aplicado; 1 revisor BE+QA (BE) e revisão FE por Lorran.

**Severidade & SLAs:** P0 <24h · P1 ≤3 dias úteis · P2 próxima sprint.

---

## 9) Metodologia

- **Scrum enxuto** + práticas de **XP** (TDD quando viável, refatoração, code review).  
- **Cerimônias**: Planning (ter de abertura), Daily 09:15 WhatsApp, Review+Retro (no marco), Refinamento (qui/assíncrono).  
- **DoR/DoD** definidos; **ADRs** versionadas; mudanças de escopo com trade-off explícito.  
- **Branching**: GitHub Flow; SemVer; **tag v1.0** no release final.

---

## 10) Definições de Produto (resumo)

### Personas
- **Carla (29) — Adotante (primária):** quer compatibilidade com rotina/moradia; teme arrependimento; usa celular.  
- **Rafael (41) — Operador ONG:** quer cadastrar pets/triagens; sofre com retrabalho e dados dispersos.  
- **Lígia (35) — Gestora ONG:** quer indicadores de adoção/retorno; precisa de relatórios.

### Jornada (Carla)
Descoberta → Exploração → Seleção & **Match** → Contato & Adoção → Pós-adoção.  
**Oportunidades:** filtros claros, ficha padronizada, **relatório de compatibilidade** explicável, trilha guiada e acompanhamento.

### Mapa de Empatia (Carla)
**Vê:** perfis/filtros/score; **Ouve:** recomendações; **Pensa/Sente:** segurança vs arrependimento; **Fala/Faz:** compara e compartilha; **Dores/Ganhos:** incompatibilidade vs rapidez/clareza.

### Proposta de Valor
**Jobs:** escolher pet compatível e finalizar adoção. **Dores:** informação dispersa/tempo/risco. **Ganhos:** decisão segura, processo guiado.  
**Aliviadores/Geradores:** questionário estruturado, **score de compatibilidade** com justificativas, exportação/compartilhamento.

### User Stories (amostra + CA)
- **Auth (DevOps owner):** Como usuário, quero autenticar para acessar rotas protegidas. **CA:** dado credenciais válidas, quando POST `/auth/login`, então recebo **JWT** válido e acesso `/pets` (200).  
- **RBAC:** Como admin, quero restringir operações por papel. **CA:** usuários sem permissão recebem 403.  
- **CRUD Pet (A):** Como operador, quero manter pets. **CA:** validações; lista paginada/filtrada; edição/exclusão confirmada.  
- **CRUD Adotante (B):** Como operador, quero manter adotantes e questionário. **CA:** consentimento LGPD registrado.  
- **Relatório de compatibilidade:** Como adotante, quero um score explicável. **CA:** score + justificativas; exportável CSV/PDF.

---

## 11) Anexos e referências rápidas
- **.env.example** na raiz (variáveis do compose).  
- **ADR-001** (Arquitetura & Padrões).  
- **Runbook** (operação local, backup/restore).  
- **Manual do usuário** (fluxos principais).

> **Próximos passos:** criar Milestones S0–S4 no GitHub, subir monorepo, ligar Actions, importar Issues da S0/S1 e anexar este Charter no repositório.
