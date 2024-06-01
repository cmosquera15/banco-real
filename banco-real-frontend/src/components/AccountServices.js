import React from 'react';
import { useNavigate } from 'react-router-dom';
import './css/AccountServices.css';
import withdrawMoneyIcon from './images/withdraw-money.png';
import transferMoneyIcon from './images/transfer-money.png';
import historyTransactionIcon from './images/history-transaction.png';

export const AccountServices = ({ client, account, onBack }) => {
  const navigate = useNavigate();

  const handleRedirect = (path, client, account) => {
    navigate(path, client, account);
  };

  return (
    <div className="AccountServices">
      <button className="back-button" onClick={onBack}>Volver</button>
 
      <h2>Número de cuenta: {account.accountId}</h2>
      
      <div className="services-buttons">
        <button className="service-button" onClick={() => handleRedirect('/money-transfer', client, account, onBack)}>
          <img src={transferMoneyIcon} alt="Transferir Dinero" />
        </button>

        <button className="service-button" onClick={() => handleRedirect('/money-withdrawal', client, account, onBack)}>
          <img src={withdrawMoneyIcon} alt="Retirar Dinero" />
        </button>

        <button className="service-button" onClick={() => handleRedirect('/transaction-history', client, account, onBack)}>
          <img src={historyTransactionIcon} alt="Historial de Transacciones" />
        </button>
      </div>
    </div>
  );
};
