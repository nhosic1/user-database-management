package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class KorisnikController {
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldEmail;
    public TextField fldUsername;
    public ListView<Korisnik> listKorisnici;
    public PasswordField fldPassword;
    public ImageView imageView;

    private KorisniciModel model;

    public KorisnikController(KorisniciModel model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        listKorisnici.setItems(model.getKorisnici());
        listKorisnici.getSelectionModel().selectedItemProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            if(oldKorisnik != null) model.izmijeniKorisnika(oldKorisnik);
            model.setTrenutniKorisnik(newKorisnik);
            if(newKorisnik != null) {
                imageView.setImage(new Image(newKorisnik.getSlika()));
            } else{
                imageView.setImage(null);
            }
            listKorisnici.refresh();
         });

        model.trenutniKorisnikProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            if (oldKorisnik != null) {
                fldIme.textProperty().unbindBidirectional(oldKorisnik.imeProperty() );
                fldPrezime.textProperty().unbindBidirectional(oldKorisnik.prezimeProperty() );
                fldEmail.textProperty().unbindBidirectional(oldKorisnik.emailProperty() );
                fldUsername.textProperty().unbindBidirectional(oldKorisnik.usernameProperty() );
                fldPassword.textProperty().unbindBidirectional(oldKorisnik.passwordProperty() );
            }
            if (newKorisnik == null) {
                fldIme.setText("");
                fldPrezime.setText("");
                fldEmail.setText("");
                fldUsername.setText("");
                fldPassword.setText("");
            }
            else {
                fldIme.textProperty().bindBidirectional( newKorisnik.imeProperty() );
                fldPrezime.textProperty().bindBidirectional( newKorisnik.prezimeProperty() );
                fldEmail.textProperty().bindBidirectional( newKorisnik.emailProperty() );
                fldUsername.textProperty().bindBidirectional( newKorisnik.usernameProperty() );
                fldPassword.textProperty().bindBidirectional( newKorisnik.passwordProperty() );
            }
        });

        fldIme.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldIme.getStyleClass().removeAll("poljeNijeIspravno");
                fldIme.getStyleClass().add("poljeIspravno");
            } else {
                fldIme.getStyleClass().removeAll("poljeIspravno");
                fldIme.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPrezime.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPrezime.getStyleClass().removeAll("poljeNijeIspravno");
                fldPrezime.getStyleClass().add("poljeIspravno");
            } else {
                fldPrezime.getStyleClass().removeAll("poljeIspravno");
                fldPrezime.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldEmail.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldEmail.getStyleClass().removeAll("poljeNijeIspravno");
                fldEmail.getStyleClass().add("poljeIspravno");
            } else {
                fldEmail.getStyleClass().removeAll("poljeIspravno");
                fldEmail.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldUsername.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
                fldUsername.getStyleClass().add("poljeIspravno");
            } else {
                fldUsername.getStyleClass().removeAll("poljeIspravno");
                fldUsername.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPassword.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldPassword.getStyleClass().removeAll("poljeIspravno");
                fldPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });

        if(!listKorisnici.getItems().isEmpty()) listKorisnici.getSelectionModel().selectFirst();
    }

    public void dodajAction(ActionEvent actionEvent) {
        Korisnik noviKorisnik = new Korisnik(model.dajNoviIdKorisnika(),"", "", "", "", "");
        model.getKorisnici().add(noviKorisnik);
        model.dodajKorisnika(noviKorisnik);
        listKorisnici.getSelectionModel().selectLast();
    }

    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }
    public void obrisiAction(ActionEvent actionEvent){
        if(model.getTrenutniKorisnik() != null){
            model.obrisiKorisnika(model.getTrenutniKorisnik().getId());
            listKorisnici.getItems().remove(model.getTrenutniKorisnik());
            listKorisnici.getSelectionModel().select(0);
        }
    }
    public void exit(ActionEvent actionEvent){
        System.exit(0);
    }
    public void about(ActionEvent actionEvent) throws Exception{
        Stage newStage = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/about.fxml"), bundle);
        if(Locale.getDefault().getLanguage().equals("bs")){
            newStage.setTitle("O aplikaciji");
        }else {
            newStage.setTitle("About");
        }
        newStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        newStage.show();
    }
    public void save(ActionEvent actionEvent) throws Exception{
        File file;
        FileChooser chooser = new FileChooser();
        if(Locale.getDefault().getLanguage().equals("bs")){
            chooser.setTitle("Sačuvaj");
        }else {
            chooser.setTitle("Save");
        }
        Stage stage = (Stage) fldIme.getScene().getWindow();
        file = chooser.showOpenDialog(stage);
        model.zapisiDatoteku(file);
    }
    public void print(ActionEvent actionEvent){
        try {
            new PrintReport().showReport(model.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }
    public void bosanski(ActionEvent actionEvent){
        Locale.setDefault(new Locale("bs"));
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/korisnici.fxml"), bundle);
        model.resetTrenutniKorisnik();
        KorisnikController ctrl = new KorisnikController(model);
        loader.setController(ctrl);
        Scene scene = fldIme.getScene();
        Stage stage = (Stage)scene.getWindow();
        stage.setTitle("Korisnici");
        try {
            scene.setRoot(loader.load());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void engleski(ActionEvent actionEvent){
        Locale.setDefault(new Locale("en", "US"));
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/korisnici.fxml"), bundle);
        model.resetTrenutniKorisnik();
        KorisnikController ctrl = new KorisnikController(model);
        loader.setController(ctrl);
        Scene scene = fldIme.getScene();
        Stage stage = (Stage)scene.getWindow();
        stage.setTitle("Users");
        try {
            scene.setRoot(loader.load());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void imgAction(ActionEvent actionEvent) throws IOException {
        if (!listKorisnici.getSelectionModel().isEmpty()) {
            Stage newStage = new Stage();
            PretragaController controller = new PretragaController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pretraga.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            newStage.setTitle("Pretraga slike");
            newStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            newStage.show();
            newStage.setOnHiding(event -> {
                if (controller.isSlikaOdabrana()) {
                    imageView.setImage(new Image(controller.getLinkIzabraneSlike()));
                    model.getTrenutniKorisnik().setSlika(controller.getLinkIzabraneSlike());
                    model.izmijeniKorisnika(model.getTrenutniKorisnik());
                }
            });
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nije izabran korisnik");
            alert.setHeaderText("Niste izabrali korisnika kojeg želite");
            alert.setContentText("Izaberite korisnika ili dodajte novog");
            alert.show();
        }
    }
}
