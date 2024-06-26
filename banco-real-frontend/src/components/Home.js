import React, { useState, useEffect } from 'react';
import './css/Home.css';
import { AccountServices } from './AccountServices';
import { getAccountsByClientId } from './api';

export const Home = ({ user, onLogout }) => {
    const [accounts, setAccounts] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(null);

    useEffect(() => {
        if (user && user.clientId) {
            const fetchAccounts = async () => {
                try {
                    const accounts = await getAccountsByClientId(user.clientId);
                    setAccounts(accounts);
                } catch (error) {
                    console.error("Error buscando cuentas:", error);
                }
            };

            fetchAccounts();
        }
    }, [user]);

    const handleAccountClick = (accountId) => {
        document.getElementById('Home').style.marginBottom = '6px';
        const account = accounts.find(account => account.accountId === accountId);
        if (account.accountStatus === 'CANCELED') {
            alert('Su cuenta está cancelada.');
        } else if (account.accountStatus === 'EMBARGOED') {
            alert('Su cuenta ha sido embargada.');
        } else {
            setSelectedAccount(account);
        }
    };

    const handleBack = () => {
        document.getElementById('Home').style.marginBottom = '250px';
        setSelectedAccount(null);
    };

    const getAccountStatusLabel = (status) => {
        switch (status) {
            case 'OPEN':
                return 'Abierta';
            case 'CANCELED':
                return 'Cancelada';
            case 'EMBARGOED':
                return 'Embargada';
            default:
                return status;
        }
    };

    const getAccountTypeLabel = (accountType) => {
        switch (accountType) {
            case 'class com.bancoreal.model.CurrentAccount':
                return 'Corriente';
            case 'class com.bancoreal.model.SavingAccount':
                return 'Ahorros';
            case 'class com.bancoreal.model.SupremeAccount':
                return 'Suprema';
            default:
                return accountType;
        }
    };

    const formatBalance = (balance) => {
        return balance.toLocaleString('es-CO', { style: 'currency', currency: 'COP' }).replace('COP', '').trim();
    };

    return (
        <div id='Home' className='Home'>
            <div className='Home-header'>
                <h2>Bienvenido, {user.firstName} {user.lastName}</h2>
                <button onClick={onLogout} id='Home-header-logout-btn'>Cerrar sesión</button>
            </div>

            {selectedAccount ? (
                <AccountServices client={user} account={selectedAccount} onBack={handleBack} />
            ) : (
                <div className='Home-main'>
                    <div className='Home-accounts'>
                        <h3>Tus Cuentas</h3>
                        <div className='Home-accounts-list'>
                            {accounts.length > 0 ? (
                                accounts.map(account => (
                                    <div className='Home-account-card' key={account.accountId} onClick={() => handleAccountClick(account.accountId)}>
                                        <p>ID: {account.accountId}</p>
                                        <p>Estado: {getAccountStatusLabel(account.accountStatus)}</p>
                                        <p>Saldo: {formatBalance(account.balance)}</p>
                                        <p>Tipo: {getAccountTypeLabel(account.accountType)}</p>
                                    </div>
                                ))
                            ) : (
                                <p>Usted no tiene cuentas ahora mismo.</p>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};
