import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ClientGUI extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Client GUI");


		Client client = new Client();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Init.fxml"));
		Parent root = fxmlLoader.load();

		InitController controller = fxmlLoader.<InitController>getController();
		controller.setClient(client);
		Scene s1 = new Scene(root, 600,500);
		s1.getStylesheets().add("/style/Init.css");
		primaryStage.setScene(s1);
		primaryStage.show();


	}

}
