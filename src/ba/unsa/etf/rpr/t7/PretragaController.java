package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PretragaController {
    public void cancel(ActionEvent actionEvent) {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        stage.close();
    }
}
