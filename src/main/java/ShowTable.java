import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ShowTable implements HierarchicalController<HomeController> {

    public TableColumn<Show, String> hallColumn;
    public TableColumn<Show, String> movieColumn;
    public TableView<Show> showTable;
    private HomeController parentController;

    public void onDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();

            Show show = showTable.getItems().get(showTable.getSelectionModel().getFocusedIndex());

            Show showBase = ses.get(
                    Show.class, show.getId());
            ses.delete(showBase);

            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        showTable.getItems().remove(showTable.getSelectionModel().getFocusedIndex());
    }


    public void initialize() {
        for (TableColumn<Show, ?> showTableColumn : showTable.getColumns()) {
            if ("date".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            } else if ("time".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            }
        }

        hallColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getHall().getNumber()));

        movieColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getMovie().getName()));

        if (parentController == null) {
            this.parentController = new HomeController();
        }
        showTable.getItems().clear();

        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();
            Query query = ses.createQuery("from Show");
            List<Show> s = query.list();
            showTable.getItems().addAll(s);
            ses.close();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
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
