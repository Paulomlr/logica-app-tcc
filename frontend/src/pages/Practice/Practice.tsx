import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './Practice.css'

type CellValue = '' | 'v' | 'f'
type RowAnswer = { and: CellValue; impl: CellValue }

const ROWS = [true, false].flatMap((p) =>
  [true, false].flatMap((q) => [true, false].map((r) => ({ p, q, r }))),
)

function vf(value: boolean) {
  return value ? 'V' : 'F'
}

function formatTime(totalSeconds: number) {
  const m = Math.floor(totalSeconds / 60)
  const s = totalSeconds % 60
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

function nextValue(value: CellValue): CellValue {
  return value === 'v' ? 'f' : 'v'
}

function Practice() {
  const navigate = useNavigate()
  const [seconds, setSeconds] = useState(0)
  const [answers, setAnswers] = useState<RowAnswer[]>(() => ROWS.map(() => ({ and: '', impl: '' })))

  useEffect(() => {
    const id = setInterval(() => setSeconds((s) => s + 1), 1000)
    return () => clearInterval(id)
  }, [])

  function toggleCell(rowIndex: number, column: 'and' | 'impl') {
    setAnswers((current) =>
      current.map((row, i) => (i === rowIndex ? { ...row, [column]: nextValue(row[column]) } : row)),
    )
  }

  function clearAnswers() {
    setAnswers(ROWS.map(() => ({ and: '', impl: '' })))
  }

  return (
    <main className="screen practice">
      <div className="practice-top">
        <button type="button" className="practice-exit" aria-label="Sair" onClick={() => navigate('/niveis')}>
          ✕
        </button>
        <span className="formula">(p ∧ q) → r</span>
        <span className="timer">{formatTime(seconds)}</span>
      </div>

      <div className="practice-sub">
        <button type="button" className="clear-btn" onClick={clearAnswers}>
          Limpar respostas
        </button>
      </div>

      <div className="table-wrap">
        <table className="tt">
          <thead>
            <tr>
              <th>p</th>
              <th>q</th>
              <th className="fillable">p∧q</th>
              <th>r</th>
              <th className="fillable">→</th>
            </tr>
          </thead>
          <tbody>
            {ROWS.map((row, i) => (
              <tr key={i}>
                <td className="cell-given">{vf(row.p)}</td>
                <td className="cell-given">{vf(row.q)}</td>
                <td>
                  <button
                    type="button"
                    className={`cell-btn ${answers[i].and}`}
                    onClick={() => toggleCell(i, 'and')}
                  >
                    {answers[i].and ? vf(answers[i].and === 'v') : '–'}
                  </button>
                </td>
                <td className="cell-given">{vf(row.r)}</td>
                <td>
                  <button
                    type="button"
                    className={`cell-btn ${answers[i].impl}`}
                    onClick={() => toggleCell(i, 'impl')}
                  >
                    {answers[i].impl ? vf(answers[i].impl === 'v') : '–'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="practice-bottom">
        <button
          type="button"
          className="btn btn-primary verify-btn"
          onClick={() => navigate('/resultado', { state: { rows: ROWS, answers } })}
        >
          Verificar
        </button>
      </div>
    </main>
  )
}

export default Practice
