import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {


    @FXML
    public VBox root;
    public Label title;
    public Label result;
    public Button restart;

    public Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }



    public void exit(){
        ((Stage)root.getScene().getWindow()).close();
    }

    public void setClient(Client theClient){
        this.client = theClient;
    }


}
