import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class DataContainer {

    private final SessionFactory sessionFactory;

    DataContainer() {
        Configuration config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
    }

    SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}