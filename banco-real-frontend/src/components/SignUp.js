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
  const [formPart, setFormPart] = useState(1);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateFormPart1 = () => {
    const firstName = formData.firstName
    const lastName = formData.lastName
    const clientId = formData.clientId

    if (!firstName || !lastName || !clientId) {
      alert ("Complete todos los campos los campos.")
    } else {
      setFormPart(2);
    }
  };

  const handleBackToPart1 = (e) => {
    e.preventDefault();
    setFormPart(1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const transferData = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        clientId: formData.clientId,
        user: formData.user,
        securityKey: formData.securityKey
      }
      const userData = await createClient(transferData);
      onSignUpSuccess(userData);
    } catch (e) {
      alert ('Error en el registro, por favor intente nuevamente.')
    }
  };

  return (
    <div className="SignUp">
      <h2>Registrarse</h2>
      <form onSubmit={formPart === 1 ? validateFormPart1 : handleSubmit}>
        {formPart === 1 && (
          <div className="form-section">
            <label>
              Nombre:
              <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required pattern="[A-Za-z]+" title="El nombre solo puede contener letras"/>
            </label>
            <label>
              Apellido:
              <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required pattern="[A-Za-z]+" title="El apellido solo puede contener letras"/>
            </label>
            <label>
              No. de identificación:
              <input type="text" name="clientId" value={formData.clientId} onChange={handleChange} required pattern="[A-Za-z0-9]{8,50}" title="El no. de identificación solo debe contener entre 8 y 50 números y/o letras"/>
            </label>
            <div className='button-section'>
              <button type="button" onClick={validateFormPart1}>Continuar</button>
            </div>
          </div>
        )}
        {formPart === 2 && (
          <div className="form-section">
            <label>
              Usuario:
              <input type="text" name="user" value={formData.user} onChange={handleChange} required minLength="8" title="El usuario debe tener al menos 8 caracteres"/>
            </label>
            <label>
              Clave:
              <input type="password" name="securityKey" value={formData.securityKey} onChange={handleChange} required pattern="\d{4}" title="La clave debe contener exactamente 4 números"/>
            </label>
            <div className="button-section">
              <button type="button" onClick={handleBackToPart1}>Regresar</button>
              <button type="submit">Registrarse</button>
            </div>
          </div>
        )}
      </form>
    </div>
  );
};
