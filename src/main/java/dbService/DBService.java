package dbService;

import dbService.dao.UsersDAO;
import dbService.dataSets.Personage;
import dbService.dataSets.UsersDataSet;
import dbService.dataSets.Profile;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "create";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getPostgresConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    @SuppressWarnings("UnusedDeclaration")
    private Configuration getPostgresConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(Profile.class);
        configuration.addAnnotatedClass(Personage.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "postgres");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(Profile.class);
        configuration.addAnnotatedClass(Personage.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }


    public UsersDataSet getUser(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet getUserN(String login) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet usersDataSet = dao.getUserId(login);
            if (usersDataSet == null) {
                return null;
            }
            long id = usersDataSet.getId();
            UsersDataSet dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addUser(String name) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            long id = dao.insertUser(name);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public UsersDataSet getUserIdSession(String sessionId) {
        Session session = sessionFactory.openSession();
        UsersDAO dao = new UsersDAO(session);
        UsersDataSet usersDataSet = dao.getUserIdSession(sessionId);
        if (usersDataSet == null) {
            return null;
        }
        session.close();
        return usersDataSet;
    }

    public String addSessionId(String sessionId, UsersDataSet user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        String login = dao.insertSession(sessionId, user);
        transaction.commit();
        session.close();
        return login;
    }

    public String deleteSessionId(UsersDataSet user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        String login = dao.insertSession("", user);
        transaction.commit();
        session.close();
        return login;
    }

    public long addPersonage(String login, String idPersonage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        dao.insertPersonage(login, idPersonage);
        long quantityPersonages = dao.getUserQuantityPersonages(login);
        transaction.commit();
        session.close();
        return quantityPersonages;
    }

    public List<Profile> getUsersPersonages(String login) {
        Session session = sessionFactory.openSession();
        UsersDAO dao = new UsersDAO(session);
        List<Profile> profiles = dao.getUsersPersonages(login);
        if (profiles == null) {
            return null;
        }
        session.close();
        return profiles;
    }

    public long addImgNamePersonage(String namePersonage, String login, String src, String idPersonage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        long id = dao.insertPersonageImgName(namePersonage, login, src, idPersonage);
        transaction.commit();
        session.close();
        return id;
    }

    public List<Personage> getAllPersonagesUser(String login) {
        Session session = sessionFactory.openSession();
        UsersDAO dao = new UsersDAO(session);
        List allPersonages = dao.getAllPersonagesUser(login);
        if (allPersonages == null) {
            return null;
        }
        session.close();
        return allPersonages;
    }

    public String deletePersonage(String login, String namePersonage) {
        Session session = sessionFactory.openSession();
        UsersDAO dao = new UsersDAO(session);
        String idPersonage = dao.deletePersonage(login, namePersonage);
        System.out.println("idPersonage+ " + idPersonage);
        if (idPersonage == null) {
            return null;
        }
        session.close();
        return idPersonage;
    }

    /*public void deleteIdPersonage(String login, String idPersonage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        dao.deleteIdPersonage(login, idPersonage);
        transaction.commit();
        session.close();
    } */

    public void deleteIdPersonage(String login, String idPersonage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        dao.deleteIdPersonage(login, idPersonage);
        transaction.commit();
        session.close();
    }

    public void updatePersonage(String login, String src, String namePersonage, String oldNameEd) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        dao.updatePersonage(login, src, namePersonage, oldNameEd);
        transaction.commit();
        session.close();
    }
}