import { Routes, Route } from 'react-router-dom'
import Splash from './pages/Splash/Splash'
import Home from './pages/Home/Home'
import LevelSelect from './pages/LevelSelect/LevelSelect'
import Practice from './pages/Practice/Practice'
import Result from './pages/Result/Result'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Splash />} />
      <Route path="/home" element={<Home />} />
      <Route path="/niveis" element={<LevelSelect />} />
      <Route path="/pratica" element={<Practice />} />
      <Route path="/resultado" element={<Result />} />
    </Routes>
  )
}

export default App
