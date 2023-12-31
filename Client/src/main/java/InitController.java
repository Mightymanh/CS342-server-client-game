import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InitController implements Initializable {


    public VBox vboxContainer;
    @FXML
    Label title;

    @FXML
    Button confirm;

    @FXML
    Label prompt;

    @FXML
    TextField portBox;

    @FXML
    HBox root;

    public Client client;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addEvents();



    }

    public void addEvents(){
        confirm.setOnAction((e)->{

           if(!portNumberEnter()) {
               return;
           }
            try{
                GameMenuScene();
            } catch (Exception error) {
                System.out.println("failed to change scene");
                error.printStackTrace();
            }

        });
    }

    public boolean portNumberEnter(){

        String portNum = portBox.getText();
        int portNumber;
        try {
            portNumber = Integer.parseInt(portNum);


        } catch (Exception e) {
            prompt.setText("Invalid port number. Try again");
            return false;
        }

        if(!client.connect(portNumber)) {
            prompt.setText("Connection failed. Try again");
            return false;
        }


        prompt.setText(portNum);
        return true;
    }



    public void setClient(Client theClient){
        this.client = theClient;
    }

    public void GameMenuScene() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/GameMenu.fxml"));
        Parent Catroot = fxmlLoader.load();
        GameMenuController controller = fxmlLoader.<GameMenuController>getController();
        controller.setClient(client);

        Scene s1 = new Scene(Catroot, 600, 500);
        s1.getStylesheets().add("/style/GameMenu.css");
        ((Stage)root.getScene().getWindow()).setScene(s1);

    }


}
