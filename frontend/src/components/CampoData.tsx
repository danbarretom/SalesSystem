import { useEffect, useState } from 'react'
import { aplicarMascaraData, dataCompleta, paraBr, paraIso } from '../utils/data'

interface CampoDataProps {
  id: string
  label: string
  valorIso: string
  onChange: (valorIso: string) => void
  required?: boolean
}

export function CampoData({ id, label, valorIso, onChange, required }: CampoDataProps) {
  const [texto, setTexto] = useState(valorIso ? paraBr(valorIso) : '')

  useEffect(() => {
    setTexto(valorIso ? paraBr(valorIso) : '')
  }, [valorIso])

  function handleChange(valorDigitado: string) {
    const mascarado = aplicarMascaraData(valorDigitado)
    setTexto(mascarado)
    onChange(dataCompleta(mascarado) ? paraIso(mascarado) : '')
  }

  return (
    <div>
      <label htmlFor={id} className="block text-sm font-medium text-gray-700">
        {label}
      </label>
      <input
        id={id}
        type="text"
        inputMode="numeric"
        placeholder="dd/mm/aaaa"
        pattern="\d{2}/\d{2}/\d{4}"
        required={required}
        value={texto}
        onChange={(e) => handleChange(e.target.value)}
        className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
      />
    </div>
  )
}
