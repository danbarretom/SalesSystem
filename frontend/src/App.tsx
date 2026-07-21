import { Navigate, Route, BrowserRouter, Routes } from 'react-router-dom'
import { NavBar } from './components/NavBar'
import { DashboardPage } from './pages/DashboardPage'
import { ProdutosPage } from './pages/ProdutosPage'
import { ProdutoFormPage } from './pages/ProdutoFormPage'
import { ProdutoEstoqueBaixoPage } from './pages/ProdutoEstoqueBaixoPage'
import { ClientesPage } from './pages/ClientesPage'
import { ClienteFormPage } from './pages/ClienteFormPage'
import { VendasPage } from './pages/VendasPage'
import { VendaFormPage } from './pages/VendaFormPage'

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/produtos" element={<ProdutosPage />} />
        <Route path="/produtos/novo" element={<ProdutoFormPage />} />
        <Route path="/produtos/:id/editar" element={<ProdutoFormPage />} />
        <Route path="/produtos/estoque-baixo" element={<ProdutoEstoqueBaixoPage />} />
        <Route path="/clientes" element={<ClientesPage />} />
        <Route path="/clientes/novo" element={<ClienteFormPage />} />
        <Route path="/clientes/:id/editar" element={<ClienteFormPage />} />
        <Route path="/vendas" element={<VendasPage />} />
        <Route path="/vendas/nova" element={<VendaFormPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
