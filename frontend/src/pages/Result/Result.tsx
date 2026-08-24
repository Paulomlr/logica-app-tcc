import { useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { StarFilledIcon } from '../../components/icons/Icons'
import './Result.css'

type CellValue = '' | 'v' | 'f'
type Row = { p: boolean; q: boolean; r: boolean }
type RowAnswer = { and: CellValue; impl: CellValue }
type ResultState = { rows: Row[]; answers: RowAnswer[] }

function vf(value: boolean) {
  return value ? 'V' : 'F'
}

function cellIsCorrect(answer: CellValue, correct: boolean) {
  if (answer === '') return false
  return (answer === 'v') === correct
}

function Result() {
  const navigate = useNavigate()
  const { state } = useLocation()
  const { rows, answers } = (state as ResultState | null) ?? { rows: [], answers: [] }

  useEffect(() => {
    if (rows.length === 0) {
      navigate('/pratica', { replace: true })
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  if (rows.length === 0) {
    return null
  }

  const totalCells = rows.length * 2
  let correctCells = 0

  const graded = rows.map((row, i) => {
    const correctAnd = row.p && row.q
    const correctImpl = !correctAnd || row.r
    const andOk = cellIsCorrect(answers[i].and, correctAnd)
    const implOk = cellIsCorrect(answers[i].impl, correctImpl)
    if (andOk) correctCells += 1
    if (implOk) correctCells += 1
    return {
      ...row,
      and: answers[i].and,
      andOk,
      impl: answers[i].impl,
      implOk,
    }
  })

  const allCorrect = correctCells === totalCells

  return (
    <main className="screen result">
      <div className="result-banner">
        <b>
          {correctCells} de {totalCells} corretas
        </b>
        <span>Revisão célula a célula abaixo</span>
      </div>

      <table className="tt">
        <thead>
          <tr>
            <th>p</th>
            <th>q</th>
            <th>p∧q</th>
            <th>r</th>
            <th>→</th>
          </tr>
        </thead>
        <tbody>
          {graded.map((row, i) => (
            <tr key={i}>
              <td className="cell-given">{vf(row.p)}</td>
              <td className="cell-given">{vf(row.q)}</td>
              <td className={row.andOk ? 'tag-ok' : 'tag-bad'}>
                {row.and ? vf(row.and === 'v') : '–'}
                {!row.andOk && '*'}
              </td>
              <td className="cell-given">{vf(row.r)}</td>
              <td className={row.implOk ? 'tag-ok' : 'tag-bad'}>
                {row.impl ? vf(row.impl === 'v') : '–'}
                {!row.implOk && '*'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {allCorrect && (
        <div className="achieve-card">
          <div className="achieve-badge">
            <StarFilledIcon />
          </div>
          <div>
            <b>Conquista: Primeira vitória</b>
            <span>1º exercício correto</span>
          </div>
        </div>
      )}

      <div className="score-line">
        +{correctCells} pontos · dificuldade média
      </div>

      <div className="actions">
        <button type="button" className="btn btn-primary" onClick={() => navigate('/pratica')}>
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
