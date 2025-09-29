# projeto-denis-ntt-api
Projeto bancário com serviços API rest: CQRS, H2 (in-memory) para write model, Redis para cache (read), JWT + Spring Security, validacao de CPF, historico de transacoes, juros em depositos para dividas.

## Requisitos para rodar
- Java 17
- Maven
- Redis (local) — pode rodar via Docker: `docker run -p 6379:6379 redis`

## Como rodar
1. `mvn clean package`
2. `mvn spring-boot:run`

API endpoints:
- POST /api/auth/register {fullName, cpf, login, password}
- POST /api/auth/login {login, password} -> {token}
- POST /api/account/deposit {amount} (Bearer token)
- POST /api/account/pay {amount} (Bearer token)
- GET  /api/account/balance (Bearer token)