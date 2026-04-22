"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth, type AuthUser } from "@/hooks/useAuth";
import ModalMeta from "./components/modal-meta";
import ModalDeletarConta from "./components/modal-deletar";
import ModalNovaConta from "./components/modal-nova-conta";
import ModalEditarConta from "./components/modal-editar";
import ModalSaque from "./components/modal-saque";
import ModalDeposito from "./components/modal-deposito";
import {
  api,
  CATEGORIA_LABELS,
  Conta,
  Deposito,
  FORMA_LABELS,
  formatData,
  formatMoeda,
  Meta,
  RECEITA_LABELS,
  Saque,
} from "@/api/api";

type ModalType =
  | "deposito"
  | "saque"
  | "editar-conta"
  | "nova-conta"
  | "meta"
  | "deletar-conta"
  | null;

type Tab = "visao-geral" | "depositos" | "saques" | "metas";

function limitePercentual(despesa: number, limite: number): number {
  if (limite <= 0) return 0;
  return Math.min((despesa / limite) * 100, 100);
}

function metaPercentual(saldo: number, objetivo: number): number {
  if (objetivo <= 0) return 0;
  return Math.min((saldo / objetivo) * 100, 100);
}

function diasRestantes(dataDeConclusao?: string): number {
  if (!dataDeConclusao) return Number.POSITIVE_INFINITY;
  const hoje = new Date();
  const fim = new Date(dataDeConclusao + "T00:00:00");
  return Math.ceil((fim.getTime() - hoje.getTime()) / (1000 * 60 * 60 * 24));
}

function StatCard({
  label,
  value,
  sub,
  color = "text-white",
}: {
  label: string;
  value: string;
  sub?: string;
  color?: string;
}) {
  return (
    <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
      <p className="text-xs text-zinc-500 uppercase tracking-wider mb-1">{label}</p>
      <p className={`text-xl font-bold font-mono ${color}`}>{value}</p>
      {sub ? <p className="text-xs text-zinc-600 mt-0.5">{sub}</p> : null}
    </div>
  );
}

function BadgeMoeda({ moeda }: { moeda: string }) {
  const colors: Record<string, string> = {
    BRL: "bg-emerald-950 text-emerald-400 border-emerald-800",
    USD: "bg-blue-950 text-blue-400 border-blue-800",
    EUR: "bg-violet-950 text-violet-400 border-violet-800",
  };

  return (
    <span
      className={`text-xs font-mono font-bold px-2 py-0.5 rounded border ${
        colors[moeda] ?? "bg-zinc-800 text-zinc-400 border-zinc-700"
      }`}
    >
      {moeda}
    </span>
  );
}

export default function DashboardPage() {
  const router = useRouter();
  const { getUser, logout } = useAuth();

  const [user] = useState<AuthUser | null>(() => getUser());
  const [contas, setContas] = useState<Conta[]>([]);
  const [contaSelecionada, setContaSelecionada] = useState<Conta | null>(null);
  const [depositos, setDepositos] = useState<Deposito[]>([]);
  const [saques, setSaques] = useState<Saque[]>([]);
  const [metas, setMetas] = useState<Meta[]>([]);
  const [tab, setTab] = useState<Tab>("visao-geral");
  const [modal, setModal] = useState<ModalType>(null);
  const [loading, setLoading] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  useEffect(() => {
    if (!user) {
      router.push("/login");
    }
  }, [router, user]);

  async function loadContas(usuarioId: string, selectedId?: string) {
    try {
      const data = await api.contas.listar(usuarioId);
      setContas(data);

      if (data.length === 0) {
        setContaSelecionada(null);
        return;
      }

      const nextSelected = selectedId ? data.find((conta) => conta.id === selectedId) : undefined;
      setContaSelecionada(nextSelected ?? data[0]);
      setTab("visao-geral");
    } catch {
      setContas([]);
      setContaSelecionada(null);
    }
  }

  async function loadDadosConta(contaId: string) {
    setLoading(true);
    try {
      const [deps, sqs, mts] = await Promise.all([
        api.depositos.listar(contaId),
        api.saques.listar(contaId),
        api.metas.listar(contaId),
      ]);

      setDepositos(deps.sort((a, b) => b.data.localeCompare(a.data)));
      setSaques(sqs.sort((a, b) => b.data.localeCompare(a.data)));
      setMetas(mts.sort((a, b) => b.dataInicio.localeCompare(a.dataInicio)));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (user) {
      const timeoutId = window.setTimeout(() => {
        void loadContas(user.id);
      }, 0);

      return () => window.clearTimeout(timeoutId);
    }
  }, [user]);

  useEffect(() => {
    const selectedContaId = contaSelecionada?.id;
    if (!selectedContaId) return;
    const timeoutId = window.setTimeout(() => {
      void loadDadosConta(selectedContaId);
    }, 0);

    return () => window.clearTimeout(timeoutId);
  }, [contaSelecionada?.id]);

  async function refreshConta() {
    if (!contaSelecionada || !user) return;

    const updated = await api.contas.buscar(contaSelecionada.id);
    setContaSelecionada(updated);
    setContas((prev) => prev.map((conta) => (conta.id === updated.id ? updated : conta)));
    await loadDadosConta(updated.id);
  }

  async function handleDeleteMeta(metaId: string) {
    if (!contaSelecionada) return;

    try {
      await api.metas.deletar(contaSelecionada.id, metaId);
      setMetas((prev) => prev.filter((meta) => meta.id !== metaId));
    } catch {
      // keep UI state unchanged if the backend delete fails
    }
  }

  const percentualLimite = contaSelecionada
    ? limitePercentual(contaSelecionada.despesaMensal, contaSelecionada.limiteGastoMensal)
    : 0;
  const saldoLivre = contaSelecionada
    ? contaSelecionada.saldoAtual - contaSelecionada.despesaMensal
    : 0;
  const totalDepositos = depositos.reduce((acc, deposito) => acc + deposito.valor, 0);
  const totalSaques = saques.reduce((acc, saque) => acc + saque.valor, 0);
  const saquesPorCategoria = saques.reduce<Record<string, number>>((acc, saque) => {
    acc[saque.categoria] = (acc[saque.categoria] ?? 0) + saque.valor;
    return acc;
  }, {});

  if (!user) return null;

  return (
    <div className="min-h-screen bg-zinc-950 flex">
      {sidebarOpen ? (
        <div
          className="fixed inset-0 z-20 bg-black/60 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      ) : null}

      <aside
        className={`fixed top-0 left-0 h-full z-30 w-64 bg-zinc-900 border-r border-zinc-800 flex flex-col transition-transform duration-200 ${
          sidebarOpen ? "translate-x-0" : "-translate-x-full"
        } lg:translate-x-0 lg:static lg:z-auto`}
      >
        <div className="px-5 py-5 border-b border-zinc-800">
          <span className="text-2xl font-bold tracking-tight text-white font-mono">
            fin<span className="text-emerald-400">.</span>
          </span>
        </div>

        <div className="flex-1 overflow-y-auto py-4 px-3">
          <p className="text-xs text-zinc-600 uppercase tracking-wider px-2 mb-2">Suas contas</p>

          {contas.map((conta) => (
            <button
              key={conta.id}
              onClick={() => {
                setTab("visao-geral");
                setContaSelecionada(conta);
                setSidebarOpen(false);
              }}
              className={`w-full text-left px-3 py-3 rounded-xl mb-1 transition border ${
                contaSelecionada?.id === conta.id
                  ? "bg-zinc-800 border-zinc-700"
                  : "hover:bg-zinc-800/50 border-transparent"
              }`}
            >
              <div className="flex items-center justify-between mb-1">
                <span className="text-white text-sm font-medium truncate">
                  {conta.descricao || "Conta"}
                </span>
                <BadgeMoeda moeda={conta.moeda} />
              </div>
              <p className="text-zinc-400 text-xs font-mono">
                {formatMoeda(conta.saldoAtual, conta.moeda)}
              </p>
            </button>
          ))}

          <button
            onClick={() => setModal("nova-conta")}
            className="w-full flex items-center gap-2 px-3 py-2.5 rounded-xl text-zinc-500 hover:text-zinc-300 hover:bg-zinc-800/50 text-sm transition mt-1"
          >
            <span className="text-lg leading-none">+</span> Nova conta
          </button>
        </div>

        <div className="px-4 py-4 border-t border-zinc-800">
          <p className="text-white text-sm font-medium truncate">{user.nome}</p>
          <p className="text-zinc-500 text-xs truncate mb-3">{user.email}</p>
          <button onClick={logout} className="text-xs text-zinc-600 hover:text-red-400 transition">
            Sair
          </button>
        </div>
      </aside>

      <main className="flex-1 min-w-0 flex flex-col">
        <header className="sticky top-0 z-10 bg-zinc-950/80 backdrop-blur border-b border-zinc-900 px-4 lg:px-8 py-4 flex items-center gap-4">
          <button
            onClick={() => setSidebarOpen(true)}
            className="lg:hidden text-zinc-400 hover:text-white transition text-xl leading-none"
          >
            ☰
          </button>

          {contaSelecionada ? (
            <div className="flex items-center gap-3 min-w-0">
              <h1 className="text-white font-semibold truncate">
                {contaSelecionada.descricao || "Conta"}
              </h1>
              <BadgeMoeda moeda={contaSelecionada.moeda} />
            </div>
          ) : (
            <h1 className="text-zinc-500 font-medium">Nenhuma conta selecionada</h1>
          )}

          {contaSelecionada ? (
            <div className="ml-auto flex items-center gap-2 flex-wrap">
              <button
                onClick={() => setModal("deposito")}
                className="bg-emerald-500 hover:bg-emerald-400 text-zinc-950 text-xs font-bold px-3 py-1.5 rounded-lg transition"
              >
                + Depositar
              </button>
              <button
                onClick={() => setModal("saque")}
                className="bg-red-500 hover:bg-red-400 text-white text-xs font-bold px-3 py-1.5 rounded-lg transition"
              >
                - Sacar
              </button>
              <button
                onClick={() => setModal("meta")}
                className="bg-violet-500 hover:bg-violet-400 text-white text-xs font-bold px-3 py-1.5 rounded-lg transition"
              >
                + Meta
              </button>
              <button
                onClick={() => setModal("editar-conta")}
                className="bg-zinc-800 hover:bg-zinc-700 text-zinc-300 text-xs font-medium px-3 py-1.5 rounded-lg transition"
              >
                Editar
              </button>
              <button
                onClick={() => setModal("deletar-conta")}
                className="text-zinc-600 hover:text-red-400 text-sm px-2 py-1.5 rounded-lg transition"
                title="Excluir conta"
              >
                X
              </button>
            </div>
          ) : null}
        </header>

        {!contaSelecionada ? (
          <div className="flex-1 flex items-center justify-center p-8">
            <div className="text-center">
              <p className="text-zinc-500 mb-4">Voce ainda nao tem contas.</p>
              <button
                onClick={() => setModal("nova-conta")}
                className="bg-emerald-500 hover:bg-emerald-400 text-zinc-950 font-semibold px-5 py-2.5 rounded-xl transition text-sm"
              >
                Criar primeira conta
              </button>
            </div>
          </div>
        ) : (
          <div className="flex-1 overflow-y-auto p-4 lg:p-8">
            <div className="grid grid-cols-2 lg:grid-cols-4 gap-3 mb-6">
              <StatCard
                label="Saldo atual"
                value={formatMoeda(contaSelecionada.saldoAtual, contaSelecionada.moeda)}
                sub={`Conta em ${contaSelecionada.moeda}`}
                color="text-emerald-400"
              />
              <StatCard
                label="Despesa mensal"
                value={formatMoeda(contaSelecionada.despesaMensal, contaSelecionada.moeda)}
                sub={`${percentualLimite.toFixed(0)}% do limite`}
                color={
                  percentualLimite >= 90
                    ? "text-red-400"
                    : percentualLimite >= 70
                      ? "text-amber-400"
                      : "text-white"
                }
              />
              <StatCard
                label="Limite mensal"
                value={formatMoeda(contaSelecionada.limiteGastoMensal, contaSelecionada.moeda)}
                sub={`Restam ${formatMoeda(
                  Math.max(contaSelecionada.limiteGastoMensal - contaSelecionada.despesaMensal, 0),
                  contaSelecionada.moeda
                )}`}
              />
              <StatCard
                label="Saldo livre"
                value={formatMoeda(saldoLivre, contaSelecionada.moeda)}
                sub="Saldo - despesas mensais"
                color={saldoLivre < 0 ? "text-red-400" : "text-zinc-300"}
              />
            </div>

            <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4 mb-6">
              <div className="flex items-center justify-between mb-2">
                <p className="text-xs text-zinc-500 uppercase tracking-wider">Limite mensal utilizado</p>
                <span
                  className={`text-xs font-bold font-mono ${
                    percentualLimite >= 90
                      ? "text-red-400"
                      : percentualLimite >= 70
                        ? "text-amber-400"
                        : "text-emerald-400"
                  }`}
                >
                  {percentualLimite.toFixed(1)}%
                </span>
              </div>
              <div className="h-2 bg-zinc-800 rounded-full overflow-hidden">
                <div
                  className={`h-full rounded-full transition-all duration-500 ${
                    percentualLimite >= 90
                      ? "bg-red-500"
                      : percentualLimite >= 70
                        ? "bg-amber-500"
                        : "bg-emerald-500"
                  }`}
                  style={{ width: `${percentualLimite}%` }}
                />
              </div>
              <div className="flex justify-between mt-1.5">
                <span className="text-xs text-zinc-600">
                  {formatMoeda(contaSelecionada.despesaMensal, contaSelecionada.moeda)} gastos
                </span>
                <span className="text-xs text-zinc-600">
                  Limite: {formatMoeda(contaSelecionada.limiteGastoMensal, contaSelecionada.moeda)}
                </span>
              </div>
            </div>

            <div className="flex gap-1 mb-6 bg-zinc-900 border border-zinc-800 rounded-xl p-1 w-fit">
              {(["visao-geral", "depositos", "saques", "metas"] as Tab[]).map((currentTab) => (
                <button
                  key={currentTab}
                  onClick={() => setTab(currentTab)}
                  className={`px-4 py-1.5 rounded-lg text-xs font-medium transition ${
                    tab === currentTab ? "bg-zinc-800 text-white" : "text-zinc-500 hover:text-zinc-300"
                  }`}
                >
                  {currentTab === "visao-geral"
                    ? "Visao geral"
                    : currentTab === "depositos"
                      ? "Depositos"
                      : currentTab === "saques"
                        ? "Saques"
                        : "Metas"}
                </button>
              ))}
            </div>

            {tab === "visao-geral" ? (
              <div className="space-y-5">
                <div className="grid grid-cols-2 gap-3">
                  <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
                    <p className="text-xs text-zinc-500 uppercase tracking-wider mb-3">Total entradas</p>
                    <p className="text-lg font-bold text-emerald-400 font-mono">
                      {formatMoeda(totalDepositos, contaSelecionada.moeda)}
                    </p>
                    <p className="text-xs text-zinc-600 mt-0.5">
                      {depositos.length} deposito{depositos.length !== 1 ? "s" : ""}
                    </p>
                  </div>
                  <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
                    <p className="text-xs text-zinc-500 uppercase tracking-wider mb-3">Total saidas</p>
                    <p className="text-lg font-bold text-red-400 font-mono">
                      {formatMoeda(totalSaques, contaSelecionada.moeda)}
                    </p>
                    <p className="text-xs text-zinc-600 mt-0.5">
                      {saques.length} saque{saques.length !== 1 ? "s" : ""}
                    </p>
                  </div>
                </div>

                {Object.keys(saquesPorCategoria).length > 0 ? (
                  <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
                    <p className="text-xs text-zinc-500 uppercase tracking-wider mb-4">
                      Gastos por categoria
                    </p>
                    <div className="space-y-3">
                      {Object.entries(saquesPorCategoria)
                        .sort(([, a], [, b]) => b - a)
                        .map(([categoria, valor]) => {
                          const percentual = totalSaques > 0 ? (valor / totalSaques) * 100 : 0;
                          return (
                            <div key={categoria}>
                              <div className="flex justify-between mb-1">
                                <span className="text-xs text-zinc-400">
                                  {CATEGORIA_LABELS[categoria] ?? categoria}
                                </span>
                                <span className="text-xs font-mono text-zinc-300">
                                  {formatMoeda(valor, contaSelecionada.moeda)}
                                </span>
                              </div>
                              <div className="h-1.5 bg-zinc-800 rounded-full">
                                <div
                                  className="h-full bg-red-500/70 rounded-full"
                                  style={{ width: `${percentual}%` }}
                                />
                              </div>
                            </div>
                          );
                        })}
                    </div>
                  </div>
                ) : null}
              </div>
            ) : null}

            {tab === "depositos" ? (
              <div className="space-y-3">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-zinc-500 text-sm">
                    {depositos.length} deposito{depositos.length !== 1 ? "s" : ""}
                  </p>
                  <button
                    onClick={() => setModal("deposito")}
                    className="bg-emerald-500 hover:bg-emerald-400 text-zinc-950 text-xs font-bold px-3 py-1.5 rounded-lg transition"
                  >
                    + Novo
                  </button>
                </div>
                {loading ? (
                  <p className="text-zinc-600 text-sm">Carregando...</p>
                ) : depositos.length === 0 ? (
                  <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-8 text-center">
                    <p className="text-zinc-500 text-sm">Nenhum deposito.</p>
                  </div>
                ) : (
                  depositos.map((deposito) => (
                    <div key={deposito.id} className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
                      <div className="flex items-start justify-between">
                        <div className="min-w-0 flex-1">
                          <div className="flex items-center gap-2 mb-2">
                            <span className="w-6 h-6 rounded-full bg-emerald-950 text-emerald-400 flex items-center justify-center text-xs">
                              ↓
                            </span>
                            <p className="text-white font-medium text-sm">{deposito.origem}</p>
                          </div>
                          <div className="flex flex-wrap gap-1.5">
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded">
                              {RECEITA_LABELS[deposito.receita] ?? deposito.receita}
                            </span>
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded">
                              {formatData(deposito.data)}
                            </span>
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded font-mono">
                              {deposito.moeda}
                            </span>
                          </div>
                          {deposito.descricao ? (
                            <p className="text-xs text-zinc-600 mt-2">{deposito.descricao}</p>
                          ) : null}
                        </div>
                        <p className="text-emerald-400 font-mono font-bold text-base ml-4 flex-shrink-0">
                          +{formatMoeda(deposito.valor, deposito.moeda)}
                        </p>
                      </div>
                    </div>
                  ))
                )}
              </div>
            ) : null}

            {tab === "saques" ? (
              <div className="space-y-3">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-zinc-500 text-sm">
                    {saques.length} saque{saques.length !== 1 ? "s" : ""}
                  </p>
                  <button
                    onClick={() => setModal("saque")}
                    className="bg-red-500 hover:bg-red-400 text-white text-xs font-bold px-3 py-1.5 rounded-lg transition"
                  >
                    - Novo
                  </button>
                </div>
                {loading ? (
                  <p className="text-zinc-600 text-sm">Carregando...</p>
                ) : saques.length === 0 ? (
                  <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-8 text-center">
                    <p className="text-zinc-500 text-sm">Nenhum saque.</p>
                  </div>
                ) : (
                  saques.map((saque) => (
                    <div key={saque.id} className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
                      <div className="flex items-start justify-between">
                        <div className="min-w-0 flex-1">
                          <div className="flex items-center gap-2 mb-2">
                            <span className="w-6 h-6 rounded-full bg-red-950 text-red-400 flex items-center justify-center text-xs">
                              ↑
                            </span>
                            <p className="text-white font-medium text-sm">{saque.destino}</p>
                          </div>
                          <div className="flex flex-wrap gap-1.5">
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded">
                              {CATEGORIA_LABELS[saque.categoria] ?? saque.categoria}
                            </span>
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded">
                              {FORMA_LABELS[saque.formaPagamento] ?? saque.formaPagamento}
                            </span>
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded">
                              {formatData(saque.data)}
                            </span>
                            <span className="text-xs bg-zinc-800 text-zinc-400 px-2 py-0.5 rounded font-mono">
                              {saque.moeda}
                            </span>
                          </div>
                          {saque.descricao ? (
                            <p className="text-xs text-zinc-600 mt-2">{saque.descricao}</p>
                          ) : null}
                          {saque.observacao ? (
                            <p className="text-xs text-zinc-700 mt-0.5 italic">{saque.observacao}</p>
                          ) : null}
                        </div>
                        <p className="text-red-400 font-mono font-bold text-base ml-4 flex-shrink-0">
                          -{formatMoeda(saque.valor, saque.moeda)}
                        </p>
                      </div>
                    </div>
                  ))
                )}
              </div>
            ) : null}

            {tab === "metas" ? (
              <div className="space-y-3">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-zinc-500 text-sm">
                    {metas.length} meta{metas.length !== 1 ? "s" : ""}
                  </p>
                  <button
                    onClick={() => setModal("meta")}
                    className="bg-violet-500 hover:bg-violet-400 text-white text-xs font-bold px-3 py-1.5 rounded-lg transition"
                  >
                    + Nova meta
                  </button>
                </div>

                {loading ? (
                  <p className="text-zinc-600 text-sm">Carregando...</p>
                ) : metas.length === 0 ? (
                  <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-8 text-center">
                    <p className="text-zinc-500 text-sm">Nenhuma meta criada para esta conta.</p>
                    <button
                      onClick={() => setModal("meta")}
                      className="mt-3 text-violet-400 hover:text-violet-300 text-sm transition"
                    >
                      Criar primeira meta →
                    </button>
                  </div>
                ) : (
                  metas.map((meta) => {
                    const percentual = metaPercentual(contaSelecionada.saldoAtual, meta.valorObjetivo);
                    const dias = diasRestantes(meta.dataDeConclusao);
                    const concluida = percentual >= 100;

                    return (
                      <div key={meta.id} className="bg-zinc-900 border border-zinc-800 rounded-xl p-4">
                        <div className="flex items-start justify-between mb-3">
                          <div>
                            <p className="text-white font-medium text-sm">{meta.nome}</p>
                            {meta.cargo ? <p className="text-xs text-zinc-600 mt-0.5">{meta.cargo}</p> : null}
                          </div>
                          <div className="flex items-center gap-2">
                            {concluida ? (
                              <span className="text-xs bg-emerald-950 text-emerald-400 border border-emerald-800 px-2 py-0.5 rounded">
                                Atingida
                              </span>
                            ) : null}
                            <button
                              onClick={() => void handleDeleteMeta(meta.id)}
                              className="text-zinc-600 hover:text-red-400 text-xs transition"
                              title="Remover meta"
                            >
                              X
                            </button>
                          </div>
                        </div>

                        <div className="mb-3">
                          <div className="flex justify-between mb-1">
                            <span className="text-xs text-zinc-500">
                              {formatMoeda(contaSelecionada.saldoAtual, contaSelecionada.moeda)} de{" "}
                              {formatMoeda(meta.valorObjetivo, meta.moeda)}
                            </span>
                            <span
                              className={`text-xs font-bold ${
                                concluida ? "text-emerald-400" : "text-violet-400"
                              }`}
                            >
                              {percentual.toFixed(0)}%
                            </span>
                          </div>
                          <div className="h-2 bg-zinc-800 rounded-full overflow-hidden">
                            <div
                              className={`h-full rounded-full transition-all duration-500 ${
                                concluida ? "bg-emerald-500" : "bg-violet-500"
                              }`}
                              style={{ width: `${percentual}%` }}
                            />
                          </div>
                        </div>

                        <div className="flex gap-6">
                          <div>
                            <span className="text-xs text-zinc-600 block">Inicio</span>
                            <p className="text-xs text-zinc-400">{formatData(meta.dataInicio)}</p>
                          </div>
                          <div>
                            <span className="text-xs text-zinc-600 block">Conclusao</span>
                            <p className="text-xs text-zinc-400">
                              {meta.dataDeConclusao ? formatData(meta.dataDeConclusao) : "Sem prazo"}
                            </p>
                          </div>
                          <div>
                            <span className="text-xs text-zinc-600 block">Dias restantes</span>
                            <p
                              className={`text-xs font-medium ${
                                !Number.isFinite(dias)
                                  ? "text-zinc-400"
                                  : dias < 0
                                    ? "text-red-400"
                                    : dias < 7
                                      ? "text-amber-400"
                                      : "text-zinc-400"
                              }`}
                            >
                              {!Number.isFinite(dias)
                                ? "Sem prazo"
                                : dias < 0
                                  ? "Expirada"
                                  : `${dias} dia${dias !== 1 ? "s" : ""}`}
                            </p>
                          </div>
                        </div>
                      </div>
                    );
                  })
                )}
              </div>
            ) : null}
          </div>
        )}
      </main>

      {modal === "deposito" && contaSelecionada ? (
        <ModalDeposito conta={contaSelecionada} onClose={() => setModal(null)} onSuccess={refreshConta} />
      ) : null}

      {modal === "saque" && contaSelecionada ? (
        <ModalSaque conta={contaSelecionada} onClose={() => setModal(null)} onSuccess={refreshConta} />
      ) : null}

      {modal === "editar-conta" && contaSelecionada ? (
        <ModalEditarConta
          conta={contaSelecionada}
          onClose={() => setModal(null)}
          onSuccess={(updated) => {
            setContaSelecionada(updated);
            setContas((prev) => prev.map((conta) => (conta.id === updated.id ? updated : conta)));
          }}
        />
      ) : null}

      {modal === "nova-conta" && user ? (
        <ModalNovaConta
          usuarioId={user.id}
          onClose={() => setModal(null)}
          onSuccess={(nova) => {
            setContas((prev) => [...prev, nova]);
            setTab("visao-geral");
            setContaSelecionada(nova);
          }}
        />
      ) : null}

      {modal === "meta" && contaSelecionada ? (
        <ModalMeta
          conta={contaSelecionada}
          onClose={() => setModal(null)}
          onSuccess={(meta) => setMetas((prev) => [meta, ...prev])}
        />
      ) : null}

      {modal === "deletar-conta" && contaSelecionada ? (
        <ModalDeletarConta
          conta={contaSelecionada}
          onClose={() => setModal(null)}
          onSuccess={() => {
            if (!user) return;
            setContaSelecionada(null);
            void loadContas(user.id);
          }}
        />
      ) : null}
    </div>
  );
}
