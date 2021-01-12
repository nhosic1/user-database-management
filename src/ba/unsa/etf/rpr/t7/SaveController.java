package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SaveController {
    public Label datotekaLabel;

    public void odaberiDatoteku(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            String fileAsString = file.toString();
            String[] s = fileAsString.split("/");

            datotekaLabel.setText("Datoteka: " + s[s.length - 1]);
        } else {
            datotekaLabel.setText("Datoteka: ");
        }
    }
}
