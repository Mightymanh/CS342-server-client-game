import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class LogController implements Initializable {

	private static final String ArrayList = null;

	// components for log scene
	@FXML
	ListView<String> list;
	
	@FXML
	Label portNumLabel;
	
	@FXML
	MenuItem exitApp;
	
	@FXML
	MenuItem closeServer;
	
	@FXML
	BorderPane logRoot;
	
	@FXML
	Label numClientLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	// actionEvent for exitApp
	public void exitApp(ActionEvent e) {
		System.exit(0);
	}
	
	// actionEvent for closeServer
	public void closeServer(ActionEvent e) throws IOException {
		// shut down server
		ServerGUI.server.shutdownServer();
		
		// back to start scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/startScene.fxml"));
		BorderPane startRoot = loader.load();
		logRoot.getScene().setRoot(startRoot);
	}

}
