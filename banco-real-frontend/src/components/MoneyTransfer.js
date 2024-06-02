import React, { useState } from 'react';
import './css/MoneyTransfer.css';
import { transferMoney } from './api';

export const MoneyTransfer = ({ client, account, onBack }) => {
  const [step, setStep] = useState(1);
  const initialFormData = {
    accountNumber: '',
    accountType: '',
    amount: '',
    securityKey: '',
    currency: 'COP',
  };
  const [formData, setFormData] = useState(initialFormData);

  const handleNextStep = (e) => {
    e.preventDefault();
    const accountNumberPattern = /^[0-9]{10}$/;
    if (accountNumberPattern.test(formData.accountNumber) && formData.accountType) {
      setStep(2);
    } else {
      alert('Por favor, complete todos los campos correctamente.');
    }
  };

  const handlePrevStep = (e) => {
    e.preventDefault();
    setStep(1);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    console.log(`Updating ${name} with value:`, value);
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const transferData = {
        senderClientId: client.clientId,
        senderAccountId: account.accountId,
        recipientAccountId: formData.accountNumber,
        amount: formData.amount,
        currency: formData.currency,
        securityKey: formData.securityKey
      };
      await transferMoney(transferData);
      alert('Dinero enviado con éxito');
      setFormData(initialFormData);
    } catch (error) {
      console.error('Error al enviar dinero:', error);
      alert('Hubo un error al enviar el dinero.');
    }
  };

  return (
    <div className='MoneyTransfer'>
      <fieldset>
        <legend>Enviar dinero</legend>
        <form onSubmit={handleSubmit}>
          {step === 1 ? (
            <>
              <label htmlFor='accountNumber' className='MoneyTransfer-label'>Número de cuenta:</label>
              <input type='text' id='accountNumber' name='accountNumber' pattern='[0-9]{10}' title='El número de cuenta debe tener 10 dígitos' required value={formData.accountNumber} onChange={handleChange}/>

              <label className='MoneyTransfer-label'>Tipo de cuenta:</label>
              <div className='radio-group'>
                <div className='radio-option'>
                  <input type='radio' id='current' name='accountType' value='current' checked={formData.accountType === 'current'} onChange={handleChange}/>
                  <label htmlFor='current'>Corriente</label>
                </div>
                <div className='radio-option'>
                  <input type='radio' id='savings' name='accountType' value='savings' checked={formData.accountType === 'savings'} onChange={handleChange}/>
                  <label htmlFor='savings'>Ahorros</label>
                </div>
                <div className='radio-option'>
                  <input type='radio' id='supreme' name='accountType' value='supreme' checked={formData.accountType === 'supreme'} onChange={handleChange}/>
                  <label htmlFor='supreme'>Suprema</label>
                </div>
              </div>

              <button className='next-button' onClick={handleNextStep}>Continuar</button>
            </>
          ) : (
            <>
              <label className='MoneyTransfer-label'>Cantidad a enviar:</label>
              <input type='number' id='amount' name='amount' min='0.01' step='0.01' required value={formData.amount} onChange={handleChange}/>

              <label className='MoneyTransfer-label'>Seleccionar moneda:</label>
              <select id='currency' name='currency' value={formData.currency} onChange={handleChange}>
                <option value='COP'>COP</option>
                <option value='USD'>USD</option>
                <option value='EUR'>EUR</option>
              </select>

              <label className='MoneyTransfer-label'>Escriba su clave para confirmar</label>
              <input type='password' id='security-key' name='securityKey' required value={formData.securityKey} onChange={handleChange}/>

              <button className='prev-button' onClick={handlePrevStep}>Regresar</button>
              <button type='submit' className='submit-button'>Enviar</button>
            </>
          )}
        </form>
      </fieldset>
      <button className='back-button' onClick={onBack}>Volver</button>
    </div>
  );
};
