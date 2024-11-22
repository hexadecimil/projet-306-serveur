package app.ihm;

import app.ctrl.IControllerForView;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Thibaud Gamez
 * @version 1.0
 * @created 18-nov.-2024 16:36:24
 */
public class View implements IViewForController, Initializable {
	@FXML
	public ImageView frameBox;
	private IControllerForView controller;
	@FXML
	private TextField userField;
	@FXML
	private TextField passwordField;
	@FXML
	private Label errorLabel;
	@FXML
	private Label labelConnect;
	@FXML
	private Button connectionButton;

	public View(){

    }

	@Override
	public void initialize(URL url, ResourceBundle rb){
	}

	public void afficherImageWebcam(BufferedImage image){
		if (frameBox!=null) {
			Image frame = SwingFXUtils.toFXImage(image, null);
			frameBox.setImage(frame);
		}
	}

	public void afficherVueConnexion(){

	}

	public void afficherVuePrincipal(){

	}

	public void messageErreur(String message){

	}

	public void messageInfo(String message){

	}

	@FXML
	public void onAutoriserConnection(){
		controller.actionAutoriserConnection();
	}

	public void onDisconnectionUser(ActionEvent e){

	}

	public void updateLabelConnectionMessage(String message) {
		labelConnect.setText(message);
	}

	public void connectCredentialsError() {
		errorLabel.setVisible(true);
	}

	public void serverOn(boolean isOn) {
		if (isOn) {
			connectionButton.setText("Fermer serveur");
		} else {
			connectionButton.setText("Allumer serveur");
		}
	}

	@Deprecated
	public void onConnectClick(javafx.event.ActionEvent actionEvent) {
		controller.actionConnectionUser(userField.getText(), passwordField.getText());
	}

	public void setController(IControllerForView controller) {
		this.controller = controller;
	}
}