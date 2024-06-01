import React, { useState } from 'react';
import './css/Login.css';
import { getClient } from './api';

export const Login = ({ onLoginSuccess }) => {
  const [formData, setFormData] = useState({
    user: '',
    securityKey: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const transferData = {
      user: formData.user,
      securityKey: formData.securityKey
    }

    try {
      const userData = await getClient(transferData);
      onLoginSuccess(userData);
    } catch (err) {
      alert('Error en el inicio de sesión, por favor intente nuevamente.');
    }
  };

  return (
    <div className="Login">
      <h2>Iniciar sesión</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Usuario:
          <input type="text" name="user" value={formData.user} onChange={handleChange} required minLength="8" title="El usuario debe tener al menos 8 caracteres" />
        </label>
        <label>
          Clave:
          <input type="password" name="securityKey" value={formData.securityKey} onChange={handleChange} required pattern="\d{4}" title="La clave debe contener exactamente 4 números" />
        </label>
        <button type="submit">Iniciar sesión</button>
      </form>
    </div>
  );
};
