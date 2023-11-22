import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {


    @FXML
    public Label title;

    @FXML
    public HBox categoryBox;

    @FXML
    public Button enterButton;

    @FXML
    public HBox root;
    public VBox VBoxContainer;
    public Button animalButton;
    public Button weatherButton;
    public Button toolsButton;
    private String CategoryChose;
    public Client client;
    void handleEvent(){
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.CategoryChose = null;
        handleEvent();
    }



    public void displayCategory(ActionEvent e) {
        CategoryChose = ((Button)e.getTarget()).getText();
        System.out.println(CategoryChose);
        title.setText(CategoryChose.toUpperCase());
    }

    public void buttonPress(){
        if(CategoryChose == null) {
            title.setText("Must Choose a Category");
            return;
        }
        title.setText(CategoryChose.toUpperCase());


       // this.client.receiveObject();
        this.client.gameInfo.category = CategoryChose;

        if(!client.sentObject()) {

            System.out.println("error");
            return;
        }


    //    System.out.println(this.client.gameInfo.category);
        if(!client.receiveObject()) {
            return;
        }
        System.out.println(client.gameInfo.wordLength);
        client.initWord();
        try{
            screenScene();
        } catch (Exception error ){
            System.out.println("Failed to change scene");
            error.printStackTrace();
        }



    }

    public void screenScene() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/GameScreen.fxml"));

        Parent screenRoot = fxmlLoader.load();
        ScreenController controller = fxmlLoader.<ScreenController>getController();
        controller.setClient(client);
        Scene s1 = new Scene(screenRoot, 600, 500);
        s1.getStylesheets().add("/style/GameScreen.css");
        ((Stage)root.getScene().getWindow()).setScene(s1);

    }


    public void startRound(){
        this.client.gameInfo.roundStatus = 2;
        this.client.sentObject();
    }


    public void disableWonCategory(){
        for(Node i: categoryBox.getChildren()) {
            if(client.categoryWon.get(((Button)i).getText()) == 1) {
                i.setDisable(true);
            }
        }

    }
    public void setClient(Client theClient){
        this.client = theClient;


        startRound();
        disableWonCategory();
    }


}
