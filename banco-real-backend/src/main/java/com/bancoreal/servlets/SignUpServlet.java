package com.bancoreal.servlets;

import com.bancoreal.dao.ClientDAO;
import com.bancoreal.model.Client;
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

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private ClientDAO clientDAO;

    public SignUpServlet() {
        this.clientDAO = new ClientDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json");

        try {
            JSONObject jsonRequest = readJsonFromRequest(request);

            String firstName = jsonRequest.getString("firstName");
            String lastName = jsonRequest.getString("lastName");
            String clientId = jsonRequest.getString("clientId");
            String user = jsonRequest.getString("user");
            String securityKey = jsonRequest.getString("securityKey");

            Client existingClientById = clientDAO.selectClientById(clientId);
            Client existingClientByUser = clientDAO.selectClientByUser(user);

            if (existingClientById != null) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("message", "El cliente con este número de identificación ya está registrado.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print(errorJson.toString());
                out.flush();
                return;
            }

            if (existingClientByUser != null) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("message", "El cliente con este usuario ya está registrado.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print(errorJson.toString());
                out.flush();
                return;
            }

            String encryptedKey = encryptKey(securityKey);

            Client newClient = new Client(firstName, lastName, clientId, user, encryptedKey);
            clientDAO.insertClient(newClient);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Registro exitoso");
            jsonResponse.put("client", newClient.toJson());

            response.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("message", "Error interno del servidor: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.print(errorJson.toString());
            out.flush();
        }
    }

    private String encryptKey(String securityKey) {
        return BCrypt.hashpw(securityKey, BCrypt.gensalt());
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
