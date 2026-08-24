import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { LogoMark } from '../../components/icons/Icons'
import './Settings.css'

const FONT_STEPS = [87.5, 100, 112.5, 125]
const FONT_STEP_KEY = 'tabula:font-step'

function Toggle({ on, onToggle }: { on: boolean; onToggle: () => void }) {
  return (
    <div
      className={`toggle${on ? ' on' : ''}`}
      role="switch"
      aria-checked={on}
      tabIndex={0}
      onClick={onToggle}
      onKeyDown={(e) => (e.key === 'Enter' || e.key === ' ') && onToggle()}
    />
  )
}

function Settings() {
  const navigate = useNavigate()
  const [darkMode, setDarkMode] = useState(true)
  const [highContrast, setHighContrast] = useState(false)
  const [fontStepIndex, setFontStepIndex] = useState(() => {
    const saved = localStorage.getItem(FONT_STEP_KEY)
    return saved ? Number(saved) : 1
  })

  useEffect(() => {
    document.documentElement.style.fontSize = `${FONT_STEPS[fontStepIndex]}%`
    localStorage.setItem(FONT_STEP_KEY, String(fontStepIndex))
  }, [fontStepIndex])

  return (
    <main className="screen settings">
      <div className="list-top">
        <button type="button" className="back" onClick={() => navigate('/home')}>
          ‹ Início
        </button>
        <div className="titles">
          <div className="screen-title">Configurações</div>
        </div>
      </div>

      <div className="setting-row">
        <div>
          <b>Modo escuro</b>
          <div className="desc">Padrão do app</div>
        </div>
        <Toggle on={darkMode} onToggle={() => setDarkMode((v) => !v)} />
      </div>

      <div className="setting-row">
        <div>
          <b>Tamanho da fonte</b>
          <div className="desc">Ajusta texto da interface</div>
        </div>
        <div className="fontstep">
          <button
            type="button"
            disabled={fontStepIndex === 0}
            onClick={() => setFontStepIndex((i) => Math.max(0, i - 1))}
            aria-label="Diminuir fonte"
          >
            –
          </button>
          <span>A</span>
          <button
            type="button"
            disabled={fontStepIndex === FONT_STEPS.length - 1}
            onClick={() => setFontStepIndex((i) => Math.min(FONT_STEPS.length - 1, i + 1))}
            aria-label="Aumentar fonte"
          >
            +
          </button>
        </div>
      </div>

      <div className="setting-row">
        <div>
          <b>Alto contraste</b>
          <div className="desc">Mais contraste entre cores</div>
        </div>
        <Toggle on={highContrast} onToggle={() => setHighContrast((v) => !v)} />
      </div>

      <div className="settings-footer">
        <LogoMark />
        <span>tábula · v0.1 (conceito)</span>
      </div>
    </main>
  )
}

export default Settings
