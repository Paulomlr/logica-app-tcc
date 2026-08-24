import type { CSSProperties } from 'react'
import { useNavigate } from 'react-router-dom'
import './Progress.css'

const DIFFICULTIES = [
  { label: 'Fácil', pct: 86, color: 'var(--true)' },
  { label: 'Médio', pct: 64, color: undefined },
  { label: 'Difícil', pct: 41, color: 'var(--false)' },
]

function Progress() {
  const navigate = useNavigate()

  return (
    <main className="screen progress">
      <div className="list-top">
        <button type="button" className="back" onClick={() => navigate('/home')}>
          ‹ Início
        </button>
        <div className="titles">
          <div className="screen-title">Seu progresso</div>
        </div>
      </div>

      <div className="ring-wrap">
        <div className="ring" style={{ '--pct': '.68turn' } as CSSProperties} />
        <span className="ring-label">68%</span>
      </div>

      <div className="progress-total">
        <div>
          <b>34</b>
          <span>Exercícios</span>
        </div>
        <div>
          <b>1h 12min</b>
          <span>Tempo total</span>
        </div>
      </div>

      <div className="diff-block">
        {DIFFICULTIES.map((d) => (
          <div className="diff-row" key={d.label}>
            <div className="lbl">
              <span>{d.label}</span>
              <span className="mono">{d.pct}%</span>
            </div>
            <div className="diff-track">
              <div className="diff-fill" style={{ width: `${d.pct}%`, background: d.color }} />
            </div>
          </div>
        ))}
      </div>

      <div className="link-row">
        <a href="#">Ver onde mais erro →</a>
        <a href="#">Ver histórico completo →</a>
      </div>
    </main>
  )
}

export default Progress
