export type Difficulty = 'FACIL' | 'MEDIO' | 'DIFICIL' | 'AVANCADO'

export type LogicExercise = {
  id: number
  formula: string
  difficulty: Difficulty
  createdAt: string
}

export type ExercisePlayView = {
  exerciseId: number
  formula: string
  variables: string[]
  columnLabels: string[]
  columnIsFillable: boolean[]
  rowAssignments: Record<string, boolean>[]
}

export type Achievement = {
  id: number
  code: string
  title: string
  description: string
}

export type AttemptResultResponse = {
  correct: boolean
  correctness: boolean[][]
  correctAnswers: boolean[][]
  newlyUnlockedAchievements: Achievement[]
}

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  })
  if (!res.ok) {
    const body = await res.text().catch(() => '')
    throw new Error(`Falha na API (${res.status}): ${body || res.statusText}`)
  }
  if (res.status === 204) {
    return undefined as T
  }
  return (await res.json()) as T
}

export function listExercises(difficulty: Difficulty): Promise<LogicExercise[]> {
  return request(`/api/exercises?difficulty=${difficulty}`)
}

/** Pass null difficulty for a "surprise" exercise at a randomly picked level. */
export function generateExercise(difficulty: Difficulty | null): Promise<LogicExercise[]> {
  const query = difficulty ? `?difficulty=${difficulty}&count=1` : '?count=1'
  return request(`/api/exercises/generate${query}`, { method: 'POST' })
}

export function getPlayView(exerciseId: number): Promise<ExercisePlayView> {
  return request(`/api/exercises/${exerciseId}/play`)
}

export function submitAttempt(
  exerciseId: number,
  answers: boolean[][],
  timeSpentSeconds: number,
): Promise<AttemptResultResponse> {
  return request(`/api/exercises/${exerciseId}/attempts`, {
    method: 'POST',
    body: JSON.stringify({ answers, timeSpentSeconds }),
  })
}
