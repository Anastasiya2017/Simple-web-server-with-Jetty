package servlets;

import accounts.AccountService;
import dbService.dataSets.UsersDataSet;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutputServlet extends HttpServlet {
    private final AccountService accountService;

    public OutputServlet(AccountService accountService) {
        this.accountService = accountService;
    }


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
        resp.setContentType("text/html;charset=utf-8");
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);

        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Unauthorized");
            return;
        }
        accountService.deleteSession(profile);
//        req.logout();
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", profile.getLogin());
        resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
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