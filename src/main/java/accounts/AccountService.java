package accounts;

import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Map<String, UsersDataSet> sessionIdToProfile;

    DBService dbService = new DBService();

    public AccountService() {
        sessionIdToProfile = new HashMap<>();
    }

    public void addNewUser(String login) throws DBException {
        long userId = dbService.addUser(login);
        System.out.println("Added user id: " + userId + " " + login);
    }

    public UsersDataSet getUserByLogin(String login) throws DBException {
        dbService.printConnectInfo();
        return dbService.getUserN(login);
    }

    public UsersDataSet getUserBySessionId(String sessionId) {
        /*for(Map.Entry<String, UsersDataSet> e : sessionIdToProfile.entrySet()) {
            System.out.println(e.getKey());
            System.out.println(e.getValue());
        }*/
//        return sessionIdToProfile.get(sesdataSetsionId);
        return dbService.getUserIdSession(sessionId);
    }

    public void addSession(String sessionId, UsersDataSet userProfile) {
        String login = dbService.addSessionId(sessionId, userProfile);
        System.out.println("Added user id: " + sessionId + " " + login);
//        sessionIdToProfile.put(sessionId, userProfile);
    }

    public String deleteSession(UsersDataSet user) {
        String login = dbService.deleteSessionId(user);
        System.out.println("Deleted user id: " + user + " " + login);
        return login;
    }
}
