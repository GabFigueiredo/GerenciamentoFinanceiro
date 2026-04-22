const BASE_URL = "http://localhost:8080";

export type Moeda = "BRL" | "USD" | "EUR";
export type Receita = "SALARIO" | "FREELANCE" | "INVESTIMENTO" | "ALUGUEL" | "OUTROS";
export type Categoria =
  | "ALIMENTACAO"
  | "TRANSPORTE"
  | "SAUDE"
  | "EDUCACAO"
  | "LAZER"
  | "MORADIA"
  | "OUTROS";
export type FormaPagamento =
  | "DINHEIRO"
  | "CARTAO_CREDITO"
  | "CARTAO_DEBITO"
  | "PIX"
  | "TRANSFERENCIA"
  | "BOLETO";

export type Usuario = {
  id: string;
  nome: string;
  cpf: string;
  email: string;
  senha: string;
  celular?: string;
  telefone?: string;
  cargo?: string;
  salario: number;
};

export type Conta = {
  id: string;
  usuarioId: string;
  moeda: Moeda;
  saldoAtual: number;
  despesaMensal: number;
  limiteGastoMensal: number;
  descricao?: string;
  dataCriacao: string;
  dataAtualizacao: string;
};

export type Deposito = {
  id: string;
  contaId: string;
  valor: number;
  moeda: string;
  origem: string;
  receita: string;
  data: string;
  descricao?: string;
};

export type Saque = {
  id: string;
  contaId: string;
  valor: number;
  moeda: string;
  destino: string;
  categoria: string;
  formaPagamento: string;
  data: string;
  descricao?: string;
  observacao?: string;
};

export type Meta = {
  id: string;
  contaId: string;
  nome: string;
  valorObjetivo: number;
  moeda: Moeda;
  cargo?: string;
  dataInicio: string;
  dataDeConclusao?: string;
};

export type CriarUsuarioBody = Omit<Usuario, "id">;

export type CriarContaBody = {
  usuarioId: string;
  moeda: Moeda;
  saldoAtual: number;
  limiteGastoMensal: number;
  descricao?: string;
};

export type AtualizarContaBody = {
  limiteGastoMensal?: number;
  descricao?: string;
};

export type CriarDepositoBody = {
  contaId: string;
  valor: number;
  moeda: Moeda;
  origem: string;
  receita: Receita;
  descricao?: string;
};

export type CriarSaqueBody = {
  contaId: string;
  valor: number;
  moeda: Moeda;
  destino: string;
  formaPagamento: FormaPagamento;
  categoria: Categoria;
  descricao?: string;
  observacao?: string;
};

export type CriarMetaBody = {
  nome: string;
  valorObjetivo: number;
  cargo?: string;
  dataInicio?: string;
  dataDeConclusao?: string;
};

export type AtualizarMetaBody = {
  nome?: string;
  valorObjetivo?: number;
  cargo?: string;
  dataInicio?: string;
  dataDeConclusao?: string;
};

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  if (!res.ok) {
    const error = await res.json().catch(() => ({ message: "Erro desconhecido" }));
    throw new Error(error.message || `Erro ${res.status}`);
  }

  if (res.status === 204) return undefined as T;
  return res.json();
}

export const api = {
  usuarios: {
    listar: () => request<Usuario[]>("/usuarios"),
    buscar: (id: string) => request<Usuario>(`/usuarios/${id}`),
    criar: (body: CriarUsuarioBody) =>
      request<Usuario>("/usuarios", { method: "POST", body: JSON.stringify(body) }),
    atualizar: (id: string, body: Partial<CriarUsuarioBody>) =>
      request<Usuario>(`/usuarios/${id}`, { method: "PATCH", body: JSON.stringify(body) }),
    deletar: (id: string) => request<void>(`/usuarios/${id}`, { method: "DELETE" }),
  },

  contas: {
    listar: (usuarioId?: string) =>
      request<Conta[]>(usuarioId ? `/contas?usuarioId=${usuarioId}` : "/contas"),
    buscar: (id: string) => request<Conta>(`/contas/${id}`),
    criar: (body: CriarContaBody) =>
      request<Conta>("/contas", { method: "POST", body: JSON.stringify(body) }),
    atualizar: (id: string, body: AtualizarContaBody) =>
      request<Conta>(`/contas/${id}`, { method: "PATCH", body: JSON.stringify(body) }),
    deletar: (id: string) => request<void>(`/contas/${id}`, { method: "DELETE" }),
  },

  metas: {
    listar: (contaId: string) => request<Meta[]>(`/contas/${contaId}/metas`),
    buscar: (contaId: string, metaId: string) =>
      request<Meta>(`/contas/${contaId}/metas/${metaId}`),
    criar: (contaId: string, body: CriarMetaBody) =>
      request<Meta>(`/contas/${contaId}/metas`, {
        method: "POST",
        body: JSON.stringify(body),
      }),
    atualizar: (contaId: string, metaId: string, body: AtualizarMetaBody) =>
      request<Meta>(`/contas/${contaId}/metas/${metaId}`, {
        method: "PATCH",
        body: JSON.stringify(body),
      }),
    deletar: (contaId: string, metaId: string) =>
      request<void>(`/contas/${contaId}/metas/${metaId}`, { method: "DELETE" }),
  },

  depositos: {
    listar: (contaId?: string) =>
      request<Deposito[]>(
        contaId ? `/transacoes/depositos?contaId=${contaId}` : "/transacoes/depositos"
      ),
    buscar: (id: string) => request<Deposito>(`/transacoes/depositos/${id}`),
    criar: (body: CriarDepositoBody) =>
      request<Deposito>("/transacoes/depositos", { method: "POST", body: JSON.stringify(body) }),
  },

  saques: {
    listar: (contaId?: string) =>
      request<Saque[]>(
        contaId ? `/transacoes/saques?contaId=${contaId}` : "/transacoes/saques"
      ),
    buscar: (id: string) => request<Saque>(`/transacoes/saques/${id}`),
    criar: (body: CriarSaqueBody) =>
      request<Saque>("/transacoes/saques", { method: "POST", body: JSON.stringify(body) }),
  },
};

export function formatMoeda(valor: number, moeda: string): string {
  const locales: Record<string, string> = { BRL: "pt-BR", USD: "en-US", EUR: "de-DE" };
  return new Intl.NumberFormat(locales[moeda] ?? "pt-BR", {
    style: "currency",
    currency: moeda,
  }).format(valor);
}

export function formatData(iso: string): string {
  return new Date(iso + "T00:00:00").toLocaleDateString("pt-BR");
}

export const RECEITA_LABELS: Record<string, string> = {
  SALARIO: "Salario",
  FREELANCE: "Freelance",
  INVESTIMENTO: "Investimento",
  ALUGUEL: "Aluguel",
  OUTROS: "Outros",
};

export const CATEGORIA_LABELS: Record<string, string> = {
  ALIMENTACAO: "Alimentacao",
  TRANSPORTE: "Transporte",
  SAUDE: "Saude",
  EDUCACAO: "Educacao",
  LAZER: "Lazer",
  MORADIA: "Moradia",
  OUTROS: "Outros",
};

export const FORMA_LABELS: Record<string, string> = {
  DINHEIRO: "Dinheiro",
  CARTAO_CREDITO: "Cartao de credito",
  CARTAO_DEBITO: "Cartao de debito",
  PIX: "PIX",
  TRANSFERENCIA: "Transferencia",
  BOLETO: "Boleto",
};
