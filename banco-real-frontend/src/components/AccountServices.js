import React from 'react';
import { useNavigate } from 'react-router-dom';
import './css/AccountServices.css';
import withdrawMoneyIcon from './images/withdraw-money.png';
import transferMoneyIcon from './images/transfer-money.png';
import historyTransactionIcon from './images/history-transaction.png';

export const AccountServices = ({ client, account, onBack }) => {
  const navigate = useNavigate();

  const handleRedirect = (path) => {
    navigate(path);
  };

  return (
    <div className="AccountServices">
      <button className="back-button" onClick={onBack}>Volver</button>
      <h2>Número de cuenta: {account.accountId}</h2>
      <div className="services-buttons">
        <button className="service-button" onClick={() => handleRedirect('/money-withdrawal')}>
          <img src={withdrawMoneyIcon} alt="Retirar Dinero" />
        </button>
        <button className="service-button" onClick={() => handleRedirect('/money-transfer')}>
          <img src={transferMoneyIcon} alt="Transferir Dinero" />
        </button>
        <button className="service-button" onClick={() => handleRedirect('/transaction-history')}>
          <img src={historyTransactionIcon} alt="Historial de Transacciones" />
        </button>
      </div>
    </div>
  );
};
