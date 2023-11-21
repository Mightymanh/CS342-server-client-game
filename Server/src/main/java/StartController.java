import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class StartController implements Initializable {
	
	// components for start scene
	@FXML
	Button confirmButton;
	
	@ FXML
	TextField prompt;
	
	@FXML
	Label serverLabel;
	
	@FXML
	Label warningLabel;
	
	@FXML
	MenuItem exitApp;
	
	@FXML
	BorderPane startRoot;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	private void createServerLog(int port) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/logScene.fxml"));
		BorderPane logRoot = loader.load();
		startRoot.getScene().setRoot(logRoot);
		LogController ctrl = loader.getController();
		ListView<String> list = ctrl.list;
		Consumer<Serializable> call = (data) -> {
			Platform.runLater(() -> {
				list.getItems().add(data.toString());
				list.scrollTo(list.getItems().size() - 1);
				//System.out.println(data.toString());
			});	
		};
				
		ServerGUI.server = new Server(port, call);	
		ctrl.portNumLabel.setText("Listening on port: " + ServerGUI.server.port);
	}
	
	// EVENT HANDLERS FOR START SCENE
	
	// actionEvent for confirmButton
	public void getPortCreateServer(ActionEvent e) throws IOException {
		int port;
		try {
			port = Integer.parseInt(prompt.getText());
			if (port <= 0) {
				warningLabel.setText("Must Be Positive Integer!");
				return;
			}
		}
		catch (Exception ex) {
			warningLabel.setText("Must Be Positive Integer!");
			return;
		}
		
		// create new server listening to that port and new scene for server log
		createServerLog(port);
	}
	
	// actionEvent for exitApp
	public void ExitApp(ActionEvent e) {
		System.exit(0);
	}
}
