"use client";

import { api, Conta, Meta } from "@/api/api";
import { useState } from "react";
import Modal from "./modal";

type Props = {
  conta: Conta;
  onClose: () => void;
  onSuccess: (meta: Meta) => void;
};

const inputClass =
  "w-full bg-zinc-800 border border-zinc-700 text-white rounded-lg px-3.5 py-2.5 text-sm placeholder-zinc-600 focus:outline-none focus:border-violet-500 focus:ring-1 focus:ring-violet-500 transition";
const labelClass = "block text-xs text-zinc-400 mb-1.5 font-medium uppercase tracking-wider";

export default function ModalMeta({ conta, onClose, onSuccess }: Props) {
  const [nome, setNome] = useState("");
  const [valorObjetivo, setValorObjetivo] = useState("");
  const [cargo, setCargo] = useState("");
  const [dataInicio, setDataInicio] = useState(new Date().toISOString().split("T")[0]);
  const [dataDeConclusao, setDataDeConclusao] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");

    if (!valorObjetivo || Number(valorObjetivo) <= 0) {
      setError("Informe um valor objetivo valido.");
      return;
    }

    if (dataDeConclusao && dataDeConclusao < dataInicio) {
      setError("A data de conclusao nao pode ser anterior a data de inicio.");
      return;
    }

    setLoading(true);

    try {
      const meta = await api.metas.criar(conta.id, {
        nome,
        valorObjetivo: Number(valorObjetivo),
        cargo: cargo || undefined,
        dataInicio,
        dataDeConclusao: dataDeConclusao || undefined,
      });

      onSuccess(meta);
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao criar meta.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal title="Nova meta" onClose={onClose}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className={labelClass}>Nome da meta</label>
          <input
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
            className={inputClass}
            placeholder="Ex: Reserva de emergencia"
          />
        </div>

        <div>
          <label className={labelClass}>Valor objetivo</label>
          <input
            type="number"
            min="0.01"
            step="0.01"
            value={valorObjetivo}
            onChange={(e) => setValorObjetivo(e.target.value)}
            required
            className={inputClass}
            placeholder="0,00"
          />
        </div>

        <div>
          <label className={labelClass}>Moeda da meta</label>
          <div className="w-full bg-zinc-900 border border-zinc-800 text-zinc-300 rounded-lg px-3.5 py-2.5 text-sm">
            {conta.moeda}
          </div>
          <p className="text-xs text-zinc-600 mt-1">A meta usa automaticamente a moeda da conta.</p>
        </div>

        <div>
          <label className={labelClass}>
            Cargo / Categoria <span className="normal-case text-zinc-600">(opcional)</span>
          </label>
          <input
            value={cargo}
            onChange={(e) => setCargo(e.target.value)}
            className={inputClass}
            placeholder="Ex: Planejamento"
          />
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className={labelClass}>Data de inicio</label>
            <input
              type="date"
              value={dataInicio}
              onChange={(e) => setDataInicio(e.target.value)}
              required
              className={inputClass}
            />
          </div>
          <div>
            <label className={labelClass}>
              Data de conclusao <span className="normal-case text-zinc-600">(opcional)</span>
            </label>
            <input
              type="date"
              value={dataDeConclusao}
              onChange={(e) => setDataDeConclusao(e.target.value)}
              className={inputClass}
            />
          </div>
        </div>

        {error ? (
          <p className="text-red-400 text-xs bg-red-950/40 border border-red-900 rounded-lg px-3 py-2">
            {error}
          </p>
        ) : null}

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-violet-500 hover:bg-violet-400 disabled:opacity-50 text-white font-semibold text-sm rounded-lg py-2.5 transition"
        >
          {loading ? "Criando..." : "Criar meta"}
        </button>
      </form>
    </Modal>
  );
}
