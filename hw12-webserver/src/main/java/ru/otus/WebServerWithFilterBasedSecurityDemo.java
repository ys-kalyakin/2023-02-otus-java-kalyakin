package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.authentication.SimpleAuthenticationService;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.server.ClientsWebServer;
import ru.otus.server.ClientsWebServerSimple;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        ClientsWebServer usersWebServer = new ClientsWebServerSimple(
                WEB_SERVER_PORT,
                new SimpleAuthenticationService(),
                templateProcessor,
                dbServiceClient
        );

        usersWebServer.start();
        usersWebServer.join();
    }
}
