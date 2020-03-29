package servlets;

import accounts.AccountService;
import com.google.gson.Gson;
import dbService.dataSets.Personage;
import dbService.dataSets.Profile;
import dbService.dataSets.UsersDataSet;
import org.json.JSONException;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "personage", urlPatterns = {"/personage"})
public class PersonageServlet extends HttpServlet {
    private final AccountService accountService;

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, JSONException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            String sessionId = req.getSession().getId();
            UsersDataSet profile = accountService.getUserBySessionId(sessionId);
            if (profile == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                JSONObject jsonEnt = new JSONObject();
                String inform = req.getParameter("inform");
                if (inform.equals("add")) {
                    String src = req.getParameter("src");
                    String namePersonage = req.getParameter("characterName");
                    String numEl = req.getParameter("numEl");
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    Date date = new Date();
                    String idPersonage = "" + date.getTime();
                    accountService.addIdPersonage(login, idPersonage);
                    accountService.addPersonage(namePersonage, login, src, idPersonage);
//                if (req.getParameter("login").equals("HELOOO!!!!!0-0")) {
//                    jsonEnt.put("backgroundColor", "#99CC66");
//                    jsonEnt.put("serverInfo", "Вы вошли!");
//                } else {
//                    jsonEnt.put("backgroundColor", "#CC6666");
//                    jsonEnt.put("serverInfo", "Введен !");
//                }
                    out.print(jsonEnt.toString());
                }
                if (inform.equals("del")) {
                    String namePersonage = req.getParameter("personageName");
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    String idPersonage = accountService.deletePersonage(login, namePersonage);
                    accountService.deleteIdPersonage(login, idPersonage);
                    out.print(jsonEnt.toString());
                }
                if (inform.equals("upd")) {
                    String namePersonage = req.getParameter("personageName");
                    String src = req.getParameter("src");
                    String oldNameEd = req.getParameter("oldNameEd");
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    accountService.updatePersonage(login, src, namePersonage, oldNameEd);
                }
                if (inform.equals("selectPersonage")) {
                    String namePersonage = req.getParameter("personageName");
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    System.out.println("selectPersonage!!" + login + " " + namePersonage);
                    accountService.selectPersonage(login, namePersonage);
                    System.out.println("успех!!");
//                    resp.sendRedirect("/game");
                }
            }
        }
    }

    public PersonageServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        if (profile == null) {
            pageVariables.put("message", "null");
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            pageVariables.put("message", profile.getLogin());
            List<Profile> usersPersonage = accountService.getAllPersonages(profile.getLogin());
            usersPersonage.forEach(System.out::println);
            List<Personage> allPersonagesUser = accountService.getPersonages(profile.getLogin());
            allPersonagesUser.forEach(System.out::println);
            String jsonStr = new Gson().toJson(allPersonagesUser);
            pageVariables.put("personages", jsonStr);
            req.setAttribute("personages", jsonStr);
            int i = 1;
            for (Personage pers :
                    allPersonagesUser) {
                pageVariables.put("addcharactername" + i + "name", pers.getName());
                pageVariables.put("addcharacter" + i, pers.getImg());
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.instance().getPage("profile.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    //add character
    public void doPost(HttpServletRequest req,
                       HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (JSONException ex) {
            Logger.getLogger(UsersDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
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