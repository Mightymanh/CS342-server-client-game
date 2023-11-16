import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenController implements Initializable {
    @FXML
    public Label CategoryName;
    @FXML
    public Label curWord;
    @FXML
    public Label promptText;
    @FXML
    public HBox ButtonField;
    @FXML
    public Button enterButton;
    @FXML
    public Button nextButton;

    public Client client;

    @FXML
    public VBox root;
    void handleEvent(){
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        handleEvent();


    }


    public void uploadLetter(ActionEvent e) {

        Button pressButton = (Button)e.getTarget();
        String letter = pressButton.getText();
        pressButton.setDisable(true);
        promptText.setText(letter);

    }

    public void nextButton()  {


        try{
            EndScreen();
        } catch (Exception err) {
            System.out.println("unable to change scene");
            err.printStackTrace();
        }
    }

    public void EndScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/EndScreen.fxml"));
        Parent Endroot = fxmlLoader.load();
        EndScreenController controller = fxmlLoader.<EndScreenController>getController();
        root.getScene().setRoot(Endroot);
    }



    public void setClient(Client theClient){
        this.client = theClient;
    }


}
