import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.HibernateException;
import org.hibernate.Session;
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
    public Button deleteMovieButton;
    public Button addButton;
    public TableColumn timeCol;
    public TableColumn reservationCol;
    public Button deleteShowButton;
    public Button showHalls;
    public Button showShows;
    private boolean isAdmin;

    private HomeController parentController;

    public void initData(boolean isAdmin) {
        showHalls.setVisible(isAdmin);
        showShows.setVisible(isAdmin);
        deleteMovieButton.setVisible(isAdmin);
        addButton.setVisible(isAdmin);
    }

    public void onMovieDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();
            String name = movieList.getSelectionModel().getSelectedItem();

            Query query = ses.createQuery("from Movie movie where movie.name = :name");
            query.setParameter("name", name);
            List m = query.list();

            //wyciagam z bazy wszystkie seanse z tym filmem

            Query showQuery = ses.createQuery("from Show show where show.movie = :movie");
            showQuery.setParameter("movie", m.get(0));
            Iterator ite = showQuery.iterate();
            while (ite.hasNext()) {
                Show obj = (Show) ite.next();
                ses.delete(obj);
            }

            //dany obiekt filmu trzeba usunac dopiero po usunieciu powiazanych z nim seansow

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

        //to cale drzewko pod spodem ma na celu stworzenie przycisku 'rezerwuje!' w tabeli

        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        reservationCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        Callback<TableColumn<Show, String>, TableCell<Show, String>> cellFactory
                = new Callback<TableColumn<Show, String>, TableCell<Show, String>>() {
            @Override
            public TableCell call(final TableColumn<Show, String> param) {
                final TableCell<Show, String> cell = new TableCell<Show, String>() {
                    final Button btn = new Button("Rezerwuję!");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(btn);
                            setText(null);
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    //klikniecie przycisku powoduje zaznaczenie wiersza, dzieki temu mam obiekt
                                    repertoireTable.getSelectionModel().select(getTableRow().getIndex());
                                    Show show = repertoireTable.getSelectionModel().getSelectedItem();
                                    addReservationWindow(actionEvent, show);
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        };
        reservationCol.setCellFactory(cellFactory);

        //upewniam sie, czy kontroler jest zainicjonowany

        if (parentController == null) {
            this.parentController = new HomeController();
        }

        //uniemozliwiam wybor dni z przeszlosci

        datePicker.setShowWeekNumbers(false);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        //wypelniam liste filmow

        ObservableList<String> movieNames = FXCollections.observableArrayList();
        Session ses = parentController.getSessionFactory().openSession();
        Query<Movie> query = ses.createQuery("from Movie", Movie.class);
        Iterator<Movie> ite = query.iterate();
        while (ite.hasNext()) {
            Movie obj = ite.next();
            movieNames.add(obj.getName());
        }
        movieList.setItems(movieNames);
        ses.close();
    }

    //metoda obsluguje wybor filmu z tej horyzontalnej listy na gorze

    public void onClick(MouseEvent mouseEvent) {
        //za kazdym razem jak klikne w jakis film to uaktualnie repertuar wyswietlany w tabelce - b fajne
        onChosenDay(new ActionEvent());
        if (movieList.getSelectionModel().getSelectedItem() != null) {
            //stopniowo odslaniam elementy interfejsu
            deleteMovieButton.setDisable(false);
            description.setVisible(true);
            length.setVisible(true);
            ageCategory.setVisible(true);
            repertoireLabel.setVisible(true);
            datePicker.setVisible(true);
            try (Session ses = parentController.getSessionFactory().openSession()) {
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

    //metoda do obslugi wyboru repertuaru na dany dzien dla danego filmu

    public void onChosenDay(ActionEvent actionEvent) {
        if (datePicker.getValue() != null) {
            deleteShowButton.setVisible(isAdmin);
            repertoireTable.setVisible(true);
            repertoireTable.getItems().clear();
            try (Session ses = parentController.getSessionFactory().openSession()) {
                ses.beginTransaction();
                Query whereQuery = ses.createQuery("from Show show where show.date = :date and show.movie = :movie");
                LocalDate date = datePicker.getValue();

                Query movieQuery = ses.createQuery("from Movie movie where movie.name = :name");
                String name = movieList.getSelectionModel().getSelectedItem();
                movieQuery.setParameter("name", name);
                List m = movieQuery.list();
                Movie movie = (Movie) m.get(0);

                whereQuery.setParameter("date", date);
                whereQuery.setParameter("movie", movie);

                List<Show> s = whereQuery.list();

                repertoireTable.getItems().addAll(s);
                ses.getTransaction().commit();
            } catch (HibernateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }

        }
    }

    public void addReservationWindow(ActionEvent actionEvent, Show show) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reservationWindow.fxml"));
        try {
            BorderPane borderPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("ReservationWindow");
            stage.setScene(new Scene(borderPane, 540, 360));
            stage.show();

            ReservationWindow controller = loader.getController();

            String name = movieList.getSelectionModel().getSelectedItem();
            String date = datePicker.getValue().toString();
            String time = show.getTime();
            String number = show.getHall().getNumber();
            String type = show.getHall().getType();
            String seats = show.getHall().getSeats();

            //przekazuje dalej niezbedne do wydrukowania rezerwacji informacje

            controller.initData(name, date, time, number, type, seats, show);

            controller.setParentController(parentController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHalls(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hallTable.fxml"));
        try {
            BorderPane borderPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("HallTable");
            stage.setScene(new Scene(borderPane, 540, 360));
            stage.show();

            HierarchicalController<HomeController> controller = loader.getController();
            controller.setParentController(parentController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //kazdy seans moge sobie usunac bez wzgledu na jego powiazanie z sala czy filmem, bo jest rodzicem w tej relacji

    public void onShowDelete(ActionEvent actionEvent) {
        try (Session ses = parentController.getSessionFactory().openSession()) {
            ses.beginTransaction();
            Show show = repertoireTable.getSelectionModel().getSelectedItem();
            Query showQuery = ses.createQuery("from Show show where show.id = :id");
            Integer id = show.getId();
            showQuery.setParameter("id", id);
            List s = showQuery.list();
            ses.delete(s.get(0));
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        repertoireTable.getItems().remove(repertoireTable.getSelectionModel().getSelectedItem());
    }

    public void showShows(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("showTable.fxml"));
        try {
            BorderPane borderPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("ShowTable");
            stage.setScene(new Scene(borderPane, 540, 360));
            stage.show();

            HierarchicalController<HomeController> controller = loader.getController();
            controller.setParentController(parentController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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
