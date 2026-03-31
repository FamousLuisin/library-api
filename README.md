# library
O objetivo do projeto é demonstrar boas práticas de desenvolvimento backend com Java e Spring.

---

# Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Maven
- Docker / Docker Compose
- Banco de dados relacional (postgreSQL)
- JUnit / Testes de integração
- OpenAPI / Swagger

---

# Arquitetura do projeto

A aplicação segue uma arquitetura em camadas:
- controller -> exposição da API
- service -> regras de negócio
- repository -> acesso ao banco
- model -> entidades
- dto -> transporte de dados
- mapper -> mapeamento de entidades
- file -> importação e exportação de arquivos csv e xlsx


Essa separação melhora a organização do código e facilita testes e manutenção.

---

# Como executar o projeto

## 1. Clonar o repositório

```bash
git clone https://github.com/FamousLuisin/library-api.git
cd library-api
```

## 2. Subir o banco
```bash
docker compose up -d
```

## 3. Rodar os testes
```bash
./mvnw test
```

## 4. Rodar o codigo
```bash
./mvnw spring-boot:run
```

---

# Swagger

O swagger do codigo esta disponivel no http://localhost:8080/swagger-ui/index.html.

---

# Melhorias

- Autenticação com JWT
- Cache
- Monitoramento
- CI/CD
- Processamento Batch
- Subir em Cloud
