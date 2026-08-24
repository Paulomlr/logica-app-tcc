# App de Ensino de Lógica Aplicada / Tabela Verdade (TCC)

PWA para apoiar o ensino de lógica proposicional e tabela verdade, desenvolvido como Trabalho de Conclusão de Curso (UNIFESSPA).

## Stack

- **Backend**: Spring Boot 3 (Java 21) + PostgreSQL + Spring Security (OAuth2 / login Google)
- **Frontend**: React + TypeScript + Vite, empacotado como PWA (`vite-plugin-pwa`)
- **Ambiente de desenvolvimento**: Docker Compose (não requer Node/Maven instalados localmente)

## Requisitos

- Docker Desktop rodando

## Como rodar

```bash
docker compose up
```

- Backend: http://localhost:8080
- Frontend: http://localhost:5173
- PostgreSQL: localhost:5432 (usuário/senha/db: `logica_app`)

## Estrutura

```
backend/    # API Spring Boot
frontend/   # PWA React + TypeScript
docker-compose.yml
```

## Origem dos requisitos

O escopo funcional foi levantado a partir de um questionário respondido por estudantes da disciplina de Lógica Aplicada à Computação (n=9). Principais direcionadores: forte preferência por gamificação e feedback imediato, uso mobile-first com funcionalidades offline, e interface simples/sem poluição visual.
