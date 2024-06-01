import React, { useState } from 'react';
import './css/Login.css';
import { getClient } from './api';

export const Login = ({ onLoginSuccess }) => {
  const [formData, setFormData] = useState({
    user: '',
    securityKey: ''
  });
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateForm = () => {
    const userRegex = /^.{8,}$/;
    const securityKeyRegex = /^[0-9]{4}$/;

    if (!userRegex.test(formData.user)) {
      return 'Usuario debe tener al menos 8 caracteres.';
    }
    if (!securityKeyRegex.test(formData.securityKey)) {
      return 'Clave debe tener exactamente 4 números.';
    }

    return '';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errorMessage = validateForm();
    if (errorMessage) {
      setError(errorMessage);
      return;
    }

    try {
      const userData = await getClient(formData);
      onLoginSuccess(userData);
    } catch (err) {
      setError('Error en el inicio de sesión, por favor intente nuevamente.');
    }
  };

  return (
    <div className="Login">
      <h2>Iniciar sesión</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <label>
          Usuario:
          <input type="text" name="user" value={formData.user} onChange={handleChange} required/>
        </label>
        <label>
          Clave:
          <input type="password" name="securityKey" value={formData.securityKey} onChange={handleChange} required/>
        </label>
        <button type="submit">Iniciar sesión</button>
      </form>
    </div>
  );
};