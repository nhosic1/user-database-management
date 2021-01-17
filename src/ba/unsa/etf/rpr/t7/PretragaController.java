package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PretragaController {
    public TextField fldPretraga;
    public TilePane tilePane;

    public void cancel(ActionEvent actionEvent) {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        stage.close();
    }

    public void search(ActionEvent actionEvent) {
        String pretraga = fldPretraga.getText().replace(" ", "+");
        String adresa = "http://api.giphy.com/v1/gifs/search?q=" + pretraga + "&api_key=K7ZzpHSODkIauWCy4wi0qgypKMpicFX5&limit=25";

        try{
            URL url = new URL(adresa);
            BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String json = "", line = null;
            while ((line = ulaz.readLine()) != null) {
                json = json + line;
            }
            ulaz.close();
            JSONObject obj = new JSONObject(json);
            JSONArray jArray = obj.getJSONArray("data");
            for(int i=0; i<jArray.length(); i++){
                JSONObject o = jArray.getJSONObject(i);
                String img = o.getJSONObject("images").getJSONObject("original_still").getString("url");
                int poc = img.indexOf("giphy");
                int kraj = img.indexOf("?");
                String link = "https://i." + img.substring(poc, kraj);
                Button b = new Button();
                ImageView view = new ImageView(new Image(link));
                view.setFitHeight(128);
                view.setFitWidth(128);
                b.setGraphic(view);
                tilePane.getChildren().add(b);
            }
        }  catch(MalformedURLException e) {
            System.out.println("String "+adresa+" ne predstavlja validan URL");
        } catch(IOException e) {
            System.out.println("Greška pri kreiranju ulaznog toka");
            System.out.println(e.getMessage());
        } catch(Exception e) {
            System.out.println("Poteškoće sa obradom JSON podataka");
            System.out.println(e.getMessage());
        }
    }
}
