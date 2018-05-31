import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

    public void initialize() {
        Image img = new Image("cinema.jpg");
        ImageView imgView = new ImageView(img);
        pane.getChildren().add(imgView);
    }


    public void movieTable(ActionEvent actionEvent) {
        loadIntoPane("movieTable.fxml");
    }

    public void showCreator(ActionEvent actionEvent) {
        loadIntoPane("showCreator.fxml");
    }

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
    public void setParentController(HomeController parentController) {
    }

}
