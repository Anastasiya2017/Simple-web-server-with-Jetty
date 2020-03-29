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

    public void addIdPersonage(String login, String idPersonage) {
        long quantityPersonages = dbService.addPersonage(login, idPersonage);
        System.out.println(" user quantityPersonages: " + quantityPersonages + " " + idPersonage);
    }

    public List<Profile> getAllPersonages(String login) {
        return dbService.getUsersPersonages(login);
    }

    public void addPersonage(String namePersonage, String login, String src, String idPersonage) {
        long id = dbService.addImgNamePersonage(namePersonage, login, src, idPersonage);
        System.out.println(" addPersonage: " + id + " " + idPersonage + " " + namePersonage);
    }

    public List<Personage> getPersonages(String login) {
        List<Personage> allPersonages = dbService.getAllPersonagesUser(login);
        return allPersonages;
    }

    public String deletePersonage(String login, String namePersonage) {
        String idPersonage = dbService.deletePersonage(login, namePersonage);
        System.out.println("ASidPersonage: " + idPersonage);
        return idPersonage;
    }

    public void deleteIdPersonage(String login, String idPersonage) {
        dbService.deleteIdPersonage(login, idPersonage);
    }

    public void updatePersonage(String login, String src, String namePersonage, String oldNameEd) {
        dbService.updatePersonage(login, src, namePersonage, oldNameEd);
    }
}
