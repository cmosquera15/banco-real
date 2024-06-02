import React, { useState } from 'react';
import './css/AccountServices.css';
import withdrawMoneyIcon from './images/withdraw-money.png';
import transferMoneyIcon from './images/transfer-money.png';
import historyTransactionIcon from './images/history-transaction.png';
import { MoneyTransfer } from './MoneyTransfer';
import { MoneyWithdrawal } from './MoneyWithdrawal';
import { TransactionHistory } from './TransactionHistory';

export const AccountServices = ({ client, account, onBack }) => {
  const [showTransfer, setShowTransfer] = useState(false);
  const [showWithdrawal, setShowWithdrawal] = useState(false);
  const [showHistory, setShowHistory] = useState(false);

  const handleShowTransfer = () => {
    setShowTransfer(true);
    setShowWithdrawal(false);
    setShowHistory(false);
  };

  const handleShowWithdrawal = () => {
    setShowTransfer(false);
    setShowWithdrawal(true);
    setShowHistory(false);
  };

  const handleShowHistory = () => {
    setShowTransfer(false);
    setShowWithdrawal(false);
    setShowHistory(true);
  };

  const handleBack = () => {
    setShowTransfer(false);
    setShowWithdrawal(false);
    setShowHistory(false);
  };

  const showButtons = !(showTransfer || showWithdrawal || showHistory);

  return (
    <div className="AccountServices">
      {showButtons && (
        <>
          <h2>Servicios para la cuenta {account.accountId}</h2>
      
          <div className="services-buttons">
            <div className="service-button">
              <button className="service-button-transfer" onClick={handleShowTransfer}>
                <img src={transferMoneyIcon} alt="Transferir Dinero" />
              </button>
              <p className='text-transfer'>Enviar dinero</p>
            </div>

            <div className="service-button">
              <button className="service-button-withdraw" onClick={handleShowWithdrawal}>
                <img src={withdrawMoneyIcon} alt="Retirar Dinero" />
              </button>
              <p className='text-withdraw'>Retirar dinero</p>
            </div>

            <div className="service-button">
              <button className="service-button-history" onClick={handleShowHistory}>
                <img src={historyTransactionIcon} alt="Historial de Transacciones" />
              </button>
              <p className='text-history'>Historial</p>
            </div>
          </div>
          
          <button className="back-button" onClick={onBack}>Volver</button>
        </>
      )}

      {showTransfer && (
        <MoneyTransfer client={client} account={account} onBack={handleBack} />
      )}

      {showWithdrawal && (
        <MoneyWithdrawal client={client} account={account} onBack={handleBack} />
      )}

      {showHistory && (
        <TransactionHistory account={account} onBack={handleBack} />
      )}
    </div>
  );
};
