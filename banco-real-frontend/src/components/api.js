import axios from 'axios';


const URL_DB = "http://localhost:8080/banco-real-backend-1.0-SNAPSHOT";

export const getClient = async (transferData) => {
  try {
    const response = await axios.post(`${URL_DB}/login`, transferData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    throw new Error('Error en la autenticación');
  }
};


export const createClient = async (transferData) => {
  try {
    const response = await axios.post(`${URL_DB}/signup`, transferData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response;
  } catch (error) {
    throw new Error('Error en el registro');
  }
};

export const getAccountsByClientId = async (clientId) => {
  try {
    const response = await fetch(`${URL_DB}/account/accounts?clientId=${clientId}`);

    if (!response.ok) {
      throw new Error('Error fetching accounts');
    }

    const data = await response.json();
    return data.accounts;
  } catch (error) {
    throw new Error('Error fetching accounts: ' + error.message);
  }
};

export const getTransactionsByAccountId = async (accountId) => {
  try {
    const response = await fetch(`${URL_DB}/account/transactions?accountId=${accountId}`);

    if (!response.ok) {
      throw new Error('Error fetching transactions');
    }

    const data = await response.json();
    return data.transactions;
  } catch (error) {
    throw new Error('Error fetching transactions: ' + error.message);
  }
};

export const withdrawMoney = async (withdrawData) => {
  try {
    const response = await fetch(`${URL_DB}/account/withdraw`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(withdrawData)
    });

    if (!response.ok) {
      throw new Error('Error withdrawing money');
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error('Error withdrawing money: ' + error.message);
  }
};

export const transferMoney = async (transferData) => {
  try {
    const response = await fetch(`${URL_DB}/account/transfer`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(transferData)
    });

    if (!response.ok) {
      throw new Error('Error transferring money');
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error('Error transferring money: ' + error.message);
  }
};
