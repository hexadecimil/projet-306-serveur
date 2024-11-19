package app.ctrl;

import app.helpers.JfxPopup;
import app.ihm.IViewForController;
import app.ihm.View;
import app.services.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author tornarem07
 * @version 1.0
 * @created 18-nov.-2024 16:36:21
 */
public class Controller implements IControllerForWebcam, IControllerDistanceRadar, IControllerForView, IControllerForWrkServer {

	private DistanceRadar distanceRadar;
	private LCDCamera lcdCamera;
	private MySQL mySQL;
	private MyWebCam myWebCam;
	private Rover rover;
	private WebcamServer webcamServer;
	private WrkServer wrkServer;
	private IViewForController viewForController;
	private Stage stage;

	public Controller(Stage stage){
		this.stage = stage;
		this.mySQL = new MySQL();
		this.wrkServer = new WrkServer(this);
	}

	public void actionConnectionUser(String utilisateur, String motDePasse){
		boolean isValid = false;
		try {
			isValid = mySQL.isUserValid(utilisateur, motDePasse);
		} catch (SQLException e) {
			JfxPopup.displayError("DB ERROR", "Erreur lors de la lecture des résultats", "");
        }
        if (isValid){
			changerView("/app/principal.fxml");
		} else {
			viewForController.connectCredentialsError();
		}
	}

	public void actionDisconnectionUser(){

	}

	public boolean connexionClient(String tag){
		boolean isValid = false;
		try {
			isValid = mySQL.isUserClientValid(tag);
		} catch (SQLException e) {
			JfxPopup.displayError("DB ERROR", "Erreur lors de la lecture des résultats", "");
		}
		if (isValid){
			viewForController.updateLabelConnectionMessage("Etat de la connexion: client connecté!");
		} else {

		}
		return isValid;
	}

	public void deconnexionClient(){

	}

	public void wallDetected(){

	}

	public void actionAutoriserConnection(boolean autoriser){
		if (autoriser) {
			wrkServer.demarrerServeur(3000);
		}
	}

	public void sendCommand(String type, double valeur){

	}

	public void actionWebcamFrame(BufferedImage frame) {

	}

	public void changerView(String FXML) {
		FXMLLoader myLoader = null;
		Parent loadScreen = null;

		try {
			// Charger le fichier FXML
			myLoader = new FXMLLoader(getClass().getResource(FXML));
			loadScreen = myLoader.load();

			// Récupérer le contrôleur actuel
			View viewController = myLoader.getController();

			// Réassocier le contrôleur principal (Controller)
			viewController.setController(this);

			// Remplacer la scène
			Scene scene = new Scene(loadScreen);
			stage.setScene(scene);
			stage.show();
		} catch (IOException ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Écouteur pour gérer la fermeture de l'application
		stage.setOnCloseRequest(e -> {
			e.consume(); // Éviter que la fenêtre ne se ferme directement
			quitter();   // Déléguer la gestion au contrôleur
		});
	}


	public void setViewForController(IViewForController viewForController) {
		this.viewForController = viewForController;
	}

	public void quitter() {
		System.exit(0);
	}
}