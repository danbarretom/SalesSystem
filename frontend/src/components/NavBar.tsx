import { NavLink } from 'react-router-dom'

const linkClass = ({ isActive }: { isActive: boolean }) =>
  `px-3 py-2 text-sm font-medium ${isActive ? 'text-blue-600' : 'text-gray-600 hover:text-gray-900'}`

export function NavBar() {
  return (
    <nav className="flex items-center gap-2 border-b border-gray-200 px-6 py-3">
      <span className="mr-4 text-sm font-semibold text-gray-800">Sistema de Vendas</span>
      <NavLink to="/produtos" className={linkClass}>
        Produtos
      </NavLink>
      <NavLink to="/clientes" className={linkClass}>
        Clientes
      </NavLink>
      <NavLink to="/vendas" className={linkClass}>
        Vendas
      </NavLink>
    </nav>
  )
}
