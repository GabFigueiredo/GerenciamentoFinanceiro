"use client";

import { api, Conta, Moeda } from "@/api/api";
import { useState } from "react";
import Modal from "./modal";

const MOEDAS: { value: Moeda; label: string }[] = [
  { value: "BRL", label: "Real (BRL)" },
  { value: "USD", label: "Dólar (USD)" },
  { value: "EUR", label: "Euro (EUR)" },
];

type Props = {
  usuarioId: string;
  onClose: () => void;
  onSuccess: (conta: Conta) => void;
};

const inputClass =
  "w-full bg-zinc-800 border border-zinc-700 text-white rounded-lg px-3.5 py-2.5 text-sm placeholder-zinc-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition";
const labelClass = "block text-xs text-zinc-400 mb-1.5 font-medium uppercase tracking-wider";

export default function ModalNovaConta({ usuarioId, onClose, onSuccess }: Props) {
  const [moeda, setMoeda] = useState<Moeda>("BRL");
  const [saldoAtual, setSaldoAtual] = useState("0");
  const [limiteGastoMensal, setLimiteGastoMensal] = useState("");
  const [descricao, setDescricao] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const conta = await api.contas.criar({
        usuarioId,
        moeda,
        saldoAtual: parseFloat(saldoAtual) || 0,
        limiteGastoMensal: parseFloat(limiteGastoMensal),
        descricao: descricao || undefined,
      });
      onSuccess(conta);
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao criar conta.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal title="Nova conta" onClose={onClose}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className={labelClass}>Moeda</label>
          <select value={moeda} onChange={(e) => setMoeda(e.target.value as Moeda)} className={inputClass}>
            {MOEDAS.map((m) => (<option key={m.value} value={m.value}>{m.label}</option>))}
          </select>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className={labelClass}>Saldo inicial</label>
            <input
              type="number"
              min="0"
              step="0.01"
              value={saldoAtual}
              onChange={(e) => setSaldoAtual(e.target.value)}
              required
              className={inputClass}
              placeholder="0,00"
            />
          </div>
          <div>
            <label className={labelClass}>Limite mensal</label>
            <input
              type="number"
              min="0"
              step="0.01"
              value={limiteGastoMensal}
              onChange={(e) => setLimiteGastoMensal(e.target.value)}
              required
              className={inputClass}
              placeholder="0,00"
            />
          </div>
        </div>

        <div>
          <label className={labelClass}>Descrição <span className="normal-case text-zinc-600">(opcional)</span></label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            className={inputClass}
            placeholder="Ex: Conta poupança"
          />
        </div>

        {error && (
          <p className="text-red-400 text-xs bg-red-950/40 border border-red-900 rounded-lg px-3 py-2">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-emerald-500 hover:bg-emerald-400 disabled:opacity-50 text-zinc-950 font-semibold text-sm rounded-lg py-2.5 transition"
        >
          {loading ? "Criando..." : "Criar conta"}
        </button>
      </form>
    </Modal>
  );
}
