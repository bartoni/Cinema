import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class HallTable implements HierarchicalController<HomeController>{

    private HomeController parentController;

    public TextField number;
    public TextField seats;
    public TextField type;
    public TableView<Hall> hallTable;

    public void add(ActionEvent actionEvent) {
        Hall h = new Hall();
        h.setNumber(number.getText());
        h.setSeats(seats.getText());
        h.setType(type.getText());
        addBase(h);
        hallTable.getItems().add(h);
    }

    public void addBase(Hall hall) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(hall);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }


    public void initialize() {
        for (TableColumn<Hall, ?> hallTableColumn : hallTable.getColumns()) {
            if ("id".equals(hallTableColumn.getId())) {
                hallTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            } else if ("number".equals(hallTableColumn.getId())) {
                hallTableColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
            } else if ("seats".equals(hallTableColumn.getId())) {
                hallTableColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
            } else if ("type".equals(hallTableColumn.getId())) {
                hallTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            }
        }
    }

    @Override
    public HomeController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(HomeController parentController) {
        this.parentController = parentController;
        hallTable.setItems(parentController.getDataContainer().getHalls());
    }
}
