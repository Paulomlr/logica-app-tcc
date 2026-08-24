import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './Practice.css'

type CellValue = '' | 'v' | 'f'
type RowAnswer = { and: CellValue; impl: CellValue }
type Modal = null | 'exit' | 'clear' | 'incomplete'

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
  const [openModal, setOpenModal] = useState<Modal>(null)

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
    setOpenModal(null)
  }

  function goToResult() {
    setOpenModal(null)
    navigate('/resultado', { state: { rows: ROWS, answers } })
  }

  function handleVerify() {
    if (incompleteCount > 0) {
      setOpenModal('incomplete')
    } else {
      goToResult()
    }
  }

  const incompleteCount = answers.reduce(
    (count, a) => count + (a.and === '' ? 1 : 0) + (a.impl === '' ? 1 : 0),
    0,
  )

  return (
    <main className="screen practice">
      <div className="practice-top">
        <button type="button" className="practice-exit" aria-label="Sair" onClick={() => setOpenModal('exit')}>
          ✕
        </button>
        <span className="formula">(p ∧ q) → r</span>
        <span className="timer">{formatTime(seconds)}</span>
      </div>

      <div className="practice-sub">
        <button type="button" className="clear-btn" onClick={() => setOpenModal('clear')}>
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
        <button type="button" className="btn btn-primary verify-btn" onClick={handleVerify}>
          Verificar
        </button>
      </div>

      <div className={`modal-backdrop${openModal === 'exit' ? ' show' : ''}`}>
        <div className="modal">
          <h3>Sair sem terminar?</h3>
          <p>Suas respostas nessa tentativa não serão salvas.</p>
          <div className="modal-actions">
            <button type="button" className="btn btn-ghost" onClick={() => setOpenModal(null)}>
              Continuar praticando
            </button>
            <button type="button" className="btn btn-danger" onClick={() => navigate('/niveis')}>
              Sair mesmo assim
            </button>
          </div>
        </div>
      </div>

      <div className={`modal-backdrop${openModal === 'incomplete' ? ' show' : ''}`}>
        <div className="modal">
          <h3>Ainda faltam respostas</h3>
          <p>
            <span>{incompleteCount}</span> células em branco. Verificar mesmo assim?
          </p>
          <div className="modal-actions">
            <button type="button" className="btn btn-ghost" onClick={() => setOpenModal(null)}>
              Voltar e completar
            </button>
            <button type="button" className="btn btn-primary" onClick={goToResult}>
              Verificar assim mesmo
            </button>
          </div>
        </div>
      </div>

      <div className={`modal-backdrop${openModal === 'clear' ? ' show' : ''}`}>
        <div className="modal">
          <h3>Limpar todas as respostas?</h3>
          <p>Isso apaga tudo que você já preencheu nessa tabela. Não dá pra desfazer.</p>
          <div className="modal-actions">
            <button type="button" className="btn btn-ghost" onClick={() => setOpenModal(null)}>
              Cancelar
            </button>
            <button type="button" className="btn btn-danger" onClick={clearAnswers}>
              Limpar tudo
            </button>
          </div>
        </div>
      </div>
    </main>
  )
}

export default Practice
