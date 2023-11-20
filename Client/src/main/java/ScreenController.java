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

        nextButton.setDisable(true);



    }


    public void uploadLetter() {
        if(enterButton.isDisable()) {
            nextButton();
            return;
        }
        curLetter = "";
        curLetter = letterField.getText();

        letterField.clear();
        if(curLetter.length() != 1 || !Character.isLetter(curLetter.charAt(0))) {

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

        }  else{
            updateLetterResult("Incorrect");
        }

        
    //    System.out.println("weweewe");

        curWord.setText(client.word);
        if (client.gameInfo.roundStatus != 0){
            endOfRound();
        }
    }
    
    public void updateLetterResult(String result) {
        promptText.setText(result);
        attemptLabel.setText("Attempt Left: " + this.client.gameInfo.guessRemain);
        
    }

    public void endOfRound() {
        enterButton.setDisable(true);
        nextButton.setDisable(false);
        if(client.gameInfo.roundStatus == 1) {
            // disable the category
            client.categoryWon.replace(CategoryName.getText(), 1);
            updateLetterResult("You win this round. Press next to continue");

        } else if (client.gameInfo.roundStatus == -1) {
            updateLetterResult("You lost this round. Press next to continue");
        }
    }

    public void nextButton()  {
        client.receiveObject(); // receiving game status
        try{

            if (client.gameInfo.gameStatus== 0) {
                // continue the game
                CategoryScene();

            } else {
                // endGame scene
                EndScreen();
            }

        } catch (Exception err) {
            System.out.println("unable to change scene");
            err.printStackTrace();
        }
    }

    public void EndScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/EndScreen.fxml"));
        Parent Endroot = fxmlLoader.load();
        EndScreenController controller = fxmlLoader.<EndScreenController>getController();
        controller.setClient(this.client);
        root.getScene().setRoot(Endroot);
    }


    public void CategoryScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Category.fxml"));
        Parent cateRoot = fxmlLoader.load();
        CategoryController controller = fxmlLoader.<CategoryController>getController();
        controller.setClient(this.client, 0);
        root.getScene().setRoot(cateRoot);
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
