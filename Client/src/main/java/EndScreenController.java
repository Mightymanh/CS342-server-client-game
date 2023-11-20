import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void displayGameResult() {
        if(this.client.gameInfo.gameStatus==1){
            result.setText("You Won");
        } else if (this.client.gameInfo.gameStatus==-1){
            result.setText("You Lost");
        } else {
            result.setText("gamestatus is " + this.client.gameInfo.gameStatus);
        }
    }


    public void menuButton () {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/GameMenu.fxml"));
            Parent Catroot = fxmlLoader.load();
            GameMenuController controller = fxmlLoader.<GameMenuController>getController();
            controller.setClient(client);

            Scene s1 = new Scene(Catroot, 700, 700);
            //s1.getStylesheets().add("/style/GameMenu.css");
            ((Stage)root.getScene().getWindow()).setScene(s1);
        } catch (Exception e) {
            System.out.println("Failed to change scene");
            e.printStackTrace();
        }
    }

    public void setClient(Client theClient){

        this.client = theClient;

        displayGameResult();
    }


}
