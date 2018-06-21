import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ShowCreator implements HierarchicalController<HomeController> {

    public ComboBox<String> moviePicker;
    public ComboBox<String> hallPicker;
    public TextField name;
    public ToggleGroup ageCategory;
    public TextArea description;
    public TextField length;
    public DatePicker datePicker;
    public TextField hourField;
    public TextField minuteField;
    public BorderPane borderPane;
    public TextField number;
    public ToggleGroup hallType;
    public TextField seats;
    private HomeController parentController;

    public void fillMovies(javafx.scene.input.MouseEvent mouseEvent) {
        moviePicker.getItems().clear();
        ObservableList<String> movies = FXCollections.observableArrayList();
        Session ses = parentController.getSessionFactory().openSession();
        Query query = ses.createQuery("from Movie", Movie.class);
        Iterator ite = query.iterate();
        while (ite.hasNext()) {
            Movie obj = (Movie) ite.next();

            movies.add(obj.getName());
        }
        moviePicker.getItems().addAll(movies);
        ses.close();
    }

    public void fillHalls(javafx.scene.input.MouseEvent mouseEvent) {
        hallPicker.getItems().clear();
        ObservableList<String> halls = FXCollections.observableArrayList();
        Session ses = parentController.getSessionFactory().openSession();
        Query query = ses.createQuery("from Hall", Hall.class);
        Iterator ite = query.iterate();
        while (ite.hasNext()) {
            Hall obj = (Hall) ite.next();

            halls.add(obj.getNumber());
        }
        hallPicker.getItems().addAll(halls);
        ses.close();
    }

    public void addMovieWindow(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addMovieWindow.fxml"));
        try {
            Parent root = loader.load();
            ShowCreator dataController = loader.getController();
            dataController.setParentController(parentController);
            Stage stage = new Stage();
            stage.setTitle("AddMovieWindow");
            stage.setScene(new Scene(root, 540, 360));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addHallWindow(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addHallWindow.fxml"));
        try {
            Parent root = loader.load();
            ShowCreator dataController = loader.getController();
            dataController.setParentController(parentController);
            Stage stage = new Stage();
            stage.setTitle("AddHallWindow");
            stage.setScene(new Scene(root, 540, 360));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShow(ActionEvent actionEvent) {
        Show s = new Show();
        s.setDate(datePicker.getValue());
        s.setTime(hourField.getText() + ":" + minuteField.getText());
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();

            Query hallQuery = ses.createQuery("from Hall hall where hall.number = :number");
            String number = hallPicker.getValue();
            hallQuery.setParameter("number", number);
            List hList = hallQuery.list();
            Hall hall = (Hall) hList.get(0);
            s.setHall(hall);

            Query movieQuery = ses.createQuery("from Movie movie where movie.name = :name");
            String name = moviePicker.getValue();
            movieQuery.setParameter("name", name);
            List mList = movieQuery.list();
            Movie movie = (Movie) mList.get(0);
            s.setMovie(movie);

            ses.save(s);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void addMovie(ActionEvent actionEvent) {
        Movie m = new Movie();
        m.setName(name.getText());
        m.setDescription(description.getText());
        m.setLength(length.getText());
        RadioButton selRadioButton = (RadioButton) ageCategory.getSelectedToggle();
        String value = selRadioButton.getText();
        m.setMinAge(value);
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(m);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void addHall(ActionEvent actionEvent) {
        Hall h = new Hall();
        h.setNumber(number.getText());
        RadioButton selRadioButton = (RadioButton) hallType.getSelectedToggle();
        String value = selRadioButton.getText();
        h.setType(value);
        h.setSeats(seats.getText());
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(h);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void onBack(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("movieSearch.fxml"));
        try {
            final BorderPane load = loader.load();
            borderPane.getChildren().clear();
            borderPane.getChildren().add(load);
            HierarchicalController<HomeController> dataController = loader.getController();
            dataController.setParentController(parentController);
            MovieSearch directController = loader.getController();
            directController.initData(true);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public HomeController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(HomeController parentController) {
        this.parentController = parentController;
    }

}
