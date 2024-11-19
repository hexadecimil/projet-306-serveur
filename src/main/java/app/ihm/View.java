package app.ihm;

import app.ctrl.IControllerForView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Thibaud Gamez
 * @version 1.0
 * @created 18-nov.-2024 16:36:24
 */
public class View implements IViewForController, Initializable {

	private IControllerForView controller;
	@FXML
	private Label labelConnect;
	@FXML
	private TextField userField;
	@FXML
	private TextField passwordField;
	@FXML
	private Label errorLabel;

	public View(){

    }

	@Override
	public void initialize(URL url, ResourceBundle rb){

	}

	public void afficherImageWebcam(){

	}

	public void afficherVueConnexion(){

	}

	public void afficherVuePrincipal(){

	}

	public void messageErreur(String message){

	}

	public void messageInfo(String message){

	}

	public void onAutoriserConnection(){
		controller.actionAutoriserConnection(true);
	}

	public void onDisconnectionUser(ActionEvent e){

	}

	public void updateLabelConnectionMessage(String message) {
		labelConnect.setText(message);
	}

	public void connectCredentialsError() {
		errorLabel.setVisible(true);
	}

	@Deprecated
	public void onConnectClick(javafx.event.ActionEvent actionEvent) {
		controller.actionConnectionUser(userField.getText(), passwordField.getText());
	}

	public void setController(IControllerForView controller) {
		this.controller = controller;
	}
}