package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String ADD_CLIENTS_PAGE_TEMPLATE = "add_client.html";
    private static final String TEMPLATE_ATTR_CLIENT_LIST = "client_list";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        if (req.getServletPath().contains("/add_client")) {
            response.getWriter().println(templateProcessor.getPage(ADD_CLIENTS_PAGE_TEMPLATE, Collections.emptyMap()));
        } else {
            var clients = dbServiceClient.findAll();
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put(TEMPLATE_ATTR_CLIENT_LIST, clients);

            response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        var client = new Client(null,
               req.getParameter("name"),
               new Address(null, req.getParameter("address")),
               Collections.singletonList(new Phone(null, req.getParameter("phone")))
        );
        dbServiceClient.saveClient(client);
        resp.sendRedirect("/clients");
    }
}
