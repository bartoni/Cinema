import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MovieSearch implements HierarchicalController<HomeController> {

    public BorderPane borderPane;
    public ListView<String> movieList;
    public DatePicker datePicker;
    public TableView<Show> repertoireTable;
    public TextArea description;
    public Label ageCategory;
    public Label length;
    public Label repertoireLabel;
    public Button deleteButton;
    public Button addButton;

    private HomeController parentController;

    public void onDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            Query query = ses.createQuery("from Movie movie where movie.name = :name");
            String name = movieList.getSelectionModel().getSelectedItem();
            query.setParameter("name", name);
            List m = query.list();
            ses.delete(m.get(0));
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        movieList.getItems().remove(movieList.getSelectionModel().getSelectedItem());
    }

    public void onAdd(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("showCreator.fxml"));
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


    public void initialize() {
        for (javafx.scene.control.TableColumn<Show, ?> showTableColumn : repertoireTable.getColumns()) {
            if ("id".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            } else if ("date".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            } else if ("time".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            }
        }

        ObservableList<String> movieNames = FXCollections.observableArrayList();
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session ses = sf.openSession();
        Query<Movie> query = ses.createQuery("from Movie", Movie.class);
        Iterator<Movie> ite = query.iterate();
        while (ite.hasNext()) {
            Movie obj = ite.next();
            movieNames.add(obj.getName());
        }
        movieList.setItems(movieNames);
    }

    public void onClick(MouseEvent mouseEvent) {
        if (movieList.getSelectionModel().getSelectedItem() != null) {
            description.setVisible(true);
            length.setVisible(true);
            ageCategory.setVisible(true);
            repertoireLabel.setVisible(true);
            datePicker.setVisible(true);
            try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
                ses.beginTransaction();
                Query query = ses.createQuery("from Movie movie where movie.name = :name");
                String name = movieList.getSelectionModel().getSelectedItem();
                query.setParameter("name", name);
                List m = query.list();
                Movie mov = (Movie) m.get(0);
                description.setText(mov.getDescription());
                ageCategory.setText("Kategoria wieku: " + mov.getMinAge());
                length.setText("Długośc filmu: " + mov.getLength() + " min.");
                ses.getTransaction().commit();
            } catch (HibernateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
        } else {
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
