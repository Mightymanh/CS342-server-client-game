import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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


    public void restartButton() {
        this.client.gameInfo.restart  = 1;

        this.client.sentObject();
        this.client.reset();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Category.fxml"));
            Parent cateRoot = fxmlLoader.load();
            CategoryController controller = fxmlLoader.<CategoryController>getController();
            controller.setClient(this.client, 1);
            root.getScene().setRoot(cateRoot);
        } catch (Exception e) {
            System.out.println("failed to change scene");
            e.printStackTrace();
        }


    }

    public void exitButton() {
        this.client.gameInfo.restart  = 0;
        this.client.sentObject();
        exit();
    }
    public void setClient(Client theClient){

        this.client = theClient;

        displayGameResult();
    }


}
