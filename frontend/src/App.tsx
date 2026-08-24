import { Routes, Route } from 'react-router-dom'
import Splash from './pages/Splash/Splash'
import Home from './pages/Home/Home'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Splash />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  )
}

export default App
