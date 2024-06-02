import React, { useState } from 'react';
import './css/MoneyWithdrawal.css';
import { withdrawMoney } from './api';
import { Modal } from './Modal';

export const MoneyWithdrawal = ({ client, onBack, account }) => {
  const initialFormData = {
    amount: '',
    securityKey: '',
    currency: 'COP',
    additionalSecurityKey: ''
  };

  const [formData, setFormData] = useState(initialFormData);
  const [isAdditionalSecurityKeyRequired, setIsAdditionalSecurityKeyRequired] = useState(false);
  const [generatedSecurityKey, setGeneratedSecurityKey] = useState('');
  const [isModalVisible, setIsModalVisible] = useState(false);

  const generateRandomKey = () => {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let randomKey = '';
    const length = Math.floor(Math.random() * 11) + 20; // Entre 20 y 30 caracteres
    for (let i = 0; i < length; i++) {
      randomKey += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return randomKey;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleAmountChange = (e) => {
    const { name, value } = e.target;
    const formattedValue = value.replace(/\D/g, '');
    setFormData((prevData) => ({
      ...prevData,
      [name]: formattedValue,
    }));

    if (name === 'amount' && parseFloat(value) > 20000000) {
      setIsAdditionalSecurityKeyRequired(true);
      const newKey = generateRandomKey();
      setGeneratedSecurityKey(newKey);
      setIsModalVisible(true);
    } else if (name === 'amount' && parseFloat(value) <= 20000000) {
      setIsAdditionalSecurityKeyRequired(false);
      setGeneratedSecurityKey('');
      setIsModalVisible(false);
    }
  };

  const handleAmountBlur = (e) => {
    const { name, value } = e.target;
    const formattedValue = parseFloat(value.replace(/\D/g, '')).toLocaleString('es-CO');
    setFormData((prevData) => ({
      ...prevData,
      [name]: formattedValue,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (isAdditionalSecurityKeyRequired && formData.additionalSecurityKey !== generatedSecurityKey) {
      alert('La clave adicional ingresada no es correcta.');
      return;
    }
    try {
      const withdrawData = {
        clientId: client.clientId,
        accountId: account.accountId,
        amount: parseFloat(formData.amount.replace(/\./g, '')),
        currency: formData.currency,
        securityKey: formData.securityKey,
        additionalSecurityKey: formData.additionalSecurityKey
      };
      await withdrawMoney(withdrawData);
      alert('Dinero retirado con éxito');
      setFormData(initialFormData);
      setIsModalVisible(false);
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
          <input type='text' id='amount' name='amount' required value={formData.amount} onChange={handleAmountChange} onBlur={handleAmountBlur} />

          <label className='MoneyWithdrawal-label'>Seleccionar moneda:</label>
          <select id='currency' name='currency' value={formData.currency} onChange={handleChange}>
            <option value='COP'>COP</option>
            <option value='USD'>USD</option>
            <option value='EUR'>EUR</option>
          </select>

          <label className='MoneyWithdrawal-label'>Escriba su clave para confirmar</label>
          <input type='password' id='securityKey' name='securityKey' required value={formData.securityKey} onChange={handleChange} />

          {isAdditionalSecurityKeyRequired && (
            <>
              <label className='MoneyWithdrawal-label'>Clave adicional:</label>
              <input type='password' id='additionalSecurityKey' name='additionalSecurityKey' required value={formData.additionalSecurityKey} onChange={handleChange} />
            </>
          )}

          <button type='submit' className='submit-button'>Retirar</button>
        </form>
      </fieldset>
      <button className='back-button' onClick={onBack}>Volver</button>

      <Modal isVisible={isModalVisible} onClose={() => setIsModalVisible(false)}>
        <p>Por favor, ingrese la siguiente clave adicional manualmente:</p>
        <p className='unselectable'><strong>{generatedSecurityKey}</strong></p>
      </Modal>
    </div>
  );
};
