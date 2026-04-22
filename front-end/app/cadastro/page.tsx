"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useAuth } from "@/hooks/useAuth";
import { api, CriarContaBody, CriarUsuarioBody } from "@/api/api";

type Step = "usuario" | "conta" | "sucesso";

const MOEDAS = [
  { value: "BRL", label: "Real brasileiro (BRL)" },
  { value: "USD", label: "Dólar americano (USD)" },
  { value: "EUR", label: "Euro (EUR)" },
] as const;

export default function CadastroPage() {
  const router = useRouter();
  const { login } = useAuth();

  const [step, setStep] = useState<Step>("usuario");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Usuário
  const [nome, setNome] = useState("");
  const [cpf, setCpf] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [celular, setCelular] = useState("");
  const [cargo, setCargo] = useState("");
  const [salario, setSalario] = useState("");

  // Conta
  const [moeda, setMoeda] = useState<"BRL" | "USD" | "EUR">("BRL");
  const [saldoAtual, setSaldoAtual] = useState("");
  const [limiteGastoMensal, setLimiteGastoMensal] = useState("");
  const [descricao, setDescricao] = useState("");

  const [usuarioCriado, setUsuarioCriado] = useState<{ id: string; nome: string; email: string } | null>(null);

  async function handleCriarUsuario(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const body: CriarUsuarioBody = {
        nome,
        cpf,
        email,
        senha,
        celular: celular || undefined,
        cargo: cargo || undefined,
        salario: parseFloat(salario),
      };

      const usuario = await api.usuarios.criar(body);
      setUsuarioCriado({ id: usuario.id, nome: usuario.nome, email: usuario.email });
      setStep("conta");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao criar usuário.");
    } finally {
      setLoading(false);
    }
  }

  async function handleCriarConta(e: React.FormEvent) {
    e.preventDefault();
    if (!usuarioCriado) return;
    setError("");
    setLoading(true);

    try {
      const body: CriarContaBody = {
        usuarioId: usuarioCriado.id,
        moeda,
        saldoAtual: parseFloat(saldoAtual) || 0,
        limiteGastoMensal: parseFloat(limiteGastoMensal),
        descricao: descricao || undefined,
      };

      await api.contas.criar(body);
      login(usuarioCriado);
      setStep("sucesso");

      setTimeout(() => {
        router.push("/dashboard");
      }, 2000);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Erro ao criar conta.");
    } finally {
      setLoading(false);
    }
  }

  const inputClass =
    "w-full bg-zinc-800 border border-zinc-700 text-white rounded-lg px-3.5 py-2.5 text-sm placeholder-zinc-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition";
  const labelClass =
    "block text-xs text-zinc-400 mb-1.5 font-medium uppercase tracking-wider";

  return (
    <div className="min-h-screen bg-zinc-950 flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        {/* Brand */}
        <div className="mb-10 text-center">
          <span className="inline-block text-3xl font-bold tracking-tight text-white font-mono">
            fin<span className="text-emerald-400">.</span>
          </span>
          <p className="mt-2 text-zinc-500 text-sm">Gestão financeira pessoal</p>
        </div>

        {/* Steps indicator */}
        {step !== "sucesso" && (
          <div className="flex items-center gap-2 mb-6">
            <div className={`flex-1 h-1 rounded-full ${step === "usuario" || step === "conta" ? "bg-emerald-500" : "bg-zinc-800"}`} />
            <div className={`flex-1 h-1 rounded-full ${step === "conta" ? "bg-emerald-500" : "bg-zinc-800"}`} />
          </div>
        )}

        {/* Card */}
        <div className="bg-zinc-900 border border-zinc-800 rounded-2xl p-8">

          {/* Step 1: Usuário */}
          {step === "usuario" && (
            <>
              <div className="mb-6">
                <p className="text-xs text-zinc-500 uppercase tracking-wider mb-1">Passo 1 de 2</p>
                <h1 className="text-white text-lg font-semibold">Seus dados</h1>
              </div>

              <form onSubmit={handleCriarUsuario} className="space-y-4">
                <div>
                  <label className={labelClass}>Nome</label>
                  <input
                    value={nome}
                    onChange={(e) => setNome(e.target.value)}
                    required
                    className={inputClass}
                    placeholder="João Silva"
                  />
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className={labelClass}>CPF</label>
                    <input
                      value={cpf}
                      onChange={(e) => setCpf(e.target.value)}
                      required
                      className={inputClass}
                      placeholder="000.000.000-00"
                    />
                  </div>
                  <div>
                    <label className={labelClass}>Celular</label>
                    <input
                      value={celular}
                      onChange={(e) => setCelular(e.target.value)}
                      className={inputClass}
                      placeholder="(11) 99999-0000"
                    />
                  </div>
                </div>

                <div>
                  <label className={labelClass}>E-mail</label>
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    className={inputClass}
                    placeholder="seu@email.com"
                  />
                </div>

                <div>
                  <label className={labelClass}>Senha</label>
                  <input
                    type="password"
                    value={senha}
                    onChange={(e) => setSenha(e.target.value)}
                    required
                    className={inputClass}
                    placeholder="••••••••"
                  />
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className={labelClass}>Cargo</label>
                    <input
                      value={cargo}
                      onChange={(e) => setCargo(e.target.value)}
                      className={inputClass}
                      placeholder="Analista"
                    />
                  </div>
                  <div>
                    <label className={labelClass}>Salário</label>
                    <input
                      type="number"
                      min="0"
                      step="0.01"
                      value={salario}
                      onChange={(e) => setSalario(e.target.value)}
                      required
                      className={inputClass}
                      placeholder="0,00"
                    />
                  </div>
                </div>

                {error && (
                  <p className="text-red-400 text-xs bg-red-950/40 border border-red-900 rounded-lg px-3 py-2">
                    {error}
                  </p>
                )}

                <button
                  type="submit"
                  disabled={loading}
                  className="w-full bg-emerald-500 hover:bg-emerald-400 disabled:opacity-50 disabled:cursor-not-allowed text-zinc-950 font-semibold text-sm rounded-lg py-2.5 transition mt-2"
                >
                  {loading ? "Criando..." : "Continuar →"}
                </button>
              </form>
            </>
          )}

          {/* Step 2: Conta */}
          {step === "conta" && (
            <>
              <div className="mb-6">
                <p className="text-xs text-zinc-500 uppercase tracking-wider mb-1">Passo 2 de 2</p>
                <h1 className="text-white text-lg font-semibold">Sua primeira conta</h1>
                <p className="text-zinc-500 text-xs mt-1">Você pode criar mais contas depois</p>
              </div>

              <form onSubmit={handleCriarConta} className="space-y-4">
                <div>
                  <label className={labelClass}>Moeda</label>
                  <select
                    value={moeda}
                    onChange={(e) => setMoeda(e.target.value as "BRL" | "USD" | "EUR")}
                    className={inputClass}
                  >
                    {MOEDAS.map((m) => (
                      <option key={m.value} value={m.value}>
                        {m.label}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className={labelClass}>Saldo atual</label>
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
                  <label className={labelClass}>Limite mensal de gastos</label>
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

                <div>
                  <label className={labelClass}>Descrição <span className="normal-case text-zinc-600">(opcional)</span></label>
                  <input
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                    className={inputClass}
                    placeholder="Ex: Conta principal"
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
                  className="w-full bg-emerald-500 hover:bg-emerald-400 disabled:opacity-50 disabled:cursor-not-allowed text-zinc-950 font-semibold text-sm rounded-lg py-2.5 transition mt-2"
                >
                  {loading ? "Criando conta..." : "Criar conta"}
                </button>
              </form>
            </>
          )}

          {/* Sucesso */}
          {step === "sucesso" && (
            <div className="text-center py-4">
              <div className="w-12 h-12 bg-emerald-500/10 border border-emerald-500/30 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-emerald-400 text-xl">✓</span>
              </div>
              <h1 className="text-white font-semibold text-lg">Conta criada!</h1>
              <p className="text-zinc-500 text-sm mt-2">Redirecionando para o dashboard...</p>
            </div>
          )}
        </div>

        {step === "usuario" && (
          <p className="text-center text-zinc-600 text-sm mt-6">
            Já tem conta?{" "}
            <Link href="/login" className="text-emerald-400 hover:text-emerald-300 transition">
              Entrar
            </Link>
          </p>
        )}
      </div>
    </div>
  );
}
