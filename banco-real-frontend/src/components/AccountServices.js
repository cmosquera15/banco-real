import React from 'react';
import { useNavigate } from 'react-router-dom';
import './css/AccountServices.css';
import withdrawMoneyIcon from './images/withdraw-money.png';
import transferMoneyIcon from './images/transfer-money.png';
import historyTransactionIcon from './images/history-transaction.png';

export const AccountServices = ({ client, account, onBack }) => {
  const navigate = useNavigate();

  const handleRedirect = (path, client, account) => {
    navigate(path, { state: { client, account } });
  };

  return (
    <div className="AccountServices">
      <h2>Número de cuenta: {account.accountId}</h2>
      
      <div className="services-buttons">
        <div className="service-button">
          <button className="service-button-transfer" onClick={() => handleRedirect('/money-transfer', client, account, onBack)}>
            <img src={transferMoneyIcon} alt="Transferir Dinero" />
          </button>
          <p className='text-transfer'>Enviar dinero</p>
        </div>

        <div className="service-button">
          <button className="service-button-withdraw" onClick={() => handleRedirect('/money-withdrawal', client, account, onBack)}>
            <img src={withdrawMoneyIcon} alt="Retirar Dinero" />
          </button>
          <p className='text-withdraw'>Retirar dinero</p>
        </div>

        <div className="service-button">
          <button className="service-button-history" onClick={() => handleRedirect('/transaction-history', client, account, onBack)}>
            <img src={historyTransactionIcon} alt="Historial de Transacciones" />
          </button>
          <p className='text-history'>Historial</p>
        </div>
      </div>

      <button className="back-button" onClick={onBack}>Volver</button>
    </div>
  );
};
