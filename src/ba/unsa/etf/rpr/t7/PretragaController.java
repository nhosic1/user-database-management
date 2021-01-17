package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    public ScrollPane scrollPane;

    public void cancel(ActionEvent actionEvent) {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        stage.close();
    }

    public void search(ActionEvent actionEvent) {
        String adresa = "http://api.giphy.com/v1/gifs/search?q=cat&api_key=K7ZzpHSODkIauWCy4wi0qgypKMpicFX5&limit=5";
        //String adresa = "https://giphy.com/search/cat";
        try{
            URL url = new URL(adresa);
            BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String json = "", line = null;
            while ((line = ulaz.readLine()) != null) {
                json = json + line;
                System.out.println(line);
            }
            ulaz.close();
            JSONObject obj = new JSONObject(json);
            JSONArray jArray = obj.getJSONArray("data");
            for(int i=0; i<jArray.length(); i++){
                JSONObject o = jArray.getJSONObject(i);
                System.out.println(o.getJSONObject("images").getJSONObject("original_still").getString("url"));
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
