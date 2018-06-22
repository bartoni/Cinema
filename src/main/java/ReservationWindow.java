import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class ReservationWindow implements HierarchicalController<HomeController> {

    @FXML
    public Label numberLabel;
    @FXML
    public Label typeLabel;
    @FXML
    public Label seatsLabel;
    public TextField emailField;
    public TextField seatsField;
    public Button reservationButton;
    @FXML
    private Label movieLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    private HomeController parentController;
    private Show myShow;

    public void initData(String name, String date, String time, String number, String type, String seats, Show show) {
        movieLabel.setText("Film: " + name);
        dateLabel.setText("Data seansu: " + date);
        timeLabel.setText("Godzina seansu: " + time);
        numberLabel.setText("Numer sali: " + number);
        typeLabel.setText("Typ sali: " + type);
        seatsLabel.setText("Wolne miejsca: " + seats);
        this.myShow = show;
    }

    public void makeReservation(ActionEvent actionEvent) throws IOException {
        //zabezpieczenie przed brakiem adresu, proba zarezerwowania wiekszej ilosci miejsc niz ma sala, albo proba
        //zarezerwowania pustej liczby miejsc
        if ((emailField.getText().equals("")) || (Integer.parseInt(seatsField.getText()) > Integer.parseInt(
                seatsLabel.getText().replaceAll("\\D+", ""))) || (seatsField.getText().equals(""))) {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Label("Brak adresu e-mail lub "));
            dialogVbox.getChildren().add(new Label("niepoprawna liczba zarezerwowanych"));
            dialogVbox.getChildren().add(new Label("miejsc!"));
            dialogVbox.setAlignment(Pos.CENTER);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        } else {
            String email = emailField.getText();
            String occSeats = seatsField.getText();
            makePdf(email, occSeats);
            //tutaj mozna byloby to rozwinac o ilosc dostepnych miejsc... ale to by sie wiazalo ze stworzeniem nowej
            //kolumny w tabeli Show, ktora dla kazdego seansu trzymalaby liczbe zajetych miejsc
            //occupySeats(myShow, occSeats);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Label("Pomyślnie dokonano rezerwacji."));
            dialogVbox.getChildren().add(new Label("Rezerwacja wygenerowana w pliku pdf."));
            dialogVbox.setAlignment(Pos.CENTER);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }

    /*
    public void occupySeats(Show show, String seats) {
        Session ses = parentController.getSessionFactory().openSession();
        Query query = ses.createQuery("from Show show where show.id = :id");
        Integer id = show.getId();
        query.setParameter("id", id);

    }
    */

    public void makePdf(String email, String seats) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);

        PDRectangle rect = page1.getMediaBox();

        document.addPage(page1);

        PDFont fontMono = PDType1Font.COURIER;
        PDPageContentStream cos = new PDPageContentStream(document, page1);

        int line = 0;

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50 * (++line));
        cos.showText("Rezerwacja na adres mailowy: " + email);
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50 * (++line));
        cos.showText(movieLabel.getText());
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50 * (++line));
        cos.showText(dateLabel.getText());
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50 * (++line));
        cos.showText(timeLabel.getText());
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50 * (++line));
        cos.showText(numberLabel.getText());
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50 * (++line));
        cos.showText("Zarezerwowano " + seats + " miejsca");
        cos.endText();

        cos.close();


        document.save("Reservation.pdf");
        document.close();
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
