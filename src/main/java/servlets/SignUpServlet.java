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

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            Map<String, Object> pageVariables = createPageVariablesMap(req);
            pageVariables.put("message", profile.getLogin());
            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (login == null || password == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UsersDataSet profile = accountService.getUserByLogin(login);
            if (profile != null) {
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println("уже существует!");
            } else {
                accountService.addNewUser(login);
                String sessionId = req.getSession().getId();
//                accountService.addSession(sessionId, profile);
                resp.setContentType("text/html;charset=utf-8");
//                accountService.addSession(req.getSession().getId(), profile);
                Gson gson = new Gson();
                String json = gson.toJson(profile);
                resp.getWriter().println(login);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
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
