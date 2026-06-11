# Back-end do Gerenciamento Financeiro

API do projeto de gerenciamento financeiro desenvolvida com Spring Boot. Ela cuida da parte de usuarios, contas, metas e transacoes financeiras.

## O que essa API faz

- Cadastra, lista, atualiza e remove usuarios
- Cria e gerencia contas financeiras
- Cria e acompanha metas por conta
- Registra depositos e saques
- Atualiza saldo, despesa mensal e valida regras de negocio
- Padroniza as respostas de erro

## Tecnologias principais

- Java 17
- Spring Boot 3
- Maven
- MySQL
- Docker e Docker Compose opcionais para subir o ambiente

## Requisitos

- JDK 17
- Maven
- MySQL rodando localmente, ou Docker instalado

## Como executar

### Opcao 1: rodando localmente

1. Entre na pasta do back-end:
   ```bash
   cd Back-end
   ```
2. Verifique a conexao com o banco em `src/main/resources/application.properties`.
3. Execute a aplicacao:
   ```bash
   mvn spring-boot:run
   ```

A API fica disponivel em `http://localhost:8080`.

### Opcao 2: usando Docker Compose

Dentro da pasta `Back-end`, rode:

```bash
docker compose up --build
```

Isso sobe o banco MySQL e a aplicacao juntos.

## Configuracao do banco

Por padrao, a aplicacao usa:

- Banco: `gerenciamento_financeiro`
- Usuario: `umc`
- Senha: `umc123`

Esses dados estao em `Back-end/src/main/resources/application.properties` para execucao local.

## Principais rotas

### Usuarios

- `POST /usuarios`
- `GET /usuarios`
- `GET /usuarios/{id}`
- `PATCH /usuarios/{id}`
- `DELETE /usuarios/{id}`

### Contas

- `POST /contas`
- `GET /contas`
- `GET /contas/{id}`
- `PATCH /contas/{id}`
- `DELETE /contas/{id}`

### Metas

- `POST /contas/{contaId}/metas`
- `GET /contas/{contaId}/metas`
- `GET /contas/{contaId}/metas/{metaId}`
- `PATCH /contas/{contaId}/metas/{metaId}`
- `DELETE /contas/{contaId}/metas/{metaId}`

### Depositos

- `POST /transacoes/depositos`
- `GET /transacoes/depositos`
- `GET /transacoes/depositos/{id}`

### Saques

- `POST /transacoes/saques`
- `GET /transacoes/saques`
- `GET /transacoes/saques/{id}`

## Regras importantes

- Nao ha autenticacao ativa no momento; as rotas estao liberadas.
- O CORS ja esta configurado para o frontend local em `http://localhost:3000` e `http://127.0.0.1:3000`.
- As respostas de erro seguem um formato padronizado com status, mensagem e caminho da rota.
- Depositos e saques podem fazer conversao de moeda quando necessario.
- Saques validam saldo disponivel e limite mensal da conta.


