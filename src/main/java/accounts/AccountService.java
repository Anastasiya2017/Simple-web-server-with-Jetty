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
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UsersDataSet userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
