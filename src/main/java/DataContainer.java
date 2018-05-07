import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class DataContainer {

    private final Configuration config;
    private final SessionFactory sessionFactory;
    protected ObservableList<Movie> movies;
    protected ObservableList<Hall> halls;
    protected ObservableList<Show> shows;

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public ObservableList<Hall> getHalls() {
        return halls;
    }

    public ObservableList<Show> getShows() { return shows; }

    public DataContainer() {
        movies = FXCollections.observableArrayList();
        halls = FXCollections.observableArrayList();
        shows = FXCollections.observableArrayList();
        config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        loadMoviesFromDatabase();
        loadHallsFromDatabase();
        loadShowsFromDatabase();
    }

    private void loadMoviesFromDatabase() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Movie> query = ses.createQuery("from Movie", Movie.class);
            movies.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void loadHallsFromDatabase() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Hall> query = ses.createQuery("from Hall", Hall.class);
            halls.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void loadShowsFromDatabase() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Show> query = ses.createQuery("from Show", Show.class);
            shows.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}