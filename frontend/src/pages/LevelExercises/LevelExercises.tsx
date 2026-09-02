import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { generateExercise, listExercises, type Difficulty, type LogicExercise } from '../../lib/api'
import { findLevel } from '../../lib/levels'

function LevelExercises() {
  const { difficulty } = useParams()
  const navigate = useNavigate()
  const level = findLevel(difficulty)

  const [exercises, setExercises] = useState<LogicExercise[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!level) return
    let cancelled = false
    setLoading(true)
    setError(null)
    listExercises(level.difficulty as Difficulty)
      .then((list) => (list.length > 0 ? list : generateExercise(level.difficulty as Difficulty)))
      .then((list) => {
        if (!cancelled) setExercises(list)
      })
      .catch((err: Error) => !cancelled && setError(err.message))
      .finally(() => !cancelled && setLoading(false))
    return () => {
      cancelled = true
    }
  }, [level])

  if (!level) {
    return (
      <main className="screen levels practice-message">
        <p>Nível não encontrado.</p>
        <button type="button" className="btn btn-ghost" onClick={() => navigate('/niveis')}>
          Voltar aos níveis
        </button>
      </main>
    )
  }

  return (
    <main className="screen levels">
      <div className="list-top">
        <button type="button" className="back" onClick={() => navigate('/niveis')}>
          ‹ Níveis
        </button>
        <div className="titles">
          <div className="screen-title">{level.name}</div>
          <div className="sub">{level.meta}</div>
        </div>
      </div>

      {error && <p className="levels-error">{error}</p>}
      {loading && <p className="hint">Carregando exercícios…</p>}

      {!loading &&
        exercises.map((exercise, index) => (
          <button
            type="button"
            key={exercise.id}
            className="level-card"
            onClick={() => navigate(`/pratica/${exercise.id}`)}
          >
            <div className="lv-info">
              <div className="lv-name">Exercício {index + 1}</div>
              <div className="lv-formula">{exercise.formula}</div>
            </div>
            <div className={`lv-dot ${level.dot}`} />
          </button>
        ))}

      {!loading && !error && exercises.length === 0 && (
        <p className="hint">Nenhum exercício disponível neste nível ainda.</p>
      )}
    </main>
  )
}

export default LevelExercises
