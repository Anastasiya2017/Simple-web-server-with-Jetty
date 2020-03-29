package main;

import accounts.AccountService;
import chat.WebSocketChatServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

public class Main {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();

        accountService.addNewUser(("admin"));
        accountService.addNewUser(("test"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //создаем 2 сервлета: 1) для регистрации
        //                    2) для авторизации
        // и закладываем эти сервлеты по адресам
        context.addServlet(new ServletHolder(new PersonageServlet(accountService)), "/personage");
        context.addServlet(new ServletHolder(new WebSocketChatServlet(accountService)), "/game");
        context.addServlet(new ServletHolder(new ProfileServlet(accountService)), "/profile");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new OutputServlet(accountService)), "/signout");
//        context.addServlet(new ServletHolder(new PersonageServlet(accountService)), "/api/v1/users");
        //добавляем возможность работать со статическими файлами
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
//        java.util.logging.Logger.getGlobal().info("Server started");
        server.join();
    }
}