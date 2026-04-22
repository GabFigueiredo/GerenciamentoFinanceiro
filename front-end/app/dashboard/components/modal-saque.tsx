"use client";

import { api, Categoria, Conta, FormaPagamento, Moeda } from "@/api/api";
import { useState } from "react";
import Modal from "./modal";

const CATEGORIAS: { value: Categoria; label: string }[] = [
  { value: "ALIMENTACAO", label: "Alimentação" },
  { value: "TRANSPORTE", label: "Transporte" },
  { value: "SAUDE", label: "Saúde" },
  { value: "EDUCACAO", label: "Educação" },
  { value: "LAZER", label: "Lazer" },
  { value: "MORADIA", label: "Moradia" },
  { value: "OUTROS", label: "Outros" },
];

const FORMAS: { value: FormaPagamento; label: string }[] = [
  { value: "PIX", label: "PIX" },
  { value: "CARTAO_CREDITO", label: "Cartão de crédito" },
  { value: "CARTAO_DEBITO", label: "Cartão de débito" },
  { value: "DINHEIRO", label: "Dinheiro" },
  { value: "TRANSFERENCIA", label: "Transferência" },
  { value: "BOLETO", label: "Boleto" },
];

const MOEDAS: Moeda[] = ["BRL", "USD", "EUR"];

type Props = {
  conta: Conta;
  onClose: () => void;
  onSuccess: () => void;
};

const inputClass =
  "w-full bg-zinc-800 border border-zinc-700 text-white rounded-lg px-3.5 py-2.5 text-sm placeholder-zinc-600 focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500 transition";
const labelClass = "block text-xs text-zinc-400 mb-1.5 font-medium uppercase tracking-wider";

export default function ModalSaque({ conta, onClose, onSuccess }: Props) {
  const [valor, setValor] = useState("");
  const [moeda, setMoeda] = useState<Moeda>(conta.moeda);
  const [destino, setDestino] = useState("");
  const [categoria, setCategoria] = useState<Categoria>("ALIMENTACAO");
  const [formaPagamento, setFormaPagamento] = useState<FormaPagamento>("PIX");
  const [descricao, setDescricao] = useState("");
  const [observacao, setObservacao] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await api.saques.criar({
        contaId: conta.id,
        valor: parseFloat(valor),
        moeda,
        destino,
        categoria,
        formaPagamento,
        descricao: descricao || undefined,
        observacao: observacao || undefined,
      });
      onSuccess();
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao realizar saque.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal title="Novo saque" onClose={onClose}>
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
              {MOEDAS.map((m) => (<option key={m} value={m}>{m}</option>))}
            </select>
          </div>
        </div>

        <div>
          <label className={labelClass}>Destino</label>
          <input
            value={destino}
            onChange={(e) => setDestino(e.target.value)}
            required
            className={inputClass}
            placeholder="Ex: Supermercado"
          />
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className={labelClass}>Categoria</label>
            <select value={categoria} onChange={(e) => setCategoria(e.target.value as Categoria)} className={inputClass}>
              {CATEGORIAS.map((c) => (<option key={c.value} value={c.value}>{c.label}</option>))}
            </select>
          </div>
          <div>
            <label className={labelClass}>Forma de pagamento</label>
            <select value={formaPagamento} onChange={(e) => setFormaPagamento(e.target.value as FormaPagamento)} className={inputClass}>
              {FORMAS.map((f) => (<option key={f.value} value={f.value}>{f.label}</option>))}
            </select>
          </div>
        </div>

        <div>
          <label className={labelClass}>Descrição <span className="normal-case text-zinc-600">(opcional)</span></label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            className={inputClass}
            placeholder="Ex: Compras do mês"
          />
        </div>

        <div>
          <label className={labelClass}>Observação <span className="normal-case text-zinc-600">(opcional)</span></label>
          <input
            value={observacao}
            onChange={(e) => setObservacao(e.target.value)}
            className={inputClass}
            placeholder="Ex: Compra semanal"
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
          className="w-full bg-red-500 hover:bg-red-400 disabled:opacity-50 text-white font-semibold text-sm rounded-lg py-2.5 transition"
        >
          {loading ? "Sacando..." : "Confirmar saque"}
        </button>
      </form>
    </Modal>
  );
}
