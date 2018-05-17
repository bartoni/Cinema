import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowTable implements HierarchicalController<HomeController> {

    public TextField hourField;
    public TextField minuteField;
    private HomeController parentController;
    public ComboBox<String> moviePicker;
    public ComboBox<String> hallPicker;
    public DatePicker datePicker;
    public TableView<Show> showTable;

    public void initialize() {
        for (TableColumn<Show, ?> showTableColumn : showTable.getColumns()) {
            if ("id".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            } else if ("name".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            } else if ("hall".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("hall"));
            } else if ("date".equals(showTableColumn.getId())) {
                showTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            }
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            Show show = ses.get(
                    Show.class, showTable.getItems().get(showTable.getSelectionModel().getFocusedIndex()).getId());
            ses.delete(show);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        showTable.getItems().remove(showTable.getSelectionModel().getFocusedIndex());
    }

    public void add(ActionEvent actionEvent) {
        Show s = new Show();
        s.setName(moviePicker.getValue());
        s.setHall(hallPicker.getValue());
        LocalDate ld = datePicker.getValue();
        LocalTime lt = LocalTime.of(Integer.parseInt(hourField.getText()),
                Integer.parseInt(minuteField.getText()));
        ZoneId z = ZoneId.of( "Europe/Warsaw" ) ;
        ZonedDateTime zdt = ZonedDateTime.of(ld, lt, z);
        s.setDate(zdt);
        //addBase(s);
        showTable.getItems().add(s);
    }

    @Override
    public HomeController getParentController() { return parentController; }

    @Override
    public void setParentController(HomeController parentController) {
        this.parentController = parentController;
        showTable.setItems(parentController.getDataContainer().getShows());

    }

    public void fillNames(javafx.scene.input.MouseEvent mouseEvent) {
        moviePicker.valueProperty().set(null);
        List<String> movieNames = new ArrayList<>();
        for (Movie movie : parentController.getDataContainer().getMovies()) {
            movieNames.add(movie.getName()); }
        ObservableList obMovieNames = FXCollections.observableList(movieNames);
        moviePicker.getItems().addAll(obMovieNames);
    }

    public void fillHalls(javafx.scene.input.MouseEvent mouseEvent) {
        hallPicker.valueProperty().set(null);
        List<String> hallNames = new ArrayList<>();
        for (Hall hall : parentController.getDataContainer().getHalls()) {
            hallNames.add(hall.getNumber()); }
        ObservableList obHallNames = FXCollections.observableList(hallNames);
        hallPicker.getItems().addAll(obHallNames);
    }
}
