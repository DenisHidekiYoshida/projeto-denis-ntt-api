# projeto-denis-ntt-api
Projeto bancário com serviços API rest: CQRS, H2 (in-memory) para write model, Redis para cache (read), JWT + Spring Security, validacao de CPF, historico de transacoes, juros em depositos para dividas.

## Requisitos para rodar
- Java 17
- Maven
- Lombok
- Redis (local) — pode rodar via Docker: `docker run -p 6379:6379 redis`

## Como rodar
1. `mvn clean package`
2. `mvn spring-boot:run`
3. http://localhost:8080/swagger-ui/index.html

API endpoints:
- POST /api/auth/register {fullName, cpf, login, password}
- POST /api/auth/login {login, password} -> {token}
- POST /api/account/deposit {amount} (Bearer token)
- POST /api/account/payment {description, amount} (Bearer token)
- GET  /api/account/balance (Bearer token)

### Sequencia de execucao das API's
1. Registrar usuario: /api/auth/register
2. Realizar o Login e com o Token gerado, autorizar no swagger
3. Com o Token autorizado realizar o pagamento e o deposito, em seguida consultar o balanco com historico