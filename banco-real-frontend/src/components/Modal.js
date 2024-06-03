import React from 'react';
import './css/Modal.css';

export const Modal = ({ isVisible, onClose, children }) => {
  if (!isVisible) return null;

  return (
    <div className='modal-overlay'>
      <div className='modal-content'>
        {children}
        <button className='modal-close-button' onClick={onClose}>Cerrar</button>
      </div>
    </div>
  );
};
