import type { CSSProperties } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  LogoMark,
  GearIcon,
  ChevronRightIcon,
  BarChartIcon,
  StarIcon,
} from '../../components/icons/Icons'
import './Home.css'

const NAV_ITEMS = [
  { Icon: BarChartIcon, label: 'Progresso', stat: '68%', path: '/progresso' },
  { Icon: StarIcon, label: 'Conquistas', stat: '3/10', path: null },
]

function Home() {
  const navigate = useNavigate()

  return (
    <main className="screen home">
      <div className="home-topbar">
        <LogoMark className="brand-mark-sm" />
        <button
          type="button"
          className="gear-btn"
          aria-label="Configurações"
          onClick={() => navigate('/configuracoes')}
        >
          <GearIcon />
        </button>
      </div>

      <div className="greet">Oi, Paulo</div>

      <div className="summary-card">
        <div className="ring-wrap">
          <div className="ring" style={{ '--pct': '.68turn' } as CSSProperties} />
          <span className="ring-label">68%</span>
        </div>
        <div className="summary-stats">
          <b>34 exercícios</b>
          <span>Feitos ao todo</span>
          <b style={{ marginTop: 6 }}>23 corretos</b>
          <span>68% de acerto</span>
        </div>
      </div>

      <button type="button" className="cta-practice" onClick={() => navigate('/niveis')}>
        <div className="cta-text">
          <span className="cta-eyebrow">Nível médio · continue de onde parou</span>
          <span className="cta-title">Praticar agora</span>
        </div>
        <ChevronRightIcon className="cta-arrow" />
      </button>

      <div className="nav-list">
        {NAV_ITEMS.map(({ Icon, label, stat, path }) => (
          <button
            type="button"
            key={label}
            className="nav-row"
            onClick={() => path && navigate(path)}
            disabled={!path}
          >
            <Icon />
            <span className="label">{label}</span>
            <span className="stat">{stat}</span>
            <ChevronRightIcon className="chev" />
          </button>
        ))}
      </div>
    </main>
  )
}

export default Home
