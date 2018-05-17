import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.io.IOException;

public class HomeController implements HierarchicalController<HomeController>{

    public Pane pane;
    public GridPane buttons;

    private DataContainer dataContainer;

    DataContainer getDataContainer() {
        return dataContainer;
    }

    public HomeController() {
        dataContainer = new DataContainer();
    }

    public void movieTable(ActionEvent actionEvent) {
        loadIntoPane("movieTable.fxml");
    }

    public void hallTable(ActionEvent actionEvent) {
        loadIntoPane("hallTable.fxml");
    }

    public void showTable(ActionEvent actionEvent) { loadIntoPane("showTable.fxml");}

    private void loadIntoPane(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            final BorderPane load = loader.load();
            pane.getChildren().clear();
            pane.getChildren().add(load);
            HierarchicalController<HomeController> dataController = loader.getController();
            dataController.setParentController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public HomeController getParentController() {
        return this;
    }

    @Override
    public void setParentController(HomeController parent) {

    }

}
