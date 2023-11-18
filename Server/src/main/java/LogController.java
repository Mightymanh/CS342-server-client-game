import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class LogController implements Initializable {

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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	// actionEvent for exitApp
	public void ExitApp(ActionEvent e) {
		Platform.exit();
	}
	
	// actionEvent for closeServer
//	public void closeServer(ActionEvent e) {
//		ServerGUI.server.listenT.close
//	}

}
