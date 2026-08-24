import LogoMark from '../../components/LogoMark/LogoMark'
import './Home.css'

const NAV_ITEMS = [
  { icon: '📊', label: 'Progresso' },
  { icon: '🔥', label: 'Sequência' },
  { icon: '⭐', label: 'Conquistas' },
]

function Home() {
  return (
    <main className="home">
      <div className="home-topbar">
        <LogoMark size="sm" />
        <button type="button" className="icon-button" aria-label="Configurações">
          ⚙
        </button>
      </div>

      <div className="home-greeting">
        <h1>Oi, Paulo</h1>
        <p>Sexta-feira · sequência de 5 dias</p>
      </div>

      <div className="home-summary-card">
        <div className="stat">
          <span className="stat-value">68%</span>
          <span className="stat-label">Precisão</span>
        </div>
        <div className="stat">
          <span className="stat-value">34</span>
          <span className="stat-label">Exercícios</span>
        </div>
        <div className="stat">
          <span className="stat-value">5</span>
          <span className="stat-label">Sequência</span>
        </div>
      </div>

      <p className="mono-label">Nível médio · continue de onde parou</p>

      <button type="button" className="home-cta">
        <span>Praticar agora</span>
        <span aria-hidden="true">›</span>
      </button>

      <nav className="home-nav">
        {NAV_ITEMS.map((item) => (
          <button type="button" key={item.label} className="home-nav-row">
            <span className="home-nav-icon" aria-hidden="true">
              {item.icon}
            </span>
            <span className="home-nav-label">{item.label}</span>
            <span className="home-nav-chevron" aria-hidden="true">
              ›
            </span>
          </button>
        ))}
      </nav>
    </main>
  )
}

export default Home
