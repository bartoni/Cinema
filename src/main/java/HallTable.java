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

import java.util.Iterator;
import java.util.List;

public class HallTable implements HierarchicalController<HomeController>{

    private HomeController parentController;

    public TableView<Hall> hallTable;

    public void onDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();

            Hall hall = hallTable.getItems().get(hallTable.getSelectionModel().getFocusedIndex());

            Query showQuery = ses.createQuery("from Show show where show.hall = :hall");
            showQuery.setParameter("hall", hall);

            Iterator ite = showQuery.iterate();
            while (ite.hasNext()) {
                Show obj = (Show) ite.next();
                ses.delete(obj);
            }

            Hall hallBase = ses.get(
                    Hall.class, hall.getId());
            ses.delete(hallBase);

            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        hallTable.getItems().remove(hallTable.getSelectionModel().getFocusedIndex());
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
        if (parentController == null) {
            this.parentController = new HomeController();
        }
        hallTable.getItems().clear();

        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();
            Query query = ses.createQuery("from Hall");
            List<Hall> h = query.list();
            hallTable.getItems().addAll(h);
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
