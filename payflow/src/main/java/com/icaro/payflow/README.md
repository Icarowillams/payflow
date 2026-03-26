# PayFlow

API RESTful de sistema financeiro para transferências entre usuários, com autenticação JWT e documentação Swagger.

## Tecnologias

- Java 17
- Spring Boot 3.4.3
- Spring Security + JWT (jjwt)
- Spring Data JPA
- PostgreSQL 15
- Lombok
- Bean Validation
- Docker & Docker Compose
- Swagger / OpenAPI (springdoc)
- JUnit 5 + Mockito

## Arquitetura

```
src/main/java/com/icaro/payflow/
├── config/          # Configurações (Security, Password)
├── controller/      # Endpoints REST
├── dto/             # Objetos de transferência
├── entity/          # Entidades JPA
├── exception/       # Tratamento global de erros
├── repository/      # Acesso a dados
├── security/        # JWT (Filter + Service)
└── service/         # Regras de negócio
```

## Endpoints

### Auth
| Método | Rota           | Descrição          | Acesso  |
|--------|----------------|--------------------|---------|
| POST   | `/auth/login`  | Login (retorna JWT)| Público |

### Usuários
| Método | Rota       | Descrição               | Acesso      |
|--------|------------|--------------------------|-------------|
| POST   | `/users`   | Criar usuário            | Público     |
| GET    | `/users/me`| Dados do usuário logado  | Autenticado |

### Transações
| Método | Rota                              | Descrição              | Acesso      |
|--------|-----------------------------------|------------------------|-------------|
| POST   | `/transactions`                   | Realizar transferência | Autenticado |
| GET    | `/transactions/history`           | Histórico de transações| Autenticado |
| GET    | `/transactions/history/paginated` | Histórico paginado     | Autenticado |

## Como rodar

### Pré-requisitos
- Docker e Docker Compose instalados

### Subir o projeto
```bash
docker-compose up --build
```

A API estará disponível em `http://localhost:8081`

### Swagger UI
Acesse a documentação interativa em:
```
http://localhost:8081/swagger-ui.html
```

### Rodar sem Docker
```bash
# 1. Suba o PostgreSQL (ou use o docker-compose só pro banco)
docker-compose up postgres

# 2. Rode a aplicação
./mvnw spring-boot:run
```

##  Testes

```bash
./mvnw test
```

Testes unitários com Mockito cobrindo:
- **AuthServiceTest** — login com sucesso, email não encontrado, senha incorreta
- **UserServiceTest** — criação de usuário, email duplicado
- **TransactionServiceTest** — transferência com sucesso, saldo insuficiente, transferência para si mesmo, destinatário não encontrado, histórico

## Autenticação

A API utiliza **JWT (JSON Web Token)**. Para acessar endpoints protegidos:

1. Crie um usuário via `POST /users`
2. Faça login via `POST /auth/login`
3. Use o token retornado no header:
```
Authorization: Bearer <seu-token>
```

## Licença

Este projeto é de uso pessoal para fins de portfólio.

---

Desenvolvido por **Ícaro** 
