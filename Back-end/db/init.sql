CREATE DATABASE IF NOT EXISTS gerenciamento_financeiro;
USE gerenciamento_financeiro;

CREATE TABLE IF NOT EXISTS usuario (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo VARCHAR(100),
    salario DECIMAL(15,2) NOT NULL,
    contato_celular VARCHAR(20),
    contato_telefone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS conta (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL,
    moeda VARCHAR(3) NOT NULL,
    saldo_atual DECIMAL(15,2) NOT NULL,
    despesa_mensal DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    limite_gasto_mensal DECIMAL(15,2) NOT NULL,
    descricao VARCHAR(255),
    data_criacao DATE NOT NULL,
    data_atualizacao DATE NOT NULL,
    CONSTRAINT fk_conta_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS deposito (
    id VARCHAR(36) PRIMARY KEY,
    conta_id VARCHAR(36) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    moeda VARCHAR(3) NOT NULL,
    data DATE NOT NULL,
    descricao VARCHAR(255),
    data_criacao DATE NOT NULL,
    origem VARCHAR(120) NOT NULL,
    receita VARCHAR(30) NOT NULL,
    CONSTRAINT fk_deposito_conta
        FOREIGN KEY (conta_id) REFERENCES conta(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS saque (
    id VARCHAR(36) PRIMARY KEY,
    conta_id VARCHAR(36) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    moeda VARCHAR(3) NOT NULL,
    data DATE NOT NULL,
    descricao VARCHAR(255),
    data_criacao DATE NOT NULL,
    destino VARCHAR(120) NOT NULL,
    categoria_despesa VARCHAR(30) NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL,
    observacao VARCHAR(255),
    CONSTRAINT fk_saque_conta
        FOREIGN KEY (conta_id) REFERENCES conta(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS meta (
    id VARCHAR(36) PRIMARY KEY,
    conta_id VARCHAR(36) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    valor_objetivo DECIMAL(15,2) NOT NULL,
    moeda VARCHAR(3) NOT NULL,
    cargo VARCHAR(100),
    data_inicio DATE NOT NULL,
    data_de_conclusao DATE,
    CONSTRAINT fk_meta_conta
        FOREIGN KEY (conta_id) REFERENCES conta(id)
        ON DELETE CASCADE
);

DELETE FROM meta;
DELETE FROM saque;
DELETE FROM deposito;
DELETE FROM conta;
DELETE FROM usuario;

INSERT INTO usuario (
    id, nome, cpf, email, senha, cargo, salario,
    contato_celular, contato_telefone
) VALUES
(
    '11111111-1111-1111-1111-111111111111',
    'Gabriel Silva',
    '123.456.789-00',
    'gabriel.silva@email.com',
    '123456',
    'Software Engineer',
    8500.00,
    '(11) 98888-1111',
    '(11) 3333-1111'
),
(
    '22222222-2222-2222-2222-222222222222',
    'Mariana Costa',
    '987.654.321-00',
    'mariana.costa@email.com',
    '654321',
    'Product Manager',
    12000.00,
    '(11) 97777-2222',
    '(11) 3333-2222'
);


INSERT INTO conta (
    id, usuario_id, moeda, saldo_atual, despesa_mensal,
    limite_gasto_mensal, descricao, data_criacao, data_atualizacao
) VALUES
(
    'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    '11111111-1111-1111-1111-111111111111',
    'BRL',
    5730.00,
    1270.00,
    4000.00,
    'Conta principal do Gabriel',
    '2026-04-01',
    '2026-04-21'
),
(
    'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
    '11111111-1111-1111-1111-111111111111',
    'USD',
    980.00,
    220.00,
    1500.00,
    'Conta internacional do Gabriel',
    '2026-04-03',
    '2026-04-21'
),
(
    'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
    '22222222-2222-2222-2222-222222222222',
    'BRL',
    10420.00,
    1580.00,
    5000.00,
    'Conta principal da Mariana',
    '2026-04-02',
    '2026-04-21'
);

INSERT INTO meta (
    id, conta_id, nome, valor_objetivo, moeda, cargo, data_inicio, data_de_conclusao
) VALUES
(
    'ccccccc1-cccc-cccc-cccc-ccccccccccc1',
    'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    'Reserva de emergencia',
    10000.00,
    'BRL',
    'Seguranca',
    '2026-04-01',
    '2026-12-31'
),
(
    'ccccccc2-cccc-cccc-cccc-ccccccccccc2',
    'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
    'Viagem internacional',
    3500.00,
    'USD',
    'Lazer',
    '2026-04-03',
    '2026-10-15'
),
(
    'ccccccc3-cccc-cccc-cccc-ccccccccccc3',
    'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
    'Entrada apartamento',
    50000.00,
    'BRL',
    'Patrimonio',
    '2026-04-02',
    '2027-06-30'
);

INSERT INTO deposito (
    id, conta_id, valor, moeda, data, descricao, data_criacao, origem, receita
) VALUES
(
    'ddddddd1-dddd-dddd-dddd-ddddddddddd1',
    'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    5000.00,
    'BRL',
    '2026-04-05',
    'Salario de abril',
    '2026-04-05',
    'Tech Solutions',
    'SALARIO'
),
(
    'ddddddd2-dddd-dddd-dddd-ddddddddddd2',
    'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    1200.00,
    'BRL',
    '2026-04-10',
    'Projeto freelance landing page',
    '2026-04-10',
    'Cliente Freelance',
    'FREELANCE'
),
(
    'ddddddd3-dddd-dddd-dddd-ddddddddddd3',
    'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
    1500.00,
    'USD',
    '2026-04-06',
    'Reserva para viagem',
    '2026-04-06',
    'Wise Transfer',
    'OUTROS'
),
(
    'ddddddd4-dddd-dddd-dddd-ddddddddddd4',
    'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
    11000.00,
    'BRL',
    '2026-04-05',
    'Salario mensal',
    '2026-04-05',
    'Acme Corp',
    'SALARIO'
),
(
    'ddddddd5-dddd-dddd-dddd-ddddddddddd5',
    'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
    1000.00,
    'BRL',
    '2026-04-12',
    'Rendimento de investimentos',
    '2026-04-12',
    'Corretora XPTO',
    'INVESTIMENTO'
);

INSERT INTO saque (
    id, conta_id, valor, moeda, data, descricao, data_criacao,
    destino, categoria_despesa, forma_pagamento, observacao
) VALUES
(
    'sssssss1-ssss-ssss-ssss-sssssssssss1',
    'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    950.00,
    'BRL',
    '2026-04-08',
    'Aluguel do apartamento',
    '2026-04-08',
    'Imobiliaria Centro',
    'MORADIA',
    'TRANSFERENCIA',
    'Pagamento referente ao mes de abril'
),
(
    'sssssss2-ssss-ssss-ssss-sssssssssss2',
    'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
    320.00,
    'BRL',
    '2026-04-15',
    'Compras do mercado',
    '2026-04-15',
    'Supermercado Bom Preco',
    'ALIMENTACAO',
    'PIX',
    'Compra da semana'
),
(
    'sssssss3-ssss-ssss-ssss-sssssssssss3',
    'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
    220.00,
    'USD',
    '2026-04-18',
    'Assinaturas e servicos',
    '2026-04-18',
    'Apple e Notion',
    'LAZER',
    'CARTAO_CREDITO',
    'Assinaturas em dolar'
),
(
    'sssssss4-ssss-ssss-ssss-sssssssssss4',
    'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
    1280.00,
    'BRL',
    '2026-04-09',
    'Mensalidade e cursos',
    '2026-04-09',
    'Plataforma de estudos',
    'EDUCACAO',
    'BOLETO',
    'Especializacao profissional'
),
(
    'sssssss5-ssss-ssss-ssss-sssssssssss5',
    'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
    300.00,
    'BRL',
    '2026-04-16',
    'Consulta medica',
    '2026-04-16',
    'Clinica Vida',
    'SAUDE',
    'PIX',
    'Consulta de rotina'
);
