import { Routes, Route } from 'react-router-dom'
import Splash from './pages/Splash/Splash'
import Home from './pages/Home/Home'
import LevelSelect from './pages/LevelSelect/LevelSelect'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Splash />} />
      <Route path="/home" element={<Home />} />
      <Route path="/niveis" element={<LevelSelect />} />
    </Routes>
  )
}

export default App
