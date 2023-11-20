import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameMenuController {


    public Button start;
    public Button Exit;

    public Client client;
    public VBox root;

    public void exitButton() {
        this.client.receiveObject();
        if(client.gameInfo.gameStatus == -2) {
            client.gameInfo.gameStatus = -3;
            this.client.sentObject();
        }

        ((Stage)root.getScene().getWindow()).close();


    }

    public void startButton() {
        this.client.reset();
        this.client.receiveObject();
        if(client.gameInfo.gameStatus == -2) {
            client.gameInfo.gameStatus = 2;
            this.client.sentObject();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Category.fxml"));
            Parent cateRoot = fxmlLoader.load();
            CategoryController controller = fxmlLoader.<CategoryController>getController();
            controller.setClient(this.client);
            root.getScene().setRoot(cateRoot);
        } catch (Exception e) {
            System.out.println("Failed to change scene");
            e.printStackTrace();
        }

    }


    public void setClient(Client client) {
        this.client = client;

    }
}
