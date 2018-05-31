import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.Iterator;

public class MovieTable implements HierarchicalController<HomeController>{

    private HomeController parentController;

    public TableView<Movie> movieTable;

    public void onDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            Movie movie = ses.get(
                    Movie.class, movieTable.getItems().get(movieTable.getSelectionModel().getFocusedIndex()).getId());
            ses.delete(movie);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        movieTable.getItems().remove(movieTable.getSelectionModel().getFocusedIndex());
    }


    public void initialize() {
        for (TableColumn<Movie, ?> movieTableColumn : movieTable.getColumns()) {
            if ("id".equals(movieTableColumn.getId())) {
                movieTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            } else if ("name".equals(movieTableColumn.getId())) {
                movieTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            } else if ("description".equals(movieTableColumn.getId())) {
                movieTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            } else if ("length".equals(movieTableColumn.getId())) {
                movieTableColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
            } else if ("minAge".equals(movieTableColumn.getId())) {
                movieTableColumn.setCellValueFactory(new PropertyValueFactory<>("minAge"));
            }


            ObservableList<Movie> data = FXCollections.observableArrayList();
            SessionFactory sf = new Configuration().configure().buildSessionFactory();
            Session ses = sf.openSession();
            Query query = ses.createQuery("from Movie", Movie.class);
            Iterator ite = query.iterate();
            while (ite.hasNext()) {
                Movie obj = (Movie) ite.next();

                data.add(obj);
            }
            movieTable.setItems(data);
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
