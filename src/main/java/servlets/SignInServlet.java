package servlets;

import accounts.AccountService;
import com.google.gson.Gson;
import dbService.DBException;
import dbService.dataSets.UsersDataSet;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(profile);
            resp.getWriter().println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (login == null || password == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UsersDataSet profile = null;
        try {
            profile = accountService.getUserByLogin(login);
            String sessionId = req.getSession().getId();
            accountService.addSession(sessionId, profile);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Unauthorized");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", profile.getLogin());
        resp.getWriter().println(PageGenerator.instance().getPage("profile.html", pageVariables));
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