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
import java.util.HashMap;
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
    public Label attemptLabel;

    String curLetter;

    HashMap<Character, Integer> alphaList;

    @FXML
    public VBox root;


    void handleEvent(){
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        handleEvent();

        alphaList = new HashMap<>();

        for(int i = 97; i <= 122;i++) {
            char letter = (char)i;
            //System.out.println(letter);
                alphaList.put(letter, 1);


        }




    }


    public void uploadLetter() {

        curLetter = letterField.getText();

        if(curLetter.length() > 1 || !Character.isLetter(curLetter.charAt(0))) {

            promptText.setText("Invalid Input");
            return;
        }

        if (alphaList.get(curLetter.charAt(0)) == 0) {
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

        alphaList.replace(curLetter.charAt(0), 0);
        if(client.gameInfo.position != null && !client.gameInfo.position.isEmpty()) {
            updateLetterResult("correct");

            client.updateWord(curLetter.charAt(0), client.gameInfo.position);

        } else if (client.gameInfo.guessRemain == 0){
                System.out.println("Round over");
    } else{
            updateLetterResult("Incorrect");
        }
        
    //    System.out.println("weweewe");

        curWord.setText(client.word);
    }
    
    public void updateLetterResult(String result) {
        promptText.setText(result);
        attemptLabel.setText("Attempt Left: " + this.client.gameInfo.guessRemain);
        
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



    public void displayWord(){
        this.curWord.setText(this.client.word);
    }
    public void setClient(Client theClient){
        this.client = theClient;
        CategoryName.setText(this.client.gameInfo.category);
        this.client.initWord();
        displayWord();

    }


}
