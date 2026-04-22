"use client";

import { api, Conta } from "@/api/api";
import { useState } from "react";
import Modal from "./modal";

type Props = {
  conta: Conta;
  onClose: () => void;
  onSuccess: () => void;
};

export default function ModalDeletarConta({ conta, onClose, onSuccess }: Props) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleDelete() {
    setError("");
    setLoading(true);
    try {
      await api.contas.deletar(conta.id);
      onSuccess();
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao deletar conta.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal title="Excluir conta" onClose={onClose}>
      <div className="space-y-4">
        <p className="text-zinc-300 text-sm">
          Tem certeza que deseja excluir a conta{" "}
          <span className="text-white font-medium">
            {conta.descricao || conta.moeda}
          </span>
          ? Esta ação não pode ser desfeita.
        </p>

        {conta.saldoAtual > 0 && (
          <div className="bg-amber-950/40 border border-amber-800 rounded-lg px-3 py-2">
            <p className="text-amber-400 text-xs">
              Esta conta ainda possui saldo. Certifique-se de transferi-lo antes de continuar.
            </p>
          </div>
        )}

        {error && (
          <p className="text-red-400 text-xs bg-red-950/40 border border-red-900 rounded-lg px-3 py-2">
            {error}
          </p>
        )}

        <div className="flex gap-3">
          <button
            onClick={onClose}
            className="flex-1 bg-zinc-800 hover:bg-zinc-700 text-zinc-300 font-medium text-sm rounded-lg py-2.5 transition"
          >
            Cancelar
          </button>
          <button
            onClick={handleDelete}
            disabled={loading}
            className="flex-1 bg-red-500 hover:bg-red-400 disabled:opacity-50 text-white font-semibold text-sm rounded-lg py-2.5 transition"
          >
            {loading ? "Excluindo..." : "Excluir conta"}
          </button>
        </div>
      </div>
    </Modal>
  );
}
