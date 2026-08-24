import { useNavigate } from 'react-router-dom'
import './LevelSelect.css'

const LEVELS = [
  { name: 'Fácil', meta: '2 variáveis · 1 operador', formula: 'p → q', dot: 'l1' },
  { name: 'Médio', meta: '3 variáveis · 2–3 operadores', formula: '(p ∧ q) → r', dot: 'l2' },
  { name: 'Difícil', meta: '4 variáveis · 4–6 operadores', formula: '(p∧q)→(r↔¬s)', dot: 'l3' },
  { name: 'Avançado', meta: '5 variáveis · 5–7 operadores', formula: '(p∧q)→(r↔(s∨t))', dot: 'l4' },
  { name: 'Mestre', meta: '6 variáveis · 6–8 operadores', formula: '((p→q)∧r)↔(¬s∨(t∧u))', dot: 'l5' },
]

function LevelSelect() {
  const navigate = useNavigate()

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

      {LEVELS.map((level) => (
        <button
          type="button"
          key={level.name}
          className="level-card"
          onClick={() => navigate('/pratica')}
        >
          <div>
            <div className="lv-name">{level.name}</div>
            <div className="lv-meta">{level.meta}</div>
            <div className="lv-formula">{level.formula}</div>
          </div>
          <div className={`lv-dot ${level.dot}`} />
        </button>
      ))}

      <div className="level-footer">
        <button type="button" className="btn btn-ghost">
          ↻ Gerar novo desafio
        </button>
        <p className="hint">Cria uma fórmula nova nesse nível, além do banco de exercícios prontos</p>
      </div>
    </main>
  )
}

export default LevelSelect
