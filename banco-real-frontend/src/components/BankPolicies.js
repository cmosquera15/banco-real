import React from 'react';
import './css/Overlay.css';

export const BankPolicies = ({ onClose }) => {
  return (
    <div className="overlay">
      <div className="modal">
        <button className="modal-close" onClick={onClose}>Cerrar</button>
        <h1>Políticas del Banco</h1>
        <section>
          <h2>Operaciones Fundamentales de las Cuentas</h2>
          <ul>
            <li><strong>Asignar una cuenta a un usuario:</strong> Cada usuario puede tener una o varias cuentas asignadas a su nombre.</li>
            <li><strong>Conocer el estado de la cuenta o cuentas:</strong> Los estados posibles de una cuenta son: Abierta, Cancelada, Embargada. El usuario puede consultar el estado de sus cuentas en cualquier momento.</li>
            <li><strong>Conocer el saldo de la cuenta o cuentas:</strong> El usuario puede consultar el saldo actual de todas sus cuentas en cualquier momento.</li>
            <li><strong>Conocer las transacciones sobre la cuenta en un periodo determinado:</strong> El usuario puede revisar el historial de transacciones de sus cuentas para un periodo específico.</li>
          </ul>
        </section>
        <section>
          <h2>Mantenimiento de cuentas</h2>
          <p>Se cobrará el mantenimiento de cuentas al principio de cada mes.</p>
          <ul>
            <li><strong>Cuenta corriente:</strong> 1.5%</li>
            <li><strong>Cuenta de ahorros:</strong> 2.5%</li>
            <li><strong>Cuenta suprema:</strong> 3%</li>
          </ul>
        </section>
        <section>
          <h2>Comisiones por transacción.</h2>
          <p>Se cobrará una comisión al momento de realizar la transacción.</p>
          <ul>
            <li><strong>Cuenta corriente:</strong> 0.03%</li>
            <li><strong>Cuenta de ahorros:</strong> 0.02%</li>
            <li><strong>Cuenta suprema:</strong> 0.01%</li>
          </ul>
          <p><strong>Nota:</strong> Si el monto de la transacción es menor a 50.000 pesos, se cobrarán 100 pesos.</p>
        </section>
      </div>
    </div>
  );
};