package com.bancoreal.servlets;

import com.bancoreal.dao.AccountDAO;
import com.bancoreal.dao.ClientDAO;
import com.bancoreal.dao.TransactionDAO;
import com.bancoreal.dto.TransferRequest;
import com.bancoreal.dto.WithdrawRequest;
import com.bancoreal.model.Account;
import com.bancoreal.model.Client;
import com.bancoreal.model.CurrentAccount;
import com.bancoreal.model.SavingAccount;
import com.bancoreal.model.SupremeAccount;
import com.bancoreal.model.Transaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/account/*")
public class AccountServlet extends HttpServlet {
    private AccountDAO accountDAO;
    private ClientDAO clientDAO;
    private TransactionDAO transactionDAO;

    public AccountServlet() {
        this.accountDAO = new AccountDAO();
        this.clientDAO = new ClientDAO();
        this.transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("")) {
            getAccountById(request, response);
        } else if (pathInfo.equals("/accounts")) {
            String clientId = request.getParameter("clientId");
            if (clientId != null) {
                getAccountsByClientId(request, response, clientId);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "El parámetro 'clientId' es requerido");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
            }
        } else if (pathInfo.equals("/transactions")) {
            String accountId = request.getParameter("accountId");
            if (accountId != null) {
                getTransactionsByAccountId(request, response, accountId);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "El parámetro 'accountId' es requerido");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "URL de solicitud no válida");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("")) {
            createAccount(request, response);
        } else if (pathInfo.equals("/transfer")) {
            transferMoney(request, response);
        } else if (pathInfo.equals("/withdraw")) {
            withdrawMoney(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "URL de solicitud no válida");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/delete")) {
            deleteAccount(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "URL de solicitud no válida");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private void createAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            JSONObject jsonRequest = readJsonFromRequest(request);
    
            String clientId = jsonRequest.getString("clientId");
            String accountType = jsonRequest.getString("accountType");
            String securityKey = jsonRequest.getString("securityKey");
    
            Client client = clientDAO.selectClientById(clientId);
    
            if (client != null && BCrypt.checkpw(securityKey, client.getSecurityKey())) {
                Account newAccount;
                switch (accountType) {
                    case "CurrentAccount":
                        newAccount = new CurrentAccount(client);
                        break;
                    case "SavingAccount":
                        newAccount = new SavingAccount(client);
                        break;
                    case "SupremeAccount":
                        newAccount = new SupremeAccount(client);
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("message", "Tipo de cuenta no válido");
                        PrintWriter out = response.getWriter();
                        out.print(jsonResponse.toString());
                        out.flush();
                        return;
                }
    
                accountDAO.insertAccount(newAccount);
                
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cuenta creada exitosamente");
                jsonResponse.put("account", newAccount.toJson());
                jsonResponse.put("accountType", accountType);
                
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cliente no encontrado");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }  
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
    
    private void getAccountsByClientId(HttpServletRequest request, HttpServletResponse response, String clientId) throws IOException {
        try {
            Client client = clientDAO.selectClientById(clientId);
    
            if (client != null) {
                List<Account> accounts = accountDAO.selectAccountsByClientId(clientId);

                if (accounts == null || accounts.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("message", "No se encontraron cuentas para el cliente con ID: " + clientId);
                    PrintWriter out = response.getWriter();
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }

                client.setAccounts(accounts);

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject clientJsonResponse = client.toJson();
                out.print(clientJsonResponse.toString());
                out.flush();                
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cliente no encontrado");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private void getAccountById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accountId = request.getParameter("accountId");

            if (accountId == null || accountId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "accountId es requerido");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }

            Account account = accountDAO.selectAccountById(accountId);

            if (account == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cuenta no encontrada");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("account", account.toJson());
            out.print(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            JSONObject jsonRequest = readJsonFromRequest(request);
    
            String clientId = jsonRequest.getString("clientId");
            String accountId = jsonRequest.getString("accountId");
            String securityKey = jsonRequest.getString("securityKey");
    
            Client client = clientDAO.selectClientById(clientId);

            if (clientId != null && BCrypt.checkpw(securityKey, client.getSecurityKey())) {
                if (accountId == null || accountId.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("message", "accountId es requerido");
                    PrintWriter out = response.getWriter();
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }
    
                accountDAO.deleteAccountById(accountId);
        
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cuenta eliminada exitosamente");
        
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cliente no encontrado");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }  
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private void getTransactionsByAccountId(HttpServletRequest request, HttpServletResponse response, String accountId) throws IOException {
        try {
            Account account = accountDAO.selectAccountById(accountId);

            if (account != null) {
                List<Transaction> transactions = transactionDAO.selectTransactionsByAccountId(accountId);

                if (transactions == null || transactions.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("message", "Transacciones no encontradas para la cuenta con ID: " + accountId);
                    PrintWriter out = response.getWriter();
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }

                account.setTransactions(transactions);

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject accountJsonResponse = account.toJson();
                out.print(accountJsonResponse.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cuenta no encontrada");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private void transferMoney(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            JSONObject jsonRequest = readJsonFromRequest(request);

            String clientId = jsonRequest.getString("senderClientId");
            String senderAccountId = jsonRequest.getString("senderAccountId");
            String recipientAccountId = jsonRequest.getString("recipientAccountId");
            double amount = jsonRequest.getDouble("amount");
            String currency = jsonRequest.getString("currency");
            String securityKey = jsonRequest.getString("securityKey");

            Client client = clientDAO.selectClientById(clientId);

            if (client != null && BCrypt.checkpw(securityKey, client.getSecurityKey())) {
                TransferRequest transferRequest = new TransferRequest(senderAccountId, recipientAccountId, amount, currency);
                
                if (accountDAO.transferMoney(transferRequest)) {
                    JSONObject jsonResponse = new JSONObject(); 
                    jsonResponse.put("message", "Transferencia exitosa");

                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(jsonResponse.toString());
                    out.flush();
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cliente no encontrado");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private void withdrawMoney(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            JSONObject jsonRequest = readJsonFromRequest(request);

            String clientId = jsonRequest.getString("clientId");
            String accountId = jsonRequest.getString("accountId");
            double amount = jsonRequest.getDouble("amount");
            String currency = jsonRequest.getString("currency");
            String securityKey = jsonRequest.getString("securityKey");

            Client client = clientDAO.selectClientById(clientId);

            if (client != null && BCrypt.checkpw(securityKey, client.getSecurityKey())) {
                WithdrawRequest withdrawRequest = new WithdrawRequest(accountId, amount, currency);
                
                if (accountDAO.withdrawMoney(withdrawRequest)) {
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("message", "Retiro exitoso");

                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(jsonResponse.toString());
                    out.flush();
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Cliente no encontrado");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
                return;
            }    
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private JSONObject readJsonFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return new JSONObject(sb.toString());
    }

    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
