import React, { useState } from 'react';
import './css/MoneyWithdrawal.css';
import { withdrawMoney } from './api';

export const MoneyWithdrawal = ({ client, onBack, account }) => {
  const initialFormData = {
    amount: '',
    securityKey: '',
    currency: 'COP'
  };

  const [formData, setFormData] = useState(initialFormData);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const withdrawData = {
        clientId: client.clientId,
        accountId: account.accountId,
        amount: formData.amount,
        currency: formData.currency,
        securityKey: formData.securityKey
      };
      await withdrawMoney(withdrawData);
      alert('Dinero retirado con éxito');
      setFormData(initialFormData);
    } catch (error) {
      console.error('Error al retirar dinero:', error);
      alert('Hubo un error al retirar el dinero.');
    }
  };

  return (
    <div className='MoneyWithdrawal'>
      <fieldset>
        <legend>Retirar Dinero</legend>
        <form onSubmit={handleSubmit}>
          <label className='MoneyWithdrawal-label'>Cantidad a retirar:</label>
          <input type='number' id='amount' name='amount' min='0.01' step='0.01' required value={formData.amount} onChange={handleChange}/>

          <label className='MoneyWithdrawal-label'>Seleccionar moneda:</label>
          <select id='currency' name='currency' value={formData.currency} onChange={handleChange}>
            <option value='COP'>COP</option>
            <option value='USD'>USD</option>
            <option value='EUR'>EUR</option>
          </select>

          <label className='MoneyWithdrawal-label'>Escriba su clave para confirmar</label>
          <input type='password' id='securityKey' name='securityKey' required value={formData.securityKey} onChange={handleChange}/>

          <button type='submit' className='submit-button'>Retirar</button>
        </form>
      </fieldset>
      <button className='back-button' onClick={onBack}>Volver</button>
    </div>
  );
};
