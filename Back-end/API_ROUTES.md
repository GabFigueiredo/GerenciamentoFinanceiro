# API Routes Documentation

This document describes the routes currently implemented in the application, how to call them, the request formats, the response formats, and the main error cases based on the current backend code.

## Overview

- Base URL: `http://localhost:8080`
- Content type for requests with body: `application/json`
- CORS: enabled for any origin, header, and method
- Database configured in the app: MySQL `gerenciamento_financeiro`

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

## Common Notes

- IDs are sent and returned as strings.
- Dates are returned as `YYYY-MM-DD`.
- `moeda` is normalized with `toUpperCase()`, so values like `brl` also work.
- `receita`, `categoria`, and `formaPagamento` are converted to enums internally and should be sent using the values listed above.
- There is no custom global exception handler in the project right now. Because of that, invalid requests and uncaught exceptions will most likely return Spring Boot's default error response.

Typical error situations:

- `400 Bad Request`: invalid or missing required data
- `404 Not Found`: entity not found
- `500 Internal Server Error`: unexpected server error or enum conversion failure not handled explicitly

---

## Accounts

### `POST /contas`

Creates a new account.

#### Request body

```json
{
  "usuarioId": "4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44",
  "moeda": "BRL",
  "saldoAtual": 1500.0,
  "limiteGastoMensal": 800.0,
  "descricao": "Conta principal"
}
```

#### Required fields

- `usuarioId`
- `moeda`
- `saldoAtual`
- `limiteGastoMensal`

#### Success response

- Status: `201 Created`

```json
{
  "id": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "usuarioId": "4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44",
  "moeda": "BRL",
  "saldoAtual": 1500.0,
  "despesaMensal": 0.0,
  "limiteGastoMensal": 800.0,
  "descricao": "Conta principal",
  "dataCriacao": "2026-04-21",
  "dataAtualizacao": "2026-04-21"
}
```

#### Main errors

- `usuarioId e obrigatorio`
- `moeda e obrigatoria`
- `saldoAtual e obrigatorio`
- `limiteGastoMensal e obrigatorio`
- `Usuario nao encontrado`

#### Example request

```bash
curl -X POST http://localhost:8080/contas \
  -H "Content-Type: application/json" \
  -d "{\"usuarioId\":\"4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44\",\"moeda\":\"BRL\",\"saldoAtual\":1500.0,\"limiteGastoMensal\":800.0,\"descricao\":\"Conta principal\"}"
```

### `GET /contas`

Lists all accounts. It can also filter by `usuarioId`.

#### Query params

- Optional: `usuarioId`

#### Example URLs

- `GET /contas`
- `GET /contas?usuarioId=4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44`

#### Success response

- Status: `200 OK`

```json
[
  {
    "id": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
    "usuarioId": "4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44",
    "moeda": "BRL",
    "saldoAtual": 1500.0,
    "despesaMensal": 120.0,
    "limiteGastoMensal": 800.0,
    "descricao": "Conta principal",
    "dataCriacao": "2026-04-21",
    "dataAtualizacao": "2026-04-21"
  }
]
```

### `GET /contas/{id}`

Fetches an account by ID.

#### Path params

- `id`: account ID

#### Success response

- Status: `200 OK`

```json
{
  "id": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "usuarioId": "4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44",
  "moeda": "BRL",
  "saldoAtual": 1500.0,
  "despesaMensal": 120.0,
  "limiteGastoMensal": 800.0,
  "descricao": "Conta principal",
  "dataCriacao": "2026-04-21",
  "dataAtualizacao": "2026-04-21"
}
```

#### Main errors

- `id e obrigatorio`
- `Conta nao encontrada`

### `PATCH /contas/{id}`

Updates an account. Only `limiteGastoMensal` and `descricao` can be changed by this route.

#### Path params

- `id`: account ID

#### Request body

All fields are optional.

```json
{
  "limiteGastoMensal": 1200.0,
  "descricao": "Conta atualizada"
}
```

#### Success response

- Status: `200 OK`

```json
{
  "id": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "usuarioId": "4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44",
  "moeda": "BRL",
  "saldoAtual": 1500.0,
  "despesaMensal": 120.0,
  "limiteGastoMensal": 1200.0,
  "descricao": "Conta atualizada",
  "dataCriacao": "2026-04-21",
  "dataAtualizacao": "2026-04-21"
}
```

#### Main errors

- `id e obrigatorio`
- `Conta nao encontrada`

### `DELETE /contas/{id}`

Deletes an account.

#### Path params

- `id`: account ID

#### Success response

- Status: `204 No Content`
- Response body: empty

#### Main errors

- `id e obrigatorio`
- `Conta nao encontrada`

### `GET /usuarios/{usuarioId}/contas`

Lists all accounts for a specific user.

#### Path params

- `usuarioId`: user ID

#### Success response

- Status: `200 OK`

```json
[
  {
    "id": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
    "usuarioId": "4c89b50c-1df5-4d26-8f2c-5b6d4f9d9c44",
    "moeda": "BRL",
    "saldoAtual": 1500.0,
    "despesaMensal": 120.0,
    "limiteGastoMensal": 800.0,
    "descricao": "Conta principal",
    "dataCriacao": "2026-04-21",
    "dataAtualizacao": "2026-04-21"
  }
]
```

#### Main errors

- `usuarioId e obrigatorio`
- `Usuario nao encontrado`

---

## Deposits

### `POST /transacoes/depositos`

Creates a deposit for an account.

If the deposit currency is different from the account currency, the backend attempts to convert the value using the exchange API before updating the account balance.

#### Request body

```json
{
  "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "valor": 250.0,
  "moeda": "BRL",
  "origem": "Empresa X",
  "receita": "SALARIO",
  "descricao": "Pagamento mensal"
}
```

#### Required fields

- `contaId`
- `valor` and it must be greater than `0`
- `moeda`
- `origem`
- `receita`

#### Success response

- Status: `201 Created`

```json
{
  "id": "5fc9c4c9-3c58-4ed8-a4d3-b822af95e5b7",
  "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "valor": 250.0,
  "moeda": "BRL",
  "origem": "Empresa X",
  "receita": "SALARIO",
  "data": "2026-04-21",
  "descricao": "Pagamento mensal"
}
```

#### Main errors

- `contaId e obrigatorio`
- `valor deve ser maior que zero`
- `moeda e obrigatoria`
- `origem e obrigatoria`
- `receita e obrigatoria`
- `Conta nao encontrada`

#### Example request

```bash
curl -X POST http://localhost:8080/transacoes/depositos \
  -H "Content-Type: application/json" \
  -d "{\"contaId\":\"2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44\",\"valor\":250.0,\"moeda\":\"BRL\",\"origem\":\"Empresa X\",\"receita\":\"SALARIO\",\"descricao\":\"Pagamento mensal\"}"
```

### `GET /transacoes/depositos`

Lists all deposits. It can also filter by `contaId`.

#### Query params

- Optional: `contaId`

#### Example URLs

- `GET /transacoes/depositos`
- `GET /transacoes/depositos?contaId=2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44`

#### Success response

- Status: `200 OK`

```json
[
  {
    "id": "5fc9c4c9-3c58-4ed8-a4d3-b822af95e5b7",
    "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
    "valor": 250.0,
    "moeda": "BRL",
    "origem": "Empresa X",
    "receita": "SALARIO",
    "data": "2026-04-21",
    "descricao": "Pagamento mensal"
  }
]
```

### `GET /transacoes/depositos/{id}`

Fetches a deposit by ID.

#### Path params

- `id`: deposit ID

#### Success response

- Status: `200 OK`

```json
{
  "id": "5fc9c4c9-3c58-4ed8-a4d3-b822af95e5b7",
  "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "valor": 250.0,
  "moeda": "BRL",
  "origem": "Empresa X",
  "receita": "SALARIO",
  "data": "2026-04-21",
  "descricao": "Pagamento mensal"
}
```

#### Main errors

- `id e obrigatorio`
- `Deposito nao encontrado`

---

## Withdrawals

### `POST /transacoes/saques`

Creates a withdrawal for an account.

If the withdrawal currency is different from the account currency, the backend attempts to convert the value before validating balance and monthly spending limit.

The route also updates:

- the account current balance
- the account monthly expense total

#### Request body

```json
{
  "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
  "valor": 120.0,
  "moeda": "BRL",
  "destino": "Supermercado",
  "formaPagamento": "PIX",
  "categoria": "ALIMENTACAO",
  "descricao": "Compras do mes",
  "observacao": "Compra semanal"
}
```

#### Required fields

- `contaId`
- `valor` and it must be greater than `0`
- `moeda`
- `destino`
- `formaPagamento`
- `categoria`

#### Success response

- Status: `201 Created`

```json
{
  "id": "f2e4c735-f0a6-47ca-8c62-5aab389ecf89",
  "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
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

#### Main errors

- `contaId e obrigatorio`
- `valor deve ser maior que zero`
- `moeda e obrigatoria`
- `formaPagamento e obrigatorio`
- `categoria e obrigatoria`
- `destino e obrigatorio`
- `Conta nao encontrada`
- `Saldo insuficiente. Disponivel: ...`
- `Limite mensal atingido. Limite: ...`

#### Example request

```bash
curl -X POST http://localhost:8080/transacoes/saques \
  -H "Content-Type: application/json" \
  -d "{\"contaId\":\"2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44\",\"valor\":120.0,\"moeda\":\"BRL\",\"destino\":\"Supermercado\",\"formaPagamento\":\"PIX\",\"categoria\":\"ALIMENTACAO\",\"descricao\":\"Compras do mes\",\"observacao\":\"Compra semanal\"}"
```

### `GET /transacoes/saques`

Lists all withdrawals. It can also filter by `contaId`.

#### Query params

- Optional: `contaId`

#### Example URLs

- `GET /transacoes/saques`
- `GET /transacoes/saques?contaId=2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44`

#### Success response

- Status: `200 OK`

```json
[
  {
    "id": "f2e4c735-f0a6-47ca-8c62-5aab389ecf89",
    "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
    "valor": 120.0,
    "moeda": "BRL",
    "destino": "Supermercado",
    "categoria": "ALIMENTACAO",
    "formaPagamento": "PIX",
    "data": "2026-04-21",
    "descricao": "Compras do mes",
    "observacao": "Compra semanal"
  }
]
```

### `GET /transacoes/saques/{id}`

Fetches a withdrawal by ID.

#### Path params

- `id`: withdrawal ID

#### Success response

- Status: `200 OK`

```json
{
  "id": "f2e4c735-f0a6-47ca-8c62-5aab389ecf89",
  "contaId": "2d8a4074-b7c6-44a0-8d0e-8d6717ce5d44",
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

#### Main errors

- `id e obrigatorio`
- `Saque nao encontrado`

---

## Route Summary

| Method | Route | Description |
|---|---|---|
| `POST` | `/contas` | Create account |
| `GET` | `/contas` | List accounts |
| `GET` | `/contas/{id}` | Get account by ID |
| `PATCH` | `/contas/{id}` | Update account |
| `DELETE` | `/contas/{id}` | Delete account |
| `GET` | `/usuarios/{usuarioId}/contas` | List accounts by user |
| `POST` | `/transacoes/depositos` | Create deposit |
| `GET` | `/transacoes/depositos` | List deposits |
| `GET` | `/transacoes/depositos/{id}` | Get deposit by ID |
| `POST` | `/transacoes/saques` | Create withdrawal |
| `GET` | `/transacoes/saques` | List withdrawals |
| `GET` | `/transacoes/saques/{id}` | Get withdrawal by ID |

## Important Implementation Detail

There are `useCases` for user operations in the codebase, but there are currently no user controllers exposed in `src/main/java/com/umc/controller`. So, based on the current application code, the documented public routes are only the ones listed in this file.
