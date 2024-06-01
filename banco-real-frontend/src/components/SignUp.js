import React, { useState } from 'react';
import './css/SignUp.css';
import { createClient } from './api';

export const SignUp = ({ onSignUpSuccess }) => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    clientId: '',
    user: '',
    securityKey: ''
  });
  const [error, setError] = useState('');
  const [formPart, setFormPart] = useState(1);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateFormPart1 = () => {
    const nameRegex = /^[A-Za-z]+$/;
    const clientIdRegex = /^[A-Za-z0-9]{8,50}$/;

    if (!nameRegex.test(formData.firstName) || !nameRegex.test(formData.lastName)) {
      return 'Nombre y Apellido solo pueden contener letras.';
    }
    if (!clientIdRegex.test(formData.clientId)) {
      return 'Client ID debe contener entre 8 y 50 caracteres, solo letras y números.';
    }

    return '';
  };

  const validateFormPart2 = () => {
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

  const handleSubmitPart1 = (e) => {
    e.preventDefault();
    const errorMessage = validateFormPart1();
    if (errorMessage) {
      setError(errorMessage);
      return;
    }
    setError('');
    setFormPart(2);
  };

  const handleSubmitPart2 = async (e) => {
    e.preventDefault();
    const errorMessage = validateFormPart2();
    if (errorMessage) {
      setError(errorMessage);
      return;
    }

    try {
      await createClient(formData);
      onSignUpSuccess();
    } catch (err) {
      setError('Error en el registro, por favor intente nuevamente.');
    }
  };

  const handleBackToPart1 = (e) => {
    e.preventDefault();
    setFormPart(1);
  };

  return (
    <div className="SignUp">
      <h2>Registrarse</h2>
      {error && <p className="error">{error}</p>}
      {formPart === 1 && (
        <form onSubmit={handleSubmitPart1}>
          <div className="form-section">
            <label>
              Nombre:
              <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
            </label>
            <label>
              Apellido:
              <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
            </label>
            <label>
              No. de identificación:
              <input type="text" name="clientId" value={formData.clientId} onChange={handleChange} required />
            </label>
          </div>
          <button type="submit">Continuar</button>
        </form>
      )}
      {formPart === 2 && (
        <form onSubmit={handleSubmitPart2}>
          <div className="form-section">
            <label>
              Usuario:
              <input type="text" name="user" value={formData.user} onChange={handleChange} required />
            </label>
            <label>
              Clave:
              <input type="password" name="securityKey" value={formData.securityKey} onChange={handleChange} required />
            </label>
          </div>
          <div className="button-section">
            <button onClick={handleBackToPart1}>Regresar a la primera parte</button>
            <button type="submit">Registrar</button>
          </div>
        </form>
      )}
    </div>
  );
};
