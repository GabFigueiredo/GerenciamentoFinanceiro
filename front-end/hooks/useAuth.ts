"use client";

import { useRouter } from "next/navigation";

export type AuthUser = {
  id: string;
  nome: string;
  email: string;
};

const KEY = "finance_user";

export function useAuth() {
  const router = useRouter();

  function getUser(): AuthUser | null {
    if (typeof window === "undefined") return null;
    const raw = localStorage.getItem(KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthUser;
    } catch {
      return null;
    }
  }

  function login(user: AuthUser) {
    localStorage.setItem(KEY, JSON.stringify(user));
  }

  function logout() {
    localStorage.removeItem(KEY);
    router.push("/login");
  }

  function requireAuth(): AuthUser {
    const user = getUser();
    if (!user) {
      router.push("/login");
      throw new Error("Não autenticado");
    }
    return user;
  }

  return { getUser, login, logout, requireAuth };
}