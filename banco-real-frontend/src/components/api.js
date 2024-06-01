const URL_DB = "http://localhost:8080/banco-real-backend-1.0-SNAPSHOT";

export const createClient = async (formData) => {
  try {
    const response = await fetch(`${URL_DB}/signup`, formData);
    const data = await response;
    return data.accounts;
  } catch (error) {
    throw new Error('Error creating client: ' + error.message);
  }
};

export const getClient = async (formData) => {
  try {
    const response = await fetch(`${URL_DB}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    });

    if (!response.ok) {
      throw new Error('Error en el inicio de sesión');
    }

    const jsonResponse = await response.json();

    if (jsonResponse.message !== "Inicio de sesión exitoso") {
      throw new Error('Error en el inicio de sesión: ' + jsonResponse.message);
    }

    return jsonResponse; // Retorna la respuesta completa del backend
  } catch (error) {
    throw new Error('Error en el inicio de sesión: ' + error.message);
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
    const response = await fetch(`${URL_DB}/transactions?accountId=${accountId}`);

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
    const response = await fetch(`${URL_DB}/withdraw`, {
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
    const response = await fetch(`${URL_DB}/transfer`, {
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
