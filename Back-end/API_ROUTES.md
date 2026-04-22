# API Routes Documentation

This document describes the routes currently implemented in the `Back-end` application, how to access them, what each route expects, and what it returns.

## Overview

- Base URL: `http://localhost:8080`
- Content type for requests with body: `application/json`
- Authentication: none required right now
- CORS: enabled
- Database: MySQL `gerenciamento_financeiro`

## Access

The current security configuration allows every route without authentication.

Because of that, requests can be made directly with tools such as:

- `curl`
- Postman
- Insomnia
- frontend `fetch`
- Axios

Example:

```bash
curl http://localhost:8080/usuarios
```

## Standard Error Response

The application now uses a global error handler. Errors are returned in this format:

```json
{
  "timestamp": "2026-04-21T19:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "email e obrigatorio",
  "path": "/usuarios"
}
```

### Main status mappings

- `400 Bad Request`: validation errors or invalid request body
- `404 Not Found`: entity not found
- `409 Conflict`: business rule violations such as insufficient balance or monthly limit reached
- `500 Internal Server Error`: database or unexpected server error

## Accepted Enum Values

### `moeda`

- `BRL`
- `USD`
- `EUR`

### `receita`

- `SALARIO`
- `FREELANCE`
- `INVESTIMENTO`
- `ALUGUEL`
- `OUTROS`

### `categoria`

- `ALIMENTACAO`
- `TRANSPORTE`
- `SAUDE`
- `EDUCACAO`
- `LAZER`
- `MORADIA`
- `OUTROS`

### `formaPagamento`

- `DINHEIRO`
- `CARTAO_CREDITO`
- `CARTAO_DEBITO`
- `PIX`
- `TRANSFERENCIA`
- `BOLETO`

## Seed Data IDs

These IDs already exist in `db/init.sql` and are useful for testing:

### Users

- Gabriel: `11111111-1111-1111-1111-111111111111`
- Mariana: `22222222-2222-2222-2222-222222222222`

### Accounts

- Gabriel BRL: `aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1`
- Gabriel USD: `aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2`
- Mariana BRL: `bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1`

### Metas

- Gabriel reserva: `ccccccc1-cccc-cccc-cccc-ccccccccccc1`
- Gabriel viagem: `ccccccc2-cccc-cccc-cccc-ccccccccccc2`
- Mariana apartamento: `ccccccc3-cccc-cccc-cccc-ccccccccccc3`

---

## Users

### `POST /usuarios`

Creates a user.

### Request body

```json
{
  "nome": "Joao Silva",
  "cpf": "111.222.333-44",
  "email": "joao@email.com",
  "senha": "123456",
  "celular": "(11) 99999-1111",
  "telefone": "(11) 3333-1111",
  "cargo": "Analista",
  "salario": 4500.0
}
```

### Required fields

- `nome`
- `cpf`
- `email`
- `senha`
- `salario`

### Success response

- Status: `201 Created`

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-1234567890ef",
  "nome": "Joao Silva",
  "cpf": "111.222.333-44",
  "email": "joao@email.com",
  "senha": "123456",
  "celular": "(11) 99999-1111",
  "telefone": "(11) 3333-1111",
  "cargo": "Analista",
  "salario": 4500.0
}
```

### Main errors

- `nome e obrigatorio`
- `cpf e obrigatorio`
- `email e obrigatorio`
- `senha e obrigatoria`
- `salario invalido`

### Example request

```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Joao Silva\",\"cpf\":\"111.222.333-44\",\"email\":\"joao@email.com\",\"senha\":\"123456\",\"celular\":\"(11) 99999-1111\",\"telefone\":\"(11) 3333-1111\",\"cargo\":\"Analista\",\"salario\":4500.0}"
```

### `GET /usuarios`

Lists all users.

### Success response

- Status: `200 OK`

```json
[
  {
    "id": "11111111-1111-1111-1111-111111111111",
    "nome": "Gabriel Silva",
    "cpf": "123.456.789-00",
    "email": "gabriel.silva@email.com",
    "senha": "123456",
    "celular": "(11) 98888-1111",
    "telefone": "(11) 3333-1111",
    "cargo": "Software Engineer",
    "salario": 8500.0
  }
]
```

### `GET /usuarios/{id}`

Fetches a user by ID.

### Path params

- `id`: user ID

### Success response

- Status: `200 OK`

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "nome": "Gabriel Silva",
  "cpf": "123.456.789-00",
  "email": "gabriel.silva@email.com",
  "senha": "123456",
  "celular": "(11) 98888-1111",
  "telefone": "(11) 3333-1111",
  "cargo": "Software Engineer",
  "salario": 8500.0
}
```

### Main errors

- `id e obrigatorio`
- `Usuario nao encontrado`

### `PATCH /usuarios/{id}`

Updates a user. All fields are optional, but when sent they must be valid.

### Path params

- `id`: user ID

### Request body

```json
{
  "nome": "Gabriel Silva Junior",
  "cpf": "123.456.789-00",
  "email": "gabrieljr@email.com",
  "senha": "novaSenha123",
  "celular": "(11) 97777-9999",
  "telefone": "(11) 3333-0000",
  "cargo": "Senior Software Engineer",
  "salario": 9800.0
}
```

### Success response

- Status: `200 OK`

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "nome": "Gabriel Silva Junior",
  "cpf": "123.456.789-00",
  "email": "gabrieljr@email.com",
  "senha": "novaSenha123",
  "celular": "(11) 97777-9999",
  "telefone": "(11) 3333-0000",
  "cargo": "Senior Software Engineer",
  "salario": 9800.0
}
```

### Main errors

- `id e obrigatorio`
- `Usuario nao encontrado`
- `nome e obrigatorio`
- `cpf e obrigatorio`
- `email e obrigatorio`
- `senha e obrigatoria`
- `salario invalido`

### `DELETE /usuarios/{id}`

Deletes a user.

### Path params

- `id`: user ID

### Success response

- Status: `204 No Content`
- Response body: empty

### Main errors

- `id e obrigatorio`
- `Usuario nao encontrado`

---

## Accounts

### `POST /contas`

Creates an account for an existing user.

### Request body

```json
{
  "usuarioId": "11111111-1111-1111-1111-111111111111",
  "moeda": "BRL",
  "saldoAtual": 1500.0,
  "limiteGastoMensal": 800.0,
  "descricao": "Conta principal"
}
```

### Required fields

- `usuarioId`
- `moeda`
- `saldoAtual`
- `limiteGastoMensal`

### Success response

- Status: `201 Created`

```json
{
  "id": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "usuarioId": "11111111-1111-1111-1111-111111111111",
  "moeda": "BRL",
  "saldoAtual": 1500.0,
  "despesaMensal": 0.0,
  "limiteGastoMensal": 800.0,
  "descricao": "Conta principal",
  "dataCriacao": "2026-04-21",
  "dataAtualizacao": "2026-04-21"
}
```

### Main errors

- `usuarioId e obrigatorio`
- `moeda e obrigatoria`
- `saldoAtual e obrigatorio`
- `limiteGastoMensal e obrigatorio`
- `Usuario nao encontrado`

### Example request

```bash
curl -X POST http://localhost:8080/contas \
  -H "Content-Type: application/json" \
  -d "{\"usuarioId\":\"11111111-1111-1111-1111-111111111111\",\"moeda\":\"BRL\",\"saldoAtual\":1500.0,\"limiteGastoMensal\":800.0,\"descricao\":\"Conta principal\"}"
```

### `GET /contas`

Lists all accounts. It can also filter by `usuarioId`.

### Query params

- Optional: `usuarioId`

### Example URLs

- `GET /contas`
- `GET /contas?usuarioId=11111111-1111-1111-1111-111111111111`

### Success response

- Status: `200 OK`

```json
[
  {
    "id": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
    "usuarioId": "11111111-1111-1111-1111-111111111111",
    "moeda": "BRL",
    "saldoAtual": 5730.0,
    "despesaMensal": 1270.0,
    "limiteGastoMensal": 4000.0,
    "descricao": "Conta principal do Gabriel",
    "dataCriacao": "2026-04-01",
    "dataAtualizacao": "2026-04-21"
  }
]
```

### `GET /contas/{id}`

Fetches an account by ID.

### Path params

- `id`: account ID

### Success response

- Status: `200 OK`

```json
{
  "id": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "usuarioId": "11111111-1111-1111-1111-111111111111",
  "moeda": "BRL",
  "saldoAtual": 5730.0,
  "despesaMensal": 1270.0,
  "limiteGastoMensal": 4000.0,
  "descricao": "Conta principal do Gabriel",
  "dataCriacao": "2026-04-01",
  "dataAtualizacao": "2026-04-21"
}
```

### Main errors

- `id e obrigatorio`
- `Conta nao encontrada`

### `PATCH /contas/{id}`

Updates an account. Only `limiteGastoMensal` and `descricao` can be changed by this route.

### Path params

- `id`: account ID

### Request body

```json
{
  "limiteGastoMensal": 4500.0,
  "descricao": "Conta principal atualizada"
}
```

### Success response

- Status: `200 OK`

```json
{
  "id": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "usuarioId": "11111111-1111-1111-1111-111111111111",
  "moeda": "BRL",
  "saldoAtual": 5730.0,
  "despesaMensal": 1270.0,
  "limiteGastoMensal": 4500.0,
  "descricao": "Conta principal atualizada",
  "dataCriacao": "2026-04-01",
  "dataAtualizacao": "2026-04-21"
}
```

### Main errors

- `id e obrigatorio`
- `Conta nao encontrada`

### `DELETE /contas/{id}`

Deletes an account.

### Path params

- `id`: account ID

### Success response

- Status: `204 No Content`
- Response body: empty

### Main errors

- `id e obrigatorio`
- `Conta nao encontrada`

---

## Metas

Meta routes are nested under an account and perform a simple CRUD.

The meta currency is always the same as the account currency.

### `POST /contas/{contaId}/metas`

Creates a meta for an account.

### Path params

- `contaId`: account ID

### Request body

```json
{
  "nome": "Reserva de emergencia",
  "valorObjetivo": 10000.0,
  "cargo": "Seguranca",
  "dataInicio": "2026-04-01",
  "dataDeConclusao": "2026-12-31"
}
```

### Required fields

- `nome`
- `valorObjetivo` and it must be greater than `0`

### Success response

- Status: `201 Created`

```json
{
  "id": "ccccccc1-cccc-cccc-cccc-ccccccccccc1",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "nome": "Reserva de emergencia",
  "valorObjetivo": 10000.0,
  "moeda": "BRL",
  "cargo": "Seguranca",
  "dataInicio": "2026-04-01",
  "dataDeConclusao": "2026-12-31"
}
```

### Main errors

- `contaId e obrigatorio`
- `Conta nao encontrada`
- `nome e obrigatorio`
- `valorObjetivo deve ser maior que zero`
- `dataDeConclusao nao pode ser anterior a dataInicio`

### `GET /contas/{contaId}/metas`

Lists all metas for an account.

### Path params

- `contaId`: account ID

### Success response

- Status: `200 OK`

```json
[
  {
    "id": "ccccccc1-cccc-cccc-cccc-ccccccccccc1",
    "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
    "nome": "Reserva de emergencia",
    "valorObjetivo": 10000.0,
    "moeda": "BRL",
    "cargo": "Seguranca",
    "dataInicio": "2026-04-01",
    "dataDeConclusao": "2026-12-31"
  }
]
```

### Main errors

- `contaId e obrigatorio`
- `Conta nao encontrada`

### `GET /contas/{contaId}/metas/{metaId}`

Fetches a meta by ID inside an account.

### Path params

- `contaId`: account ID
- `metaId`: meta ID

### Success response

- Status: `200 OK`

```json
{
  "id": "ccccccc1-cccc-cccc-cccc-ccccccccccc1",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "nome": "Reserva de emergencia",
  "valorObjetivo": 10000.0,
  "moeda": "BRL",
  "cargo": "Seguranca",
  "dataInicio": "2026-04-01",
  "dataDeConclusao": "2026-12-31"
}
```

### Main errors

- `contaId e obrigatorio`
- `metaId e obrigatorio`
- `Conta nao encontrada`
- `Meta nao encontrada`

### `PATCH /contas/{contaId}/metas/{metaId}`

Updates a meta. All fields are optional, but when sent they must be valid.

### Path params

- `contaId`: account ID
- `metaId`: meta ID

### Request body

```json
{
  "nome": "Reserva ampliada",
  "valorObjetivo": 15000.0,
  "cargo": "Planejamento",
  "dataInicio": "2026-04-01",
  "dataDeConclusao": "2027-03-31"
}
```

### Success response

- Status: `200 OK`

```json
{
  "id": "ccccccc1-cccc-cccc-cccc-ccccccccccc1",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "nome": "Reserva ampliada",
  "valorObjetivo": 15000.0,
  "moeda": "BRL",
  "cargo": "Planejamento",
  "dataInicio": "2026-04-01",
  "dataDeConclusao": "2027-03-31"
}
```

### Main errors

- `contaId e obrigatorio`
- `metaId e obrigatorio`
- `Conta nao encontrada`
- `Meta nao encontrada`
- `nome e obrigatorio`
- `valorObjetivo deve ser maior que zero`
- `dataDeConclusao nao pode ser anterior a dataInicio`

### `DELETE /contas/{contaId}/metas/{metaId}`

Deletes a meta.

### Path params

- `contaId`: account ID
- `metaId`: meta ID

### Success response

- Status: `204 No Content`
- Response body: empty

### Main errors

- `contaId e obrigatorio`
- `metaId e obrigatorio`
- `Conta nao encontrada`
- `Meta nao encontrada`

---

## Deposits

### `POST /transacoes/depositos`

Creates a deposit for an account.

If the deposit currency is different from the account currency, the backend uses the exchange API to convert the value before updating the account balance.

Important detail:

- the deposit record keeps the original submitted currency
- the account balance is updated in the account currency after conversion

### Request body

```json
{
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "valor": 250.0,
  "moeda": "BRL",
  "origem": "Empresa X",
  "receita": "SALARIO",
  "descricao": "Pagamento mensal"
}
```

### Required fields

- `contaId`
- `valor` and it must be greater than `0`
- `moeda`
- `origem`
- `receita`

### Success response

- Status: `201 Created`

```json
{
  "id": "5fc9c4c9-3c58-4ed8-a4d3-b822af95e5b7",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "valor": 250.0,
  "moeda": "BRL",
  "origem": "Empresa X",
  "receita": "SALARIO",
  "data": "2026-04-21",
  "descricao": "Pagamento mensal"
}
```

### Main errors

- `contaId e obrigatorio`
- `valor deve ser maior que zero`
- `moeda e obrigatoria`
- `origem e obrigatoria`
- `receita e obrigatoria`
- `Conta nao encontrada`

### Example request

```bash
curl -X POST http://localhost:8080/transacoes/depositos \
  -H "Content-Type: application/json" \
  -d "{\"contaId\":\"aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1\",\"valor\":250.0,\"moeda\":\"BRL\",\"origem\":\"Empresa X\",\"receita\":\"SALARIO\",\"descricao\":\"Pagamento mensal\"}"
```

### `GET /transacoes/depositos`

Lists all deposits. It can also filter by `contaId`.

### Query params

- Optional: `contaId`

### Example URLs

- `GET /transacoes/depositos`
- `GET /transacoes/depositos?contaId=aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1`

### Success response

- Status: `200 OK`

```json
[
  {
    "id": "ddddddd1-dddd-dddd-dddd-ddddddddddd1",
    "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
    "valor": 5000.0,
    "moeda": "BRL",
    "origem": "Tech Solutions",
    "receita": "SALARIO",
    "data": "2026-04-05",
    "descricao": "Salario de abril"
  }
]
```

### `GET /transacoes/depositos/{id}`

Fetches a deposit by ID.

### Path params

- `id`: deposit ID

### Success response

- Status: `200 OK`

```json
{
  "id": "ddddddd1-dddd-dddd-dddd-ddddddddddd1",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "valor": 5000.0,
  "moeda": "BRL",
  "origem": "Tech Solutions",
  "receita": "SALARIO",
  "data": "2026-04-05",
  "descricao": "Salario de abril"
}
```

### Main errors

- `id e obrigatorio`
- `Deposito nao encontrado`

---

## Withdrawals

### `POST /transacoes/saques`

Creates a withdrawal for an account.

If the withdrawal currency is different from the account currency, the backend tries to convert the value before validating balance and monthly limit.

This route also updates:

- the current account balance
- the monthly expense total of the account

### Request body

```json
{
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "valor": 120.0,
  "moeda": "BRL",
  "destino": "Supermercado",
  "formaPagamento": "PIX",
  "categoria": "ALIMENTACAO",
  "descricao": "Compras do mes",
  "observacao": "Compra semanal"
}
```

### Required fields

- `contaId`
- `valor` and it must be greater than `0`
- `moeda`
- `destino`
- `formaPagamento`
- `categoria`

### Success response

- Status: `201 Created`

```json
{
  "id": "f2e4c735-f0a6-47ca-8c62-5aab389ecf89",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "valor": 120.0,
  "moeda": "BRL",
  "destino": "Supermercado",
  "categoria": "ALIMENTACAO",
  "formaPagamento": "PIX",
  "data": "2026-04-21",
  "descricao": "Compras do mes",
  "observacao": "Compra semanal"
}
```

### Main errors

- `contaId e obrigatorio`
- `valor deve ser maior que zero`
- `moeda e obrigatoria`
- `formaPagamento e obrigatorio`
- `categoria e obrigatoria`
- `destino e obrigatorio`
- `Conta nao encontrada`
- `Saldo insuficiente. Disponivel: ...`
- `Limite mensal atingido. Limite: ...`

### Example request

```bash
curl -X POST http://localhost:8080/transacoes/saques \
  -H "Content-Type: application/json" \
  -d "{\"contaId\":\"aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1\",\"valor\":120.0,\"moeda\":\"BRL\",\"destino\":\"Supermercado\",\"formaPagamento\":\"PIX\",\"categoria\":\"ALIMENTACAO\",\"descricao\":\"Compras do mes\",\"observacao\":\"Compra semanal\"}"
```

### `GET /transacoes/saques`

Lists all withdrawals. It can also filter by `contaId`.

### Query params

- Optional: `contaId`

### Example URLs

- `GET /transacoes/saques`
- `GET /transacoes/saques?contaId=aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1`

### Success response

- Status: `200 OK`

```json
[
  {
    "id": "sssssss1-ssss-ssss-ssss-sssssssssss1",
    "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
    "valor": 950.0,
    "moeda": "BRL",
    "destino": "Imobiliaria Centro",
    "categoria": "MORADIA",
    "formaPagamento": "TRANSFERENCIA",
    "data": "2026-04-08",
    "descricao": "Aluguel do apartamento",
    "observacao": "Pagamento referente ao mes de abril"
  }
]
```

### `GET /transacoes/saques/{id}`

Fetches a withdrawal by ID.

### Path params

- `id`: withdrawal ID

### Success response

- Status: `200 OK`

```json
{
  "id": "sssssss1-ssss-ssss-ssss-sssssssssss1",
  "contaId": "aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1",
  "valor": 950.0,
  "moeda": "BRL",
  "destino": "Imobiliaria Centro",
  "categoria": "MORADIA",
  "formaPagamento": "TRANSFERENCIA",
  "data": "2026-04-08",
  "descricao": "Aluguel do apartamento",
  "observacao": "Pagamento referente ao mes de abril"
}
```

### Main errors

- `id e obrigatorio`
- `Saque nao encontrado`

---

## Route Summary

| Method | Route | Description |
|---|---|---|
| `POST` | `/usuarios` | Create user |
| `GET` | `/usuarios` | List users |
| `GET` | `/usuarios/{id}` | Get user by ID |
| `PATCH` | `/usuarios/{id}` | Update user |
| `DELETE` | `/usuarios/{id}` | Delete user |
| `POST` | `/contas` | Create account |
| `GET` | `/contas` | List accounts |
| `GET` | `/contas/{id}` | Get account by ID |
| `PATCH` | `/contas/{id}` | Update account |
| `DELETE` | `/contas/{id}` | Delete account |
| `POST` | `/contas/{contaId}/metas` | Create meta for account |
| `GET` | `/contas/{contaId}/metas` | List metas for account |
| `GET` | `/contas/{contaId}/metas/{metaId}` | Get meta by ID |
| `PATCH` | `/contas/{contaId}/metas/{metaId}` | Update meta |
| `DELETE` | `/contas/{contaId}/metas/{metaId}` | Delete meta |
| `POST` | `/transacoes/depositos` | Create deposit |
| `GET` | `/transacoes/depositos` | List deposits |
| `GET` | `/transacoes/depositos/{id}` | Get deposit by ID |
| `POST` | `/transacoes/saques` | Create withdrawal |
| `GET` | `/transacoes/saques` | List withdrawals |
| `GET` | `/transacoes/saques/{id}` | Get withdrawal by ID |

## Notes

- Passwords are stored and returned as plain text because this project is currently a simulation.
- `GET /contas` supports filtering by `usuarioId`, but there is no separate route `/usuarios/{usuarioId}/contas` in the current backend.
- `moeda` values are normalized to uppercase internally.
- `receita`, `categoria`, and `formaPagamento` should use the exact enum names listed above.
- Meta values use the account currency automatically.
- Deposit and withdrawal conversions use the external exchange API before changing the account balance.
