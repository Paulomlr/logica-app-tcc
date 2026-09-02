import { useEffect, useState, type UIEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getPlayView, submitAttempt, type ExercisePlayView } from '../../lib/api'
import { getDisplayColumns, pinnedColumnProps } from '../../lib/tableColumns'
import './Practice.css'

type CellValue = '' | 'v' | 'f'
type Modal = null | 'exit' | 'clear' | 'incomplete'

function formatTime(totalSeconds: number) {
  const m = Math.floor(totalSeconds / 60)
  const s = totalSeconds % 60
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

function nextValue(value: CellValue): CellValue {
  return value === 'v' ? 'f' : 'v'
}

function formulaFontSize(formula: string) {
  if (formula.length > 40) return 11.5
  if (formula.length > 24) return 13
  return 15
}

function Practice() {
  const { exerciseId } = useParams()
  const navigate = useNavigate()

  const [play, setPlay] = useState<ExercisePlayView | null>(null)
  const [loadError, setLoadError] = useState<string | null>(null)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [seconds, setSeconds] = useState(0)
  const [answers, setAnswers] = useState<CellValue[][]>([])
  const [openModal, setOpenModal] = useState<Modal>(null)
  const [hasScrolled, setHasScrolled] = useState(false)
  const [scrolledToEnd, setScrolledToEnd] = useState(false)

  function handleTableScroll(e: UIEvent<HTMLDivElement>) {
    setHasScrolled(true)
    const el = e.currentTarget
    setScrolledToEnd(el.scrollLeft + el.clientWidth >= el.scrollWidth - 2)
  }

  useEffect(() => {
    let cancelled = false
    getPlayView(Number(exerciseId))
      .then((view) => {
        if (cancelled) return
        setPlay(view)
        const fillableCount = view.columnIsFillable.filter(Boolean).length
        setAnswers(view.rowAssignments.map(() => Array(fillableCount).fill('')))
      })
      .catch((err: Error) => !cancelled && setLoadError(err.message))
    return () => {
      cancelled = true
    }
  }, [exerciseId])

  useEffect(() => {
    const id = setInterval(() => setSeconds((s) => s + 1), 1000)
    return () => clearInterval(id)
  }, [])

  function toggleCell(rowIndex: number, slot: number) {
    setAnswers((current) =>
      current.map((row, r) =>
        r === rowIndex ? row.map((v, s) => (s === slot ? nextValue(v) : v)) : row,
      ),
    )
  }

  function clearAnswers() {
    setAnswers((current) => current.map((row) => row.map(() => '')))
    setOpenModal(null)
  }

  async function goToResult() {
    if (!play) return
    setOpenModal(null)
    setSubmitError(null)
    setSubmitting(true)
    try {
      const payload = answers.map((row) => row.map((v) => v === 'v'))
      const result = await submitAttempt(play.exerciseId, payload, seconds)
      navigate('/resultado', { state: { play, result, answers, seconds } })
    } catch (err) {
      setSubmitError(err instanceof Error ? err.message : 'Não foi possível enviar sua resposta.')
    } finally {
      setSubmitting(false)
    }
  }

  const incompleteCount = answers.flat().filter((v) => v === '').length

  function handleVerify() {
    if (incompleteCount > 0) {
      setOpenModal('incomplete')
    } else {
      goToResult()
    }
  }

  if (loadError) {
    return (
      <main className="screen practice practice-message">
        <p>Não foi possível carregar este exercício.</p>
        <p className="practice-message-detail">{loadError}</p>
        <button type="button" className="btn btn-ghost" onClick={() => navigate('/niveis')}>
          Voltar aos níveis
        </button>
      </main>
    )
  }

  if (!play) {
    return (
      <main className="screen practice practice-message">
        <p>Carregando exercício…</p>
      </main>
    )
  }

  const fillableSlotForColumn: number[] = []
  let slotCounter = 0
  for (const isFillable of play.columnIsFillable) {
    fillableSlotForColumn.push(isFillable ? slotCounter++ : -1)
  }

  const { order: displayColumns, givenCount } = getDisplayColumns(play)

  return (
    <main className="screen practice">
      <div className="practice-top">
        <div className="practice-top-row">
          <button type="button" className="practice-exit" aria-label="Sair" onClick={() => setOpenModal('exit')}>
            ✕
          </button>
          <span className="timer">{formatTime(seconds)}</span>
        </div>
        <div className="formula-wrap">
          <span className="formula" style={{ fontSize: formulaFontSize(play.formula) }}>
            {play.formula}
          </span>
        </div>
      </div>

      <div className="practice-sub">
        <button type="button" className="clear-btn" onClick={() => setOpenModal('clear')}>
          Limpar respostas
        </button>
      </div>

      {submitError && <p className="practice-error">{submitError}</p>}

      <div
        className={`table-wrap${play.columnLabels.length > 5 ? ' scrollable' : ''}${scrolledToEnd ? ' at-end' : ''}`}
        onScroll={handleTableScroll}
      >
        <table className="tt">
          <thead>
            <tr>
              {displayColumns.map((c, pos) => {
                const label = play.columnLabels[c]
                const pin = pinnedColumnProps(pos, givenCount, play.columnIsFillable[c] ? 'fillable' : undefined)
                return (
                  <th key={label} className={pin.className} style={pin.style}>
                    {label}
                  </th>
                )
              })}
            </tr>
          </thead>
          <tbody>
            {play.rowAssignments.map((assignment, r) => (
              <tr key={r}>
                {displayColumns.map((c, pos) => {
                  const label = play.columnLabels[c]
                  const pin = pinnedColumnProps(pos, givenCount)
                  if (!play.columnIsFillable[c]) {
                    return (
                      <td key={label} className={['cell-given', pin.className].filter(Boolean).join(' ')} style={pin.style}>
                        {assignment[label] ? 'V' : 'F'}
                      </td>
                    )
                  }
                  const slot = fillableSlotForColumn[c]
                  const value = answers[r]?.[slot] ?? ''
                  return (
                    <td key={label} className={pin.className} style={pin.style}>
                      <button
                        type="button"
                        className={`cell-btn ${value}`}
                        onClick={() => toggleCell(r, slot)}
                      >
                        {value ? (value === 'v' ? 'V' : 'F') : '–'}
                      </button>
                    </td>
                  )
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {play.columnLabels.length > 5 && (
        <p className={`hint${hasScrolled ? ' hint-hidden' : ''}`}>◂ arraste ou gire o celular ▸</p>
      )}

      <div className="practice-bottom">
        <button
          type="button"
          className="btn btn-primary verify-btn"
          onClick={handleVerify}
          disabled={submitting}
        >
          {submitting ? 'Verificando…' : 'Verificar'}
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
