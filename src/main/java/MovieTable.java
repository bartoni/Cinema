import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class MovieTable implements HierarchicalController<HomeController>{

    private HomeController parentController;

    public void save(ActionEvent actionEvent) {
    }

    public void load(ActionEvent actionEvent) {
    }

    public TextField name;
    public TextField description;
    public TextField length;
    public TextField minAge;
    public Pane poster;
    public TableView<Movie> movieTable;

    public void add(ActionEvent actionEvent) {
        Movie m = new Movie();
        m.setName(name.getText());
        m.setDescription(description.getText());
        m.setLength(length.getText());
        m.setMinAge(minAge.getText());
        addBase(m);
        movieTable.getItems().add(m);
    }

    public void addBase(Movie movie) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(movie);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
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
        }
    }

    @Override
    public HomeController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(HomeController parent) {
        this.parentController = parentController;

    }
}
