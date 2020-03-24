package servlets;

import accounts.AccountService;
import com.google.gson.Gson;
import dbService.dataSets.UsersDataSet;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ProfileServlet",
        description = " Servlet profile",
        urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {
    private final AccountService accountService;
    private String param;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = "/home/asya/Загрузки/javaApps/Simple web server with Jetty/public_html/profile.html";
            /*ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(req, resp);*/
//            req.getRequestDispatcher("public_html/profile.html").forward(req, resp);
        System.out.println(req);
        PrintWriter writer = resp.getWriter();
        writer.println("Method GET from AddServlet");
        resp.getWriter().println("profile_GET............. ");
        String sessionId = req.getSession().getId();
        System.out.println(sessionId);
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
//            RequestDispatcher requestDispatcher = req.getRequestDispatcher("profile.html");
//            requestDispatcher.forward(req, resp);
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", profile.getLogin());
        resp.getWriter().println(PageGenerator.instance().getPage("profile.html", pageVariables));


            /*if (profile == null) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {*/
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(json);
        resp.setStatus(HttpServletResponse.SC_OK);
//            }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);

        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Unauthorized");
            return;
        }

        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", profile.getLogin());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.instance().getPage("game.html", pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }
}