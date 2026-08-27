import { Routes, Route } from 'react-router-dom'
import Splash from './pages/Splash/Splash'
import Home from './pages/Home/Home'
import LevelSelect from './pages/LevelSelect/LevelSelect'
import Practice from './pages/Practice/Practice'
import Result from './pages/Result/Result'
import Progress from './pages/Progress/Progress'
import Settings from './pages/Settings/Settings'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Splash />} />
      <Route path="/home" element={<Home />} />
      <Route path="/niveis" element={<LevelSelect />} />
      <Route path="/pratica/:exerciseId" element={<Practice />} />
      <Route path="/resultado" element={<Result />} />
      <Route path="/progresso" element={<Progress />} />
      <Route path="/configuracoes" element={<Settings />} />
    </Routes>
  )
}

export default App
