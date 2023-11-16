import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ServerGUI extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Server GUI");
		
		Parent root = FXMLLoader.load(getClass().getResource("FXML/startScene.fxml"));
		
		// initialize scene
		Scene scene = new Scene(root, 500, 400);
		//scene.getStylesheets().add("/styles/style.css");
				
		// show scene
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
