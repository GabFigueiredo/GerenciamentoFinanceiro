# Informações Para Diagrama de Sequência

Este documento reúne as principais informações para a criação de diagramas de sequência das funcionalidades centrais da aplicação.

O foco aqui não é documentar todos os detalhes de implementação, mas organizar:

- atores
- participantes técnicos
- fluxo principal
- fluxos alternativos
- mensagens importantes

## Objetivo

A aplicação é um sistema de gerenciamento financeiro com:

- cadastro de usuário
- login
- criação e gerenciamento de contas
- depósitos
- saques
- metas financeiras por conta
- visualização de dashboard

## Participantes Recomendados

Para os diagramas de sequência, os participantes mais úteis são:

- `Usuário`
- `Front-end`
  Página, modal ou dashboard que inicia a ação
- `api/api.ts`
  Camada cliente responsável pelas requisições HTTP
- `Controller`
  Endpoint do Spring Boot
- `UseCase`
  Regra de negócio
- `DAO / Repository`
  Acesso a dados
- `ExchangeAPI`
  Conversão de moeda em depósitos e saques
- `MySQL`
  Persistência
- `localStorage`
  Persistência local da sessão do usuário no front-end

## Observações Arquiteturais Importantes

- O front-end chama o back-end via `fetch`, centralizado em `Front-end/api/api.ts`.
- O login atual é uma simulação:
  O front-end busca todos os usuários em `/usuarios`, compara `email` e `senha` localmente e, se encontrar um usuário compatível, salva os dados em `localStorage`.
- Não existe autenticação real com token.
- As metas são vinculadas a uma conta específica.
- A moeda da meta sempre é a mesma moeda da conta.
- Depósitos e saques podem envolver conversão cambial via `ExchangeAPI`.
- O back-end usa Controllers, UseCases e DAOs.
- O tratamento de erro é centralizado em `GlobalExceptionHandler`.

## Funcionalidades Principais

## 1. Cadastro de usuário e primeira conta

### Objetivo

Cadastrar um novo usuário e, em seguida, criar a primeira conta dele.

### Participantes

- `Usuário`
- `Front-end /cadastro`
- `api/api.ts`
- `CriarUsuarioController`
- `CriarUsuarioUseCase`
- `UsuarioDAO`
- `MySQL`
- `CriarContaController`
- `CriarContaUseCase`
- `ContaDAO`
- `localStorage`

### Fluxo principal

1. O usuário preenche os dados pessoais.
2. O front-end envia `POST /usuarios`.
3. O controller recebe a requisição e chama `CriarUsuarioUseCase`.
4. O use case valida nome, CPF, email, senha e salário.
5. O DAO salva o usuário no banco.
6. O back-end retorna o usuário criado.
7. O front-end avança para a etapa de criação da conta.
8. O usuário informa moeda, saldo inicial, limite mensal e descrição.
9. O front-end envia `POST /contas`.
10. O controller chama `CriarContaUseCase`.
11. O use case valida os dados, busca o usuário pelo `usuarioId` e monta a conta.
12. O DAO salva a conta.
13. O back-end retorna a conta criada.
14. O front-end grava o usuário no `localStorage`.
15. O front-end redireciona para o dashboard.

### Fluxos alternativos

- Se o usuário já existir por restrição do banco, a operação falha.
- Se algum campo obrigatório estiver ausente, o back-end retorna erro `400`.
- Se o `usuarioId` informado para a conta não existir, retorna `404`.

### Mensagens importantes

- `POST /usuarios`
- `POST /contas`
- `localStorage.setItem("finance_user", ...)`

## 2. Login

### Objetivo

Permitir que um usuário entre na aplicação.

### Participantes

- `Usuário`
- `Front-end /login`
- `api/api.ts`
- `ListarUsuariosController`
- `ListarUsuariosUseCase`
- `UsuarioDAO`
- `MySQL`
- `localStorage`

### Fluxo principal

1. O usuário informa email e senha.
2. O front-end envia `GET /usuarios`.
3. O back-end retorna a lista de usuários.
4. O front-end compara localmente o `email` e a `senha`.
5. Se encontrar um usuário compatível, salva os dados no `localStorage`.
6. O front-end redireciona para `/dashboard`.

### Fluxos alternativos

- Se nenhum usuário corresponder, o front-end exibe erro de credenciais inválidas.
- Se o back-end estiver indisponível, o front-end exibe erro de conexão.

### Observação importante

Esse fluxo é apenas simulado.
Em um sistema real, a validação de senha aconteceria no back-end.

## 3. Carregamento do dashboard

### Objetivo

Carregar a visão principal da conta selecionada com contas, depósitos, saques e metas.

### Participantes

- `Usuário`
- `Front-end /dashboard`
- `localStorage`
- `api/api.ts`
- `ListarContasController`
- `ListarContasUseCase`
- `ContaDAO`
- `ListarDepositosController`
- `ListarDepositosUseCase`
- `DepositoDAO`
- `ListarSaquesController`
- `ListarSaquesUseCase`
- `SaqueDAO`
- `ListarMetasController`
- `ListarMetasUseCase`
- `MetaDAO`
- `MySQL`

### Fluxo principal

1. O dashboard lê o usuário autenticado do `localStorage`.
2. O front-end envia `GET /contas?usuarioId={idDoUsuario}`.
3. O back-end retorna as contas do usuário.
4. O front-end seleciona uma conta.
5. O front-end dispara em paralelo:
   `GET /transacoes/depositos?contaId=...`
   `GET /transacoes/saques?contaId=...`
   `GET /contas/{contaId}/metas`
6. O back-end retorna os três conjuntos de dados.
7. O dashboard atualiza o estado e renderiza:
   saldo, limite, despesas, metas e histórico.

### Fluxos alternativos

- Se o usuário não estiver no `localStorage`, o front-end redireciona para `/login`.
- Se não houver contas, o dashboard exibe o estado vazio.

## 4. Criar conta

### Objetivo

Criar uma nova conta para um usuário existente.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `CriarContaController`
- `CriarContaUseCase`
- `UsuarioDAO`
- `ContaDAO`
- `MySQL`

### Fluxo principal

1. O usuário abre o modal de nova conta.
2. O front-end envia `POST /contas`.
3. O controller aciona `CriarContaUseCase`.
4. O use case valida os campos e busca o usuário.
5. O DAO persiste a conta.
6. O back-end retorna a conta criada.
7. O front-end adiciona a conta à lista e a seleciona.

## 5. Editar conta

### Objetivo

Atualizar o limite de gasto mensal e/ou a descrição de uma conta.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `AtualizarContaController`
- `AtualizarContaUseCase`
- `ContaDAO`
- `MySQL`

### Fluxo principal

1. O usuário abre o modal de edição.
2. O front-end envia `PATCH /contas/{id}`.
3. O controller chama `AtualizarContaUseCase`.
4. O use case busca a conta atual.
5. O use case mantém os valores antigos e substitui apenas os campos enviados.
6. O DAO atualiza a conta.
7. O back-end retorna a conta atualizada.
8. O front-end atualiza a conta selecionada e a lista de contas.

## 6. Excluir conta

### Objetivo

Remover uma conta.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `DeletarContaController`
- `DeletarContaUseCase`
- `ContaDAO`
- `MySQL`

### Fluxo principal

1. O usuário confirma a exclusão da conta.
2. O front-end envia `DELETE /contas/{id}`.
3. O controller chama `DeletarContaUseCase`.
4. O use case verifica se a conta existe.
5. O DAO remove a conta.
6. O back-end retorna `204 No Content`.
7. O front-end recarrega a lista de contas.

## 7. Realizar depósito

### Objetivo

Registrar um depósito e atualizar o saldo da conta.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `RealizarDepositoController`
- `RealizarDepositoUseCase`
- `ContaDAO`
- `ExchangeAPI`
- `DepositoDAO`
- `MySQL`

### Fluxo principal

1. O usuário abre o modal de depósito.
2. O front-end envia `POST /transacoes/depositos`.
3. O controller chama `RealizarDepositoUseCase`.
4. O use case valida `contaId`, valor, moeda, origem e receita.
5. O use case busca a conta.
6. O use case cria um objeto monetário com a moeda enviada.
7. Se a moeda do depósito for diferente da moeda da conta:
   chama `ExchangeAPI.converter(...)`.
8. O valor convertido é somado ao saldo da conta.
9. O DAO atualiza a conta.
10. O DAO salva o depósito.
11. O back-end retorna o depósito criado.
12. O front-end recarrega a conta, depósitos, saques e metas.

### Fluxos alternativos

- Valor inválido gera `400`.
- Conta inexistente gera `404`.
- Falha externa na API de câmbio pode gerar erro de servidor.

### Observação importante

O depósito salvo mantém a moeda original enviada pelo usuário.
O saldo da conta é atualizado na moeda da própria conta.

## 8. Realizar saque

### Objetivo

Registrar um saque, validar saldo e limite mensal e atualizar a conta.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `RealizarSaqueController`
- `RealizarSaqueUseCase`
- `ContaDAO`
- `ExchangeAPI`
- `SaqueDAO`
- `MySQL`

### Fluxo principal

1. O usuário abre o modal de saque.
2. O front-end envia `POST /transacoes/saques`.
3. O controller chama `RealizarSaqueUseCase`.
4. O use case valida os campos obrigatórios.
5. O use case busca a conta.
6. Se a moeda informada for diferente da moeda da conta:
   chama `ExchangeAPI.converter(...)`.
7. O use case valida:
   saldo disponível
   limite mensal projetado
8. O saldo da conta é reduzido.
9. A despesa mensal da conta é incrementada.
10. O DAO atualiza a conta.
11. O DAO salva o saque.
12. O back-end retorna o saque criado.
13. O front-end recarrega a conta e as transações.

### Fluxos alternativos

- Saldo insuficiente gera `409`.
- Limite mensal excedido gera `409`.
- Conta inexistente gera `404`.

## 9. Criar meta

### Objetivo

Criar uma meta financeira associada a uma conta.

### Participantes

- `Usuário`
- `Front-end`
- `Modal de Meta`
- `api/api.ts`
- `CriarMetaController`
- `CriarMetaUseCase`
- `ContaDAO`
- `MetaDAO`
- `MySQL`

### Fluxo principal

1. O usuário abre o modal de meta.
2. O front-end envia `POST /contas/{contaId}/metas`.
3. O controller chama `CriarMetaUseCase`.
4. O use case valida nome e valor objetivo.
5. O use case busca a conta.
6. O use case define a moeda da meta com base na moeda da conta.
7. O use case valida as datas.
8. O DAO salva a meta.
9. O back-end retorna a meta criada.
10. O front-end adiciona a meta ao estado da tela.

### Fluxos alternativos

- Conta inexistente gera `404`.
- Valor objetivo inválido gera `400`.
- Data de conclusão anterior à data de início gera `400`.

## 10. Listar metas

### Objetivo

Exibir todas as metas de uma conta.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `ListarMetasController`
- `ListarMetasUseCase`
- `ContaDAO`
- `MetaDAO`
- `MySQL`

### Fluxo principal

1. O front-end envia `GET /contas/{contaId}/metas`.
2. O controller chama `ListarMetasUseCase`.
3. O use case verifica se a conta existe.
4. O DAO busca as metas da conta.
5. O back-end retorna a lista.
6. O dashboard renderiza barras de progresso, datas e percentual.

## 11. Excluir meta

### Objetivo

Remover uma meta de uma conta.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `DeletarMetaController`
- `DeletarMetaUseCase`
- `ContaDAO`
- `MetaDAO`
- `MySQL`

### Fluxo principal

1. O usuário clica para remover a meta.
2. O front-end envia `DELETE /contas/{contaId}/metas/{metaId}`.
3. O controller chama `DeletarMetaUseCase`.
4. O use case verifica se a conta existe.
5. O use case verifica se a meta existe dentro da conta.
6. O DAO remove a meta.
7. O back-end retorna `204 No Content`.
8. O front-end remove a meta da lista em memória.

## 12. Atualizar meta

### Objetivo

Atualizar uma meta existente.

### Participantes

- `Usuário`
- `Front-end`
- `api/api.ts`
- `AtualizarMetaController`
- `AtualizarMetaUseCase`
- `ContaDAO`
- `MetaDAO`
- `MySQL`

### Observação importante

O back-end já possui CRUD completo para metas, incluindo atualização.
No momento, o front-end está preparado para listar, criar e excluir metas, mas ainda não expõe uma interface visual específica para editar meta.

### Fluxo principal no back-end

1. O cliente envia `PATCH /contas/{contaId}/metas/{metaId}`.
2. O controller chama `AtualizarMetaUseCase`.
3. O use case valida `contaId` e `metaId`.
4. O use case busca conta e meta atual.
5. O use case atualiza apenas os campos enviados.
6. O DAO persiste a meta atualizada.
7. O back-end retorna a meta atualizada.

## Sugestão de Diagramas

Para facilitar a leitura, vale separar em diagramas menores:

- `Diagrama 1`
  Cadastro de usuário + criação da primeira conta
- `Diagrama 2`
  Login
- `Diagrama 3`
  Carregamento do dashboard
- `Diagrama 4`
  Depósito com conversão de moeda
- `Diagrama 5`
  Saque com validação de saldo e limite
- `Diagrama 6`
  CRUD de metas
- `Diagrama 7`
  CRUD de contas

## Pontos Importantes Para Destacar no Diagrama

- O front-end não chama o banco diretamente.
- Toda requisição passa por `api/api.ts`.
- O login é validado no front-end, não no back-end.
- `localStorage` participa apenas da manutenção da sessão simulada.
- O `GlobalExceptionHandler` padroniza os erros do back-end.
- Depósitos e saques podem envolver `ExchangeAPI`.
- Metas são sempre vinculadas a uma conta.
- Metas usam a mesma moeda da conta.

## Arquivos de Referência

Se precisar cruzar o diagrama com a implementação, os arquivos mais úteis são:

- `Front-end/api/api.ts`
- `Front-end/app/cadastro/page.tsx`
- `Front-end/app/login/page.tsx`
- `Front-end/app/dashboard/page.tsx`
- `Front-end/app/dashboard/components/modal-deposito.tsx`
- `Front-end/app/dashboard/components/modal-saque.tsx`
- `Front-end/app/dashboard/components/modal-meta.tsx`
- `Back-end/API_ROUTES.md`

## Assunção Utilizada

Este documento considera como "principais funcionalidades" aquilo que já está efetivamente disponível no fluxo atual da aplicação entre front-end e back-end.
