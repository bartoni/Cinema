import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class ShowTable implements HierarchicalController<HomeController>{

    public ComboBox moviePicker;
    public ComboBox hallPicker;
    public DatePicker datePicker;
    public TextField timeField;
    public TableView<Show> showTable;

    private HomeController parentController;

    public ShowTable() {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void onDelete(ActionEvent actionEvent) {
    }

    public void add(ActionEvent actionEvent) {
    }

    @Override
    public HomeController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(HomeController parent) {
        this.parentController = parentController;
        showTable.setItems(parentController.getDataContainer().getShows());

    }
}
