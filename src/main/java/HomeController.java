import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController implements HierarchicalController<HomeController>{

    public BorderPane borderPane;
    private DataContainer dataContainer;
    private HomeController parentController;

    private Stage myStage;

    void setStage(Stage myStage) {
        this.myStage = myStage;
    }

    DataContainer getDataContainer() {
        return dataContainer;
    }

    public HomeController() {
        dataContainer = new DataContainer();
    }

    @Override
    public HomeController getParentController() {
        return this;
    }

    @Override
    public void setParentController(HomeController parentController) {
        this.parentController = parentController;
    }

    public void showUserView(ActionEvent actionEvent) {
        loadWindow("movieSearch.fxml", false);
    }

    public void showAdminView(ActionEvent actionEvent) {
        loadWindow("movieSearch.fxml", true);
    }

    public void loadWindow(String fxml, boolean showAdminFun) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            Parent root = loader.load();
            HierarchicalController<HomeController> dataController = loader.getController();
            dataController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Cinema");
            stage.setScene(new Scene(root, 1080, 720));
            stage.show();

            MovieSearch directController = loader.getController();

            directController.deleteButton.setVisible(showAdminFun);
            directController.addButton.setVisible(showAdminFun);

            myStage.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
