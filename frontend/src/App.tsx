import { Navigate, Route, BrowserRouter, Routes } from 'react-router-dom'
import { NavBar } from './components/NavBar'
import { ProdutosPage } from './pages/ProdutosPage'
import { ClientesPage } from './pages/ClientesPage'

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/produtos" element={<ProdutosPage />} />
        <Route path="/clientes" element={<ClientesPage />} />
        <Route path="*" element={<Navigate to="/produtos" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
