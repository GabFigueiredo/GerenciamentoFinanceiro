"use client";

import { useState } from "react";
import Modal from "./modal";
import { api, Conta } from "@/api/api";

type Props = {
  conta: Conta;
  onClose: () => void;
  onSuccess: (updated: Conta) => void;
};

const inputClass =
  "w-full bg-zinc-800 border border-zinc-700 text-white rounded-lg px-3.5 py-2.5 text-sm placeholder-zinc-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition";
const labelClass = "block text-xs text-zinc-400 mb-1.5 font-medium uppercase tracking-wider";

export default function ModalEditarConta({ conta, onClose, onSuccess }: Props) {
  const [limite, setLimite] = useState(String(conta.limiteGastoMensal));
  const [descricao, setDescricao] = useState(conta.descricao ?? "");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const updated = await api.contas.atualizar(conta.id, {
        limiteGastoMensal: parseFloat(limite),
        descricao: descricao || undefined,
      });
      onSuccess(updated);
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao atualizar conta.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal title="Editar conta" onClose={onClose}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className={labelClass}>Limite mensal de gastos</label>
          <input
            type="number"
            min="0"
            step="0.01"
            value={limite}
            onChange={(e) => setLimite(e.target.value)}
            required
            className={inputClass}
          />
        </div>

        <div>
          <label className={labelClass}>Descrição</label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            className={inputClass}
            placeholder="Ex: Conta principal"
          />
        </div>

        <p className="text-xs text-zinc-600">
          Moeda e saldo não são editáveis diretamente — use depósitos e saques para alterar o saldo.
        </p>

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
          {loading ? "Salvando..." : "Salvar alterações"}
        </button>
      </form>
    </Modal>
  );
}
