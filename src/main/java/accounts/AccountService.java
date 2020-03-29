package accounts;

import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.Personage;
import dbService.dataSets.UsersDataSet;
import dbService.dataSets.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {
    private final Map<String, UsersDataSet> sessionIdToProfile;

    DBService dbService = new DBService();

    public AccountService() {
        sessionIdToProfile = new HashMap<>();
    }

    public void addNewUser(String login) throws DBException {
        long userId = dbService.addUser(login);
    }

    public UsersDataSet getUserByLogin(String login) throws DBException {
        dbService.printConnectInfo();
        return dbService.getUserN(login);
    }

    public UsersDataSet getUserBySessionId(String sessionId) {
        return dbService.getUserIdSession(sessionId);
    }

    public void addSession(String sessionId, UsersDataSet userProfile) {
        String login = dbService.addSessionId(sessionId, userProfile);
    }

    public String deleteSession(UsersDataSet user) {
        String login = dbService.deleteSessionId(user);
        return login;
    }

    public void addIdPersonage(String login, String idPersonage) {
        long quantityPersonages = dbService.addPersonage(login, idPersonage);
    }

    public List<Profile> getAllPersonages(String login) {
        return dbService.getUsersPersonages(login);
    }

    public void addPersonage(String namePersonage, String login, String src, String idPersonage) {
        dbService.addImgNamePersonage(namePersonage, login, src, idPersonage);
    }

    public List<Personage> getPersonages(String login) {
        List<Personage> allPersonages = dbService.getAllPersonagesUser(login);
        return allPersonages;
    }

    public String deletePersonage(String login, String namePersonage) {
        String idPersonage = dbService.deletePersonage(login, namePersonage);
        return idPersonage;
    }

    public void deleteIdPersonage(String login, String idPersonage) {
        dbService.deleteIdPersonage(login, idPersonage);
    }

    public void updatePersonage(String login, String src, String namePersonage, String oldNameEd) {
        dbService.updatePersonage(login, src, namePersonage, oldNameEd);
    }

    public void selectPersonage(String login, String namePersonage) {
        dbService.selectPersonage(login, namePersonage);
    }

    public Personage getMyPersonagesInGame(String login) {
        return dbService.getMyPersonagesInGame(login);
    }
}