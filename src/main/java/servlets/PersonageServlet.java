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
        System.out.println("ajax ЗАПРОС!!!");
        resp.setContentType("application/json");//Отправляем от сервера данные в JSON -формате
//        response.setCharacterEncoding("utf-8");//Кодировка отправляемых данных
        try (PrintWriter out = resp.getWriter()) {
            String sessionId = req.getSession().getId();
            UsersDataSet profile = accountService.getUserBySessionId(sessionId);
            if (profile == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println("SC_UNAUTHORIZED");
            } else {
                JSONObject jsonEnt = new JSONObject();
                String inform = req.getParameter("inform");
                if (inform.equals("add")) {
                    String src = req.getParameter("src");
                    String namePersonage = req.getParameter("characterName");
                    String numEl = req.getParameter("numEl");
                    System.out.println("_____________src: " + src);
                    System.out.println("_____________idTag: " + namePersonage);
                    System.out.println("_____________numEl: " + numEl);
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    Date date = new Date();
                    String idPersonage = "" + date.getTime();
//                System.out.println("idPersonage= " + idPersonage);
                    accountService.addIdPersonage(login, idPersonage);
                    accountService.addPersonage(namePersonage, login, src, idPersonage);
                    System.out.println("end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
                    String numEl = req.getParameter("numEl");
                    System.out.println("_____________personageName: " + namePersonage + "/");
                    System.out.println("_____________numEl: " + numEl + "/");
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    String idPersonage = accountService.deletePersonage(login, namePersonage);
                    System.out.println("idPersonage " + idPersonage);
                    accountService.deleteIdPersonage(login, idPersonage);
                    System.out.println(" login end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    out.print(jsonEnt.toString());
                }
                if (inform.equals("upd")) {
                    String namePersonage = req.getParameter("personageName");
                    String src = req.getParameter("src");
                    String oldNameEd = req.getParameter("oldNameEd");
                    System.out.println("_____________personageName " + namePersonage);
                    System.out.println("_____________src: " + src);
                    System.out.println("_____________oldNameEd: " + oldNameEd);
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    pageVariables.put("message", profile.getLogin());
                    accountService.updatePersonage(login, src, namePersonage, oldNameEd);
                    System.out.println(" updatePersonage end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        }
    }

    public PersonageServlet(AccountService accountService) {
        this.accountService = accountService;
    }


    //get public user profile
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession().getId();
        System.out.println(sessionId);
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
            System.out.println("List<Profile>:");
            usersPersonage.forEach(System.out::println);
            System.out.println(req.toString());
            List<Personage> allPersonagesUser = accountService.getPersonages(profile.getLogin());
            System.out.println("List<Personage>:");
            allPersonagesUser.forEach(System.out::println);
            String jsonStr = new Gson().toJson(allPersonagesUser);
            System.out.println("jsonStr    >:" + jsonStr);
            pageVariables.put("personages", jsonStr);
            req.setAttribute("personages", jsonStr);
//            resp.addHeader("Content-Type", "application/json");


//            resp.addHeader("Content-Type", "application/json");


//            resp.getWriter().print(json);

//            resp.flushBuffer();
//            resp.getWriter().print(jsonStr);

//            resp.flushBuffer();
            int i = 1;
            for (Personage pers :
                    allPersonagesUser) {
                System.out.println("name " + pers.getName());
                System.out.println("getImg " + pers.getImg());
                pageVariables.put("addcharactername" + i + "name", pers.getName());
                pageVariables.put("addcharacter" + i, pers.getImg());
            }
//            if (!req.toString().contains("characterName")) {
            resp.setContentType("text/html;charset=utf-8");
//            req.getRequestDispatcher("profile.html").forward(req,resp);
            resp.getWriter().println(PageGenerator.instance().getPage("profile.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
           /* } else {
                try {
                    processRequest(req, resp);
                } catch (JSONException ex) {
                    Logger.getLogger(UsersDataSet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }*/
        }
    }

    //add character
    public void doPost(HttpServletRequest req,
                       HttpServletResponse resp) throws ServletException, IOException {
//        String login = req.getParameter("login");
           /* String src = req.getParameter("src");
            String namePersonage = req.getParameter("characterName");
            System.out.println("_____________src: " + src);
            System.out.println("_____________idTag: " + namePersonage);
            String idPersonage = "";*/
//        String src = req.getParameter("src");
        /*if (login == null || src == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/
        /*String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("SC_UNAUTHORIZED");
        } else {*/
            /*Map<String, Object> pageVariables = createPageVariablesMap(req);
            String login = profile.getLogin();
            System.out.println("good");
            pageVariables.put("message", profile.getLogin());
            Date date = new Date();
            idPersonage = "" + date.getTime();
            System.out.println("idPersonage= " + idPersonage);
            accountService.addIdPersonage(login, idPersonage);*/
//            accountService.addPersonage(namePersonage, src, idPersonage);
//                accountService.addSession(sessionId, profile);
//            resp.setContentType("text/html;charset=utf-8");
        try {
            processRequest(req, resp);
        } catch (JSONException ex) {
            Logger.getLogger(UsersDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
//                accountService.addSession(req.getSession().getId(), profile);
//            Gson gson = new Gson();
//            String json = gson.toJson(profile);
//            resp.getWriter().println(login + " | " /*+ src*/ + " | " + idPersonage);
//            resp.getWriter().println(PageGenerator.instance().getPage("profile.html", pageVariables));
//            resp.sendRedirect("/personage");
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