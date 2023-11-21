import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ServerGUI extends Application {

	static LogController logCtrl;
	static Server server;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Server GUI");
		
		// loading resources
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/startScene.fxml"));
		BorderPane root = loader.load();
		
		// initialize scene
		Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
		scene.getStylesheets().add("/Style/style.css");

		// show scene
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
