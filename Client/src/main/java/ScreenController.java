import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

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

    @FXML
    public TextField letterField;
    public Client client;

    String curLetter;

    Set<Character> alphaList;

    @FXML
    public VBox root;


    void handleEvent(){
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        handleEvent();

        for(int i = 'a'; i <= 'z';i++) {
            alphaList.add((char)i);
        }

        for(Character i: alphaList) {
            System.out.println(i);
        }


    }


    public void uploadLetter(ActionEvent e) {

        String curText = letterField.getText();

        if(curText.length() > 1 || !Character.isLetter(curText.charAt(0))) {

            promptText.setText("Invalid Input");
            return;
        }

        if (!alphaList.contains(curText.charAt(0))) {
            promptText.setText("Letter already picked");
            return;
        }




        promptText.setText(curLetter);
        client.gameInfo.guessLetter= curLetter.charAt(0);
        dealtwithLetter();
    }


    public void dealtwithLetter() {

        client.sentObject();

        client.receiveObject();

        if(client.gameInfo.position != null && !client.gameInfo.position.isEmpty()) {
            client.updateWord(curLetter.charAt(0), client.gameInfo.position);
        } else if (client.gameInfo.guessRemain == 0){
                System.out.println("Round over");


    }


        curWord.setText(client.word);
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
