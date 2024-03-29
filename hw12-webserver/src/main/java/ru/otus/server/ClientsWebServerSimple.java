package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.authentication.AuthenticationService;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.ClientsServlet;
import ru.otus.servlet.LoginServlet;

import java.util.Arrays;


public class ClientsWebServerSimple implements ClientsWebServer {
    private static final String START_PAGE_NAME = "/login.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final AuthenticationService authenticationService;
    private final DBServiceClient dbServiceClient;

    public ClientsWebServerSimple(int port,
                                  AuthenticationService authenticationService,
                                  TemplateProcessor templateProcessor,
                                  DBServiceClient dbServiceClient) {
        this.authenticationService = authenticationService;
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/clients", "/add_client"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authenticationService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        var clientServlet = new ClientsServlet(templateProcessor, dbServiceClient);
        servletContextHandler.addServlet(new ServletHolder(clientServlet), "/clients");
        servletContextHandler.addServlet(new ServletHolder(clientServlet), "/add_client");
        return servletContextHandler;
    }
}
