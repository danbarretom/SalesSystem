import { Navigate, Route, BrowserRouter, Routes } from 'react-router-dom'
import { NavBar } from './components/NavBar'
import { ProdutosPage } from './pages/ProdutosPage'
import { ProdutoFormPage } from './pages/ProdutoFormPage'
import { ClientesPage } from './pages/ClientesPage'
import { ClienteFormPage } from './pages/ClienteFormPage'
import { VendasPage } from './pages/VendasPage'
import { VendaFormPage } from './pages/VendaFormPage'

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/produtos" element={<ProdutosPage />} />
        <Route path="/produtos/novo" element={<ProdutoFormPage />} />
        <Route path="/clientes" element={<ClientesPage />} />
        <Route path="/clientes/novo" element={<ClienteFormPage />} />
        <Route path="/vendas" element={<VendasPage />} />
        <Route path="/vendas/nova" element={<VendaFormPage />} />
        <Route path="*" element={<Navigate to="/produtos" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
