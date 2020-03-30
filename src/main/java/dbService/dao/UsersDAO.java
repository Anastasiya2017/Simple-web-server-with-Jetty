package dbService.dao;

import dbService.dataSets.Personage;
import dbService.dataSets.Profile;
import dbService.dataSets.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UsersDAO {

    private Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet get(long id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public UsersDataSet getUserId(String name) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria.add(Restrictions.eq("login", name)).uniqueResult());
    }

    public UsersDataSet getUserIdSession(String id) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria.add(Restrictions.eq("idSession", id)).uniqueResult());
    }

    public long insertUser(String name) throws HibernateException {
        return (Long) session.save(new UsersDataSet(name));
    }

    public String insertSession(String sessionId, UsersDataSet user) {
        user.setIdSession(sessionId);
        session.update(user);
        return user.getLogin();
    }

    public void insertPersonage(String login, String idPersonage) {
        session.save(new Profile(login, idPersonage));
    }

    public long getUserQuantityPersonages(String login) {
        Criteria criteria = session.createCriteria(Profile.class);
        return criteria.add(Restrictions.eq("login", login)).list().size();
    }

    public List getUsersPersonages(String login) {
        Criteria criteria = session.createCriteria(Profile.class);
        return (criteria.add(Restrictions.eq("login", login)).list());

    }

    public long insertPersonageImgName(String namePersonage, String login, String src, String idPersonage) {
        return (Long) session.save(new Personage(namePersonage, login, src, idPersonage, "18", "30"));
    }

    public List getAllPersonagesUser(String login) {
        Criteria criteria = session.createCriteria(Personage.class);
        return criteria.add(Restrictions.eq("login", login)).list();
    }

    public String deletePersonage(String login, String namePersonage) {
        Criteria criteria = session.createCriteria(Personage.class);
        session.beginTransaction();
        Personage personage = (Personage) criteria.add(Restrictions.eq("login", login)).add(Restrictions.eq("name", namePersonage)).uniqueResult();
        String idPersonage = personage.getPersonageId();
        session.delete(personage);
        session.getTransaction().commit();
        return idPersonage;
    }

    public void deleteIdPersonage(String login, String idPersonage) {
        Criteria criteria = session.createCriteria(Profile.class);
        Profile profile = (Profile) criteria.add(Restrictions.eq("login", login)).add(Restrictions.eq("personageId", idPersonage)).uniqueResult();
        session.delete(profile);
    }

    public void updatePersonage(String login, String src, String namePersonage, String oldNameEd) {
        Criteria criteria = session.createCriteria(Personage.class);
        Personage personage = (Personage) criteria.add(Restrictions.eq("login", login)).add(Restrictions.eq("name", oldNameEd)).uniqueResult();
        personage.setName(namePersonage);
        personage.setImg(src);
        session.update(personage);
    }

    public void selectPersonage(String login, String namePersonage) {
        Criteria criteria = session.createCriteria(Personage.class);
        Personage personage = (Personage) criteria.add(Restrictions.eq("login", login)).add(Restrictions.eq("name", namePersonage)).uniqueResult();
        personage.setStatus(true);
        session.update(personage);
    }

    public Personage getMyPersonagesInGame(String login) {
        Criteria criteria = session.createCriteria(Personage.class);
        Personage personage = (Personage) criteria.add(Restrictions.eq("login", login)).add(Restrictions.eq("status", true)).uniqueResult();
        return personage;
    }

    public void updateCoordinatesPersonage(String login, String left, String top) {
        Criteria criteria = session.createCriteria(Personage.class);
        Personage personage = (Personage) criteria.add(Restrictions.eq("login", login)).add(Restrictions.eq("status", true)).uniqueResult();
        personage.setX(left);
        personage.setY(top);
        personage.setStatus(false);
        session.update(personage);
    }
}