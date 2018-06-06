import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class MovieSearch implements HierarchicalController<HomeController> {

    public BorderPane borderPane;
    public ListView<String> movieList;
    public DatePicker datePicker;
    public TableView<Show> repertoireTable;
    public TextArea description;
    public Label ageCategory;
    public Label length;
    public Label repertoireLabel;
    public Button deleteButton;
    public Button addButton;

    private HomeController parentController;

    public void onDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            Query query = ses.createQuery("from Movie movie where movie.name = :name");
            String name = movieList.getSelectionModel().getSelectedItem();
            query.setParameter("name", name);
            List m = query.list();
            ses.delete(m.get(0));
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        movieList.getItems().remove(movieList.getSelectionModel().getSelectedItem());
    }

    public void onAdd(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("showCreator.fxml"));
        try {
            final BorderPane load = loader.load();
            borderPane.getChildren().clear();
            borderPane.getChildren().add(load);
            HierarchicalController<HomeController> dataController = loader.getController();
            dataController.setParentController(parentController);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void initialize() {
        for (javafx.scene.control.TableColumn<Show, ?> showTableColumn : repertoireTable.getColumns()) {
            if ("time".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            } else if ("hall".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("hall"));
            }
        }

        ObservableList<String> movieNames = FXCollections.observableArrayList();
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session ses = sf.openSession();
        Query<Movie> query = ses.createQuery("from Movie", Movie.class);
        Iterator<Movie> ite = query.iterate();
        while (ite.hasNext()) {
            Movie obj = ite.next();
            movieNames.add(obj.getName());
        }
        movieList.setItems(movieNames);
    }

    public void onClick(MouseEvent mouseEvent) {
        if (movieList.getSelectionModel().getSelectedItem() != null) {
            description.setVisible(true);
            length.setVisible(true);
            ageCategory.setVisible(true);
            repertoireLabel.setVisible(true);
            datePicker.setVisible(true);
            try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
                ses.beginTransaction();
                Query query = ses.createQuery("from Movie movie where movie.name = :name");
                String name = movieList.getSelectionModel().getSelectedItem();
                query.setParameter("name", name);
                List m = query.list();
                Movie mov = (Movie) m.get(0);
                description.setText(mov.getDescription());
                ageCategory.setText("Kategoria wieku: " + mov.getMinAge());
                length.setText("Długośc filmu: " + mov.getLength() + " min.");
                ses.getTransaction().commit();
            } catch (HibernateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
        } else {
        }
    }

    public void onChosenDay(ActionEvent actionEvent) {
        if (datePicker.getValue() != null) {
            repertoireTable.setVisible(true);
            repertoireTable.getItems().clear();
            try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
                ses.beginTransaction();
                Query query = ses.createQuery("from Show show where show.date = :date");
                LocalDate date = datePicker.getValue();
                query.setParameter("date", date);
                List<Show> s = query.list();

                //ok - udaje mi się zebrać listę seansów, które zapisalem na dany dzien, ale nie wiem jak sie dostac do
                //do sali, do ktorej z tabelki SHOW powinienem sie generalnie moc dostac (a jak probuje to chamsko
                //wyprintowac do konsoli, to dostaje adres obiektu typu @Hall#13878463.. .

                repertoireTable.getItems().addAll(s);
                ses.getTransaction().commit();
            } catch (HibernateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
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
    }


}
