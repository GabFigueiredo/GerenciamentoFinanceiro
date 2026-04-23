## Resumo das Rotas

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Criar usuário |
| `GET` | `/usuarios` | Listar usuários |
| `GET` | `/usuarios/{id}` | Buscar usuário por ID |
| `PATCH` | `/usuarios/{id}` | Atualizar usuário |
| `DELETE` | `/usuarios/{id}` | Deletar usuário |
| `POST` | `/contas` | Criar conta |
| `GET` | `/contas` | Listar contas |
| `GET` | `/contas/{id}` | Buscar conta por ID |
| `PATCH` | `/contas/{id}` | Atualizar conta |
| `DELETE` | `/contas/{id}` | Deletar conta |
| `POST` | `/contas/{contaId}/metas` | Criar meta para a conta |
| `GET` | `/contas/{contaId}/metas` | Listar metas da conta |
| `GET` | `/contas/{contaId}/metas/{metaId}` | Buscar meta por ID |
| `PATCH` | `/contas/{contaId}/metas/{metaId}` | Atualizar meta |
| `DELETE` | `/contas/{contaId}/metas/{metaId}` | Deletar meta |
| `POST` | `/transacoes/depositos` | Criar depósito |
| `GET` | `/transacoes/depositos` | Listar depósitos |
| `GET` | `/transacoes/depositos/{id}` | Buscar depósito por ID |
| `POST` | `/transacoes/saques` | Criar saque |
| `GET` | `/transacoes/saques` | Listar saques |
| `GET` | `/transacoes/saques/{id}` | Buscar saque por ID |
