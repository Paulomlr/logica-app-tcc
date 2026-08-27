import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { generateExercise, listExercises, type Difficulty } from '../../lib/api'
import './LevelSelect.css'

const LEVELS: { difficulty: Difficulty; name: string; meta: string; dot: string }[] = [
  { difficulty: 'FACIL', name: 'Fácil', meta: '1 operador · ∧, ∨', dot: 'l1' },
  { difficulty: 'MEDIO', name: 'Médio', meta: '2–3 operadores · ∧, ∨, →', dot: 'l2' },
  { difficulty: 'DIFICIL', name: 'Difícil', meta: '4–5 operadores · ∧, ∨, →, ↔', dot: 'l3' },
  { difficulty: 'AVANCADO', name: 'Avançado', meta: '6–8 operadores · todos', dot: 'l4' },
]

function pickRandom<T>(items: T[]): T {
  return items[Math.floor(Math.random() * items.length)]
}

function LevelSelect() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState<Difficulty | 'random' | null>(null)
  const [error, setError] = useState<string | null>(null)

  async function playLevel(difficulty: Difficulty) {
    setError(null)
    setLoading(difficulty)
    try {
      let exercises = await listExercises(difficulty)
      if (exercises.length === 0) {
        exercises = await generateExercise(difficulty)
      }
      const exercise = pickRandom(exercises)
      navigate(`/pratica/${exercise.id}`)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Não foi possível carregar exercícios.')
    } finally {
      setLoading(null)
    }
  }

  async function generateRandomChallenge() {
    setError(null)
    setLoading('random')
    try {
      const [exercise] = await generateExercise(null)
      navigate(`/pratica/${exercise.id}`)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Não foi possível gerar um desafio.')
    } finally {
      setLoading(null)
    }
  }

  return (
    <main className="screen levels">
      <div className="list-top">
        <button type="button" className="back" onClick={() => navigate('/home')}>
          ‹ Início
        </button>
        <div className="titles">
          <div className="screen-title">Escolher nível</div>
        </div>
      </div>

      {error && <p className="levels-error">{error}</p>}

      {LEVELS.map((level) => (
        <button
          type="button"
          key={level.difficulty}
          className="level-card"
          disabled={loading !== null}
          onClick={() => playLevel(level.difficulty)}
        >
          <div>
            <div className="lv-name">{level.name}</div>
            <div className="lv-meta">
              {loading === level.difficulty ? 'Carregando…' : level.meta}
            </div>
          </div>
          <div className={`lv-dot ${level.dot}`} />
        </button>
      ))}

      <div className="level-footer">
        <button
          type="button"
          className="btn btn-ghost"
          disabled={loading !== null}
          onClick={generateRandomChallenge}
        >
          {loading === 'random' ? 'Gerando…' : '↻ Gerar novo desafio'}
        </button>
        <p className="hint">Sorteia a dificuldade e a fórmula, de Fácil a Avançado</p>
      </div>
    </main>
  )
}

export default LevelSelect
