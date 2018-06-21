import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;

public class HomeController implements HierarchicalController<HomeController>{

    public BorderPane borderPane;
    private HomeController parentController;
    private Stage myStage;
    private final SessionFactory sessionFactory;

    public HomeController() {
        Configuration config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
    }

    SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setStage(Stage myStage) {
        this.myStage = myStage;
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
            dataController.setParentController(parentController);

            Stage stage = new Stage();
            stage.setTitle("Cinema");
            stage.setScene(new Scene(root, 1080, 720));
            stage.show();

            MovieSearch directController = loader.getController();

            directController.initData(showAdminFun);

            directController.setAdmin(showAdminFun);
            dataController.setParentController(this);

            myStage.close();
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
        this.parentController = parentController;
    }
}
