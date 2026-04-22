"use client";

import { api, Conta, Moeda, Receita } from "@/api/api";
import { useState } from "react";
import Modal from "./modal";

const RECEITAS: { value: Receita; label: string }[] = [
  { value: "SALARIO", label: "Salário" },
  { value: "FREELANCE", label: "Freelance" },
  { value: "INVESTIMENTO", label: "Investimento" },
  { value: "ALUGUEL", label: "Aluguel" },
  { value: "OUTROS", label: "Outros" },
];

const MOEDAS: Moeda[] = ["BRL", "USD", "EUR"];

type Props = {
  conta: Conta;
  onClose: () => void;
  onSuccess: () => void;
};

const inputClass =
  "w-full bg-zinc-800 border border-zinc-700 text-white rounded-lg px-3.5 py-2.5 text-sm placeholder-zinc-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition";
const labelClass = "block text-xs text-zinc-400 mb-1.5 font-medium uppercase tracking-wider";

export default function ModalDeposito({ conta, onClose, onSuccess }: Props) {
  const [valor, setValor] = useState("");
  const [moeda, setMoeda] = useState<Moeda>(conta.moeda);
  const [origem, setOrigem] = useState("");
  const [receita, setReceita] = useState<Receita>("SALARIO");
  const [descricao, setDescricao] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await api.depositos.criar({
        contaId: conta.id,
        valor: parseFloat(valor),
        moeda,
        origem,
        receita,
        descricao: descricao || undefined,
      });
      onSuccess();
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao realizar depósito.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal title="Novo depósito" onClose={onClose}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className={labelClass}>Valor</label>
            <input
              type="number"
              min="0.01"
              step="0.01"
              value={valor}
              onChange={(e) => setValor(e.target.value)}
              required
              className={inputClass}
              placeholder="0,00"
            />
          </div>
          <div>
            <label className={labelClass}>Moeda</label>
            <select value={moeda} onChange={(e) => setMoeda(e.target.value as Moeda)} className={inputClass}>
              {MOEDAS.map((m) => (
                <option key={m} value={m}>{m}</option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label className={labelClass}>Origem</label>
          <input
            value={origem}
            onChange={(e) => setOrigem(e.target.value)}
            required
            className={inputClass}
            placeholder="Ex: Empresa X"
          />
        </div>

        <div>
          <label className={labelClass}>Tipo de receita</label>
          <select value={receita} onChange={(e) => setReceita(e.target.value as Receita)} className={inputClass}>
            {RECEITAS.map((r) => (
              <option key={r.value} value={r.value}>{r.label}</option>
            ))}
          </select>
        </div>

        <div>
          <label className={labelClass}>Descrição <span className="normal-case text-zinc-600">(opcional)</span></label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            className={inputClass}
            placeholder="Ex: Pagamento mensal"
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
          {loading ? "Depositando..." : "Confirmar depósito"}
        </button>
      </form>
    </Modal>
  );
}
