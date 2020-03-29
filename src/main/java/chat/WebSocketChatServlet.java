package chat;

import accounts.AccountService;
import dbService.dataSets.Personage;
import dbService.dataSets.UsersDataSet;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.JSONException;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "WebSocketChatServlet", urlPatterns = {"/game"})

public class WebSocketChatServlet extends WebSocketServlet {
    private final AccountService accountService;
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final ChatService chatService;

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
                if (inform.equals("coordinates")) {
                    String left = req.getParameter("left");
                    String top = req.getParameter("top");
                    Map<String, Object> pageVariables = createPageVariablesMap(req);
                    String login = profile.getLogin();
                    String[] coordinates = new String[2];
                    coordinates[0] = "189.5px";
                    coordinates[1] = "198px";
//                    String coordinat = "style=\"position: absolute; left: 187px; top: 190px;\"";
                    pageVariables.put("left", coordinates[0]);
                    pageVariables.put("top", coordinates[1]);
                    pageVariables.put("message", profile.getLogin());
                    System.out.println("selectPersonage!!" + left + " : " + top);
                    accountService.updateCoordinatesPersonage(login, left, top);
                    System.out.println("успех!!");
//                    resp.sendRedirect("/game");
                }
            }
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        if (profile == null) {
            System.out.println("null: ");
            pageVariables.put("message", "null");
            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            System.out.println("login: " + profile.getLogin());
            resp.getWriter().println("_");
            String login = profile.getLogin();
            pageVariables.put("message", profile.getLogin());

            Personage myGamePersonage = accountService.getMyPersonagesInGame(login);
            String img = "ds";
            if (myGamePersonage != null) {
                System.out.println("myGamePersonage.getImg(: " + myGamePersonage.getImg());
                img = myGamePersonage.getImg();
                String[] coordinates = new String[2];
                coordinates[0] = myGamePersonage.getX();
                coordinates[1] = myGamePersonage.getY();
                String coordinat = "style=\"position: absolute; left: " + coordinates[0] + "px; top: " + coordinates[1] + "px;\"";
                pageVariables.put("coordinat", coordinat);
            }

            System.out.println("img: " + img);
            pageVariables.put("gamePersonage", "\"" + img + "\"");
            resp.getWriter().println(PageGenerator.instance().getPage("game.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* resp.setContentType("text/html;charset=utf-8");
        String sessionId = req.getSession().getId();
        UsersDataSet profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Unauthorized");
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("_");
            Map<String, Object> pageVariables = createPageVariablesMap(req);
            pageVariables.put("message", profile.getLogin());
            resp.getWriter().println(PageGenerator.instance().getPage("game.html", pageVariables));
//            resp.sendRedirect("/game");
        }*/
        try {
            processRequest(req, resp);
        } catch (JSONException ex) {
            Logger.getLogger(UsersDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    public WebSocketChatServlet(AccountService accountService) {
        this.chatService = new ChatService();
        this.accountService = accountService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((req, resp) -> new ChatWebSocket(chatService));
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