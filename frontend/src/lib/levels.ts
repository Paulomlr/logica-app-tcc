import type { Difficulty } from './api'

export const LEVELS: { difficulty: Difficulty; name: string; meta: string; dot: string }[] = [
  { difficulty: 'FACIL', name: 'Fácil', meta: '1 operador · ∧, ∨', dot: 'l1' },
  { difficulty: 'MEDIO', name: 'Médio', meta: '2–3 operadores · ∧, ∨, →', dot: 'l2' },
  { difficulty: 'DIFICIL', name: 'Difícil', meta: '4–5 operadores · ∧, ∨, →, ↔', dot: 'l3' },
  { difficulty: 'AVANCADO', name: 'Avançado', meta: '6–8 operadores · todos', dot: 'l4' },
]

export function findLevel(difficulty: string | undefined) {
  return LEVELS.find((level) => level.difficulty === difficulty)
}
