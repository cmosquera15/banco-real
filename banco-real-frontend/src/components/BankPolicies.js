import React from 'react';
import './css/BankPolicies.css';

export const BankPolicies = () => {
  return (
    <div className="BankPolicies">
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
        <h2>Comisiones y Tasas</h2>
        <ul>
          <li><strong>Comisiones:</strong> Es un método abstracto que será redefinido en las subclases. Las comisiones se ejecutarán los días 1 de cada mes para cobrar el impuesto de mantenimiento de la cuenta. Las tarifas específicas se determinarán de acuerdo a las políticas del banco.</li>
          <li><strong>Ingreso o Consignación:</strong> Método que recibe un parámetro cantidad de tipo double. Añade la cantidad especificada al saldo actual de la cuenta. El ingreso puede ser en dólares, euros o pesos, y se debe registrar la fecha del ingreso.</li>
          <li><strong>Conversión de Moneda:</strong> Las transacciones pueden realizarse en dólares, euros o pesos. Toda la información se visualizará y los reportes se entregarán en pesos.</li>
          <li><strong>Retiro:</strong> Método que recibe un parámetro cantidad de tipo double. Resta la cantidad especificada del saldo actual de la cuenta. Se debe registrar la fecha del retiro. Si el monto es mayor a 20.000.000 de pesos, se generarán alarmas en pantalla y se requerirá una clave adicional o validación con preguntas.</li>
          <li><strong>Asignar Tipo de Interés:</strong> Método que permite asignar el tipo de interés. Las tasas de interés dependen de las políticas del banco. El interés se paga diario y es compuesto.</li>
        </ul>
      </section>
      <section>
        <h2>Políticas Generales</h2>
        <ul>
          <li><strong>Disponibilidad de Políticas y Código de Ética:</strong> Las políticas del banco y el código de ética estarán disponibles y visibles para cualquier interesado.</li>
          <li><strong>Cuota de Membresía (Mantenimiento):</strong> Cada mes se cobrará la cuota de membresía (mantenimiento) según las políticas del banco.</li>
          <li><strong>Tarifas por Transacción:</strong> Cada transacción de retiro o depósito tiene un costo del 1% del monto de la transacción. Si el valor de esta comisión es menor a 50.000 pesos, se cobrará una tarifa fija de 100.000 pesos.</li>
        </ul>
      </section>
    </div>
  );
};
