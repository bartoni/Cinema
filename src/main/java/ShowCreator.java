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
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.Iterator;

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
    public Button addShow;
    public BorderPane borderPane;
    private HomeController parentController;

    public void fillMovies(javafx.scene.input.MouseEvent mouseEvent) {
        moviePicker.getItems().clear();
        ObservableList<String> movies = FXCollections.observableArrayList();
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session ses = sf.openSession();
        Query query = ses.createQuery("from Movie", Movie.class);
        Iterator ite = query.iterate();
        while (ite.hasNext()) {
            Movie obj = (Movie) ite.next();

            movies.add(obj.getName());
        }
        moviePicker.getItems().addAll(movies);
    }

    public void fillHalls(javafx.scene.input.MouseEvent mouseEvent) {
        hallPicker.getItems().clear();
        ObservableList<String> halls = FXCollections.observableArrayList();
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session ses = sf.openSession();
        Query query = ses.createQuery("from Hall", Hall.class);
        Iterator ite = query.iterate();
        while (ite.hasNext()) {
            Hall obj = (Hall) ite.next();

            halls.add(obj.getNumber());
        }
        hallPicker.getItems().addAll(halls);
    }

    public void addMovie(ActionEvent actionEvent) {
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

    public void add(ActionEvent actionEvent) {
        Movie m = new Movie();
        m.setName(name.getText());
        m.setDescription(description.getText());
        m.setLength(length.getText());
        RadioButton selRadioButton = (RadioButton) ageCategory.getSelectedToggle();
        String value = selRadioButton.getText();
        m.setMinAge(value);
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
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

    public void onBack(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("movieSearch.fxml"));
        try {
            final BorderPane load = loader.load();
            borderPane.getChildren().clear();
            borderPane.getChildren().add(load);
            HierarchicalController<HomeController> dataController = loader.getController();
            dataController.setParentController(parentController);
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
