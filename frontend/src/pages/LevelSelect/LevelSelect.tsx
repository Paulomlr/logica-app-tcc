import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Formula from '../../components/Formula'
import { generateExercise } from '../../lib/api'
import { LEVELS } from '../../lib/levels'
import './LevelSelect.css'

function LevelSelect() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function generateRandomChallenge() {
    setError(null)
    setLoading(true)
    try {
      const [exercise] = await generateExercise(null)
      navigate(`/pratica/${exercise.id}`)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Não foi possível gerar um desafio.')
    } finally {
      setLoading(false)
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
          disabled={loading}
          onClick={() => navigate(`/niveis/${level.difficulty}`)}
        >
          <div>
            <div className="lv-name">{level.name}</div>
            <div className="lv-meta">
              <Formula text={level.meta} />
            </div>
          </div>
          <div className={`lv-dot ${level.dot}`} />
        </button>
      ))}

      <div className="level-footer">
        <button
          type="button"
          className="btn btn-ghost"
          disabled={loading}
          onClick={generateRandomChallenge}
        >
          {loading ? 'Gerando…' : '↻ Gerar novo desafio'}
        </button>
        <p className="hint">Sorteia a dificuldade e a fórmula, de Fácil a Avançado</p>
      </div>
    </main>
  )
}

export default LevelSelect
