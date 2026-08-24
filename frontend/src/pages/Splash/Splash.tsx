import { useNavigate } from 'react-router-dom'
import { LogoMark, GoogleIcon } from '../../components/icons/Icons'
import './Splash.css'

function Splash() {
  const navigate = useNavigate()

  return (
    <main className="screen splash">
      <div className="pwa-pill">↓ Instalar como app</div>
      <LogoMark className="splash-mark" />
      <div className="wordmark">
        tábula<span>.</span>
      </div>
      <p className="tagline">Tabela verdade, sem enrolação</p>
      <div className="actions">
        <button type="button" className="btn btn-primary" onClick={() => navigate('/home')}>
          <GoogleIcon className="google-icn" />
          Entrar com Google
        </button>
      </div>
    </main>
  )
}

export default Splash
