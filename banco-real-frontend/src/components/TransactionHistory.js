import React, { useState, useEffect } from 'react';
import './css/TransactionHistory.css';
import { getTransactionsByAccountId } from './api';

export const TransactionHistory = ({ account, onBack }) => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchTransactions = async () => {
      if (account && account.accountId) {
        try {
          setLoading(true);
          const response = await getTransactionsByAccountId(account.accountId);
          if (response && response.transactions) {
            setTransactions(response.transactions);
          }
        } catch (error) {
          console.error("Error buscando transacciones:", error);
        } finally {
          setLoading(false);
        }
      }
    };

    fetchTransactions();
  }, [account]);

  const getTransactionTypeLabel = (type) => {
    switch (type) {
      case 'TRANSFER':
        return 'Pago realizado';
      case 'RECEIPT':
        return 'Envio recibido';
      case 'WITHDRAW':
        return 'Retiro';
      default:
        return type;
    }
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', options);
  };

  const formatBalance = (balance) => {
    return parseFloat(balance).toLocaleString('es-CO');
  };

  if (!account) {
    return (
      <div>
        <p>Cuenta no seleccionada.</p>
        <button className='back-button' onClick={onBack}>Volver</button>
      </div>
    );
  }

  return (
    <div className='TransactionHistory'>
      <h2>Historial de Transacciones</h2>
      {loading ? (
        <p>Cargando transacciones...</p>
      ) : (
        <React.Fragment>
          {transactions && transactions.length > 0 ? (
            <table className='TransactionHistory-table'>
              <thead>
                <tr>
                  <th>ID Transacción</th>
                  <th>Monto</th>
                  <th>Moneda</th>
                  <th>Tipo</th>
                  <th>Fecha</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map(transaction => (
                  <tr key={transaction.transactionId}>
                    <td>{transaction.transactionId}</td>
                    <td>{formatBalance(transaction.amount)}</td>
                    <td>{transaction.currency}</td>
                    <td>{getTransactionTypeLabel(transaction.transactionType)}</td>
                    <td>{formatDate(transaction.transactionDate)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>Usted no ha realizado transacciones.</p>
          )}
        </React.Fragment>
      )}
      <button className='back-button' onClick={onBack}>Volver</button>
    </div>
  );
};
