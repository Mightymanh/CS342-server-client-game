import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {


    @FXML
    public Label title;

    @FXML
    public HBox categoryBox;

    @FXML
    public Button enterButton;
    public Menu option;
    public MenuItem restart;
    public MenuItem exit;

    @FXML
    public VBox root;
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

        title.setText(CategoryChose);
    }

    public void buttonPress(){
        if(CategoryChose == null) {
            title.setText("Must Choose a category to continue");
            return;
        }
        title.setText(CategoryChose);

        this.client.gameInfo.category = CategoryChose;
        if(!client.sentObject()) {

            System.out.println("error");
            return;
        }

        System.out.println(this.client.gameInfo.category);
        if(!client.receiveObject()) {
            return;
        }

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
        root.getScene().setRoot(screenRoot);

    }


    public void startRound(){
        this.client.gameInfo.roundStatus = 0;
        this.client.sentObject();
    }

    public void setClient(Client theClient){
        this.client = theClient;
        this.client.receiveObject();
        if(client.gameInfo.gameStatus == -2) {
            client.gameInfo.gameStatus = 0;
        }

        this.client.sentObject();

        startRound();


    }

}
