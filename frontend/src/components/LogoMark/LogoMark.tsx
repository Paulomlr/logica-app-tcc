import './LogoMark.css'

type LogoMarkProps = {
  size?: 'sm' | 'lg'
}

function LogoMark({ size = 'lg' }: LogoMarkProps) {
  return (
    <div className={`logo-mark logo-mark--${size}`} aria-hidden="true">
      <div className="logo-mark-row">
        <span className="logo-mark-sq logo-mark-sq--big" />
        <span className="logo-mark-sq logo-mark-sq--small" />
      </div>
      <div className="logo-mark-row">
        <span className="logo-mark-sq logo-mark-sq--small" />
        <span className="logo-mark-sq logo-mark-sq--big" />
      </div>
    </div>
  )
}

export default LogoMark
