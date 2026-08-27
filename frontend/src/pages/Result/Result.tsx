import { useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { StarFilledIcon } from '../../components/icons/Icons'
import type { AttemptResultResponse, ExercisePlayView } from '../../lib/api'
import './Result.css'

type CellValue = '' | 'v' | 'f'
type ResultState = {
  play: ExercisePlayView
  result: AttemptResultResponse
  answers: CellValue[][]
  seconds: number
}

function vf(value: boolean) {
  return value ? 'V' : 'F'
}

function formatTime(totalSeconds: number) {
  const m = Math.floor(totalSeconds / 60)
  const s = totalSeconds % 60
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

function Result() {
  const navigate = useNavigate()
  const { state } = useLocation()
  const resultState = state as ResultState | null

  useEffect(() => {
    if (!resultState) {
      navigate('/pratica', { replace: true })
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  if (!resultState) {
    return null
  }

  const { play, result, answers, seconds } = resultState

  const fillableSlotForColumn: number[] = []
  let slotCounter = 0
  for (const isFillable of play.columnIsFillable) {
    fillableSlotForColumn.push(isFillable ? slotCounter++ : -1)
  }

  const totalCells = result.correctness.reduce((sum, row) => sum + row.length, 0)
  const correctCells = result.correctness.reduce(
    (sum, row) => sum + row.filter(Boolean).length,
    0,
  )

  const achievement = result.newlyUnlockedAchievements[0]

  return (
    <main className="screen result">
      <div className="list-top">
        <button type="button" className="back" onClick={() => navigate('/niveis')}>
          ‹ Nível
        </button>
        <div className="titles">
          <div className="screen-title">Resultado</div>
        </div>
      </div>

      <div className="result-banner">
        <b>
          {correctCells} de {totalCells} corretas
        </b>
        <span>Revisão célula a célula abaixo</span>
      </div>

      <table className="tt">
        <thead>
          <tr>
            {play.columnLabels.map((label, c) => (
              <th key={label} className={play.columnIsFillable[c] ? 'fillable' : undefined}>
                {label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {play.rowAssignments.map((assignment, r) => (
            <tr key={r}>
              {play.columnLabels.map((label, c) => {
                if (!play.columnIsFillable[c]) {
                  return (
                    <td key={label} className="cell-given">
                      {assignment[label] ? 'V' : 'F'}
                    </td>
                  )
                }
                const slot = fillableSlotForColumn[c]
                const isOk = result.correctness[r][slot]
                const answer = answers[r][slot]
                return (
                  <td key={label} className={isOk ? 'tag-ok' : 'tag-bad'}>
                    {answer ? vf(answer === 'v') : '–'}
                    {!isOk && '*'}
                  </td>
                )
              })}
            </tr>
          ))}
        </tbody>
      </table>

      {achievement && (
        <div className="achieve-card">
          <div className="achieve-badge">
            <StarFilledIcon />
          </div>
          <div>
            <b>Conquista: {achievement.title}</b>
            <span>{achievement.description}</span>
          </div>
        </div>
      )}

      <div className="score-line">Tempo: {formatTime(seconds)}</div>

      <div className="actions">
        <button type="button" className="btn btn-primary" onClick={() => navigate('/niveis')}>
          Próximo exercício
        </button>
        <button type="button" className="btn btn-ghost" onClick={() => navigate('/niveis')}>
          Voltar ao nível
        </button>
      </div>
    </main>
  )
}

export default Result
