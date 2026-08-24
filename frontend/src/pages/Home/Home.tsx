import type { CSSProperties } from 'react'
import {
  LogoMark,
  GearIcon,
  ChevronRightIcon,
  BarChartIcon,
  FlameIcon,
  StarIcon,
  TrophyIcon,
} from '../../components/icons/Icons'
import './Home.css'

const NAV_ITEMS = [
  { Icon: BarChartIcon, label: 'Progresso', stat: '68%' },
  { Icon: FlameIcon, label: 'Sequência', stat: '4 dias' },
  { Icon: StarIcon, label: 'Conquistas', stat: '3/10' },
  { Icon: TrophyIcon, label: 'Ranking', stat: '#3' },
]

function Home() {
  return (
    <main className="screen home">
      <div className="home-topbar">
        <LogoMark className="brand-mark-sm" />
        <button type="button" className="gear-btn" aria-label="Configurações">
          <GearIcon />
        </button>
      </div>

      <div>
        <div className="greet">Oi, Paulo</div>
        <div className="greet-sub">Sexta-feira · sequência de 4 dias</div>
      </div>

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

      <button type="button" className="cta-practice">
        <div className="cta-text">
          <span className="cta-eyebrow">Nível médio · continue de onde parou</span>
          <span className="cta-title">Praticar agora</span>
        </div>
        <ChevronRightIcon className="cta-arrow" />
      </button>

      <div className="nav-list">
        {NAV_ITEMS.map(({ Icon, label, stat }) => (
          <button type="button" key={label} className="nav-row">
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
