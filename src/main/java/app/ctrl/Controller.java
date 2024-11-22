package app.ctrl;

import app.helpers.JfxPopup;
import app.ihm.IViewForController;
import app.ihm.View;
import app.services.*;
import com.phidget22.Net;
import com.phidget22.PhidgetException;
import com.phidget22.ServerType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
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
	private WebcamServer webcamServer = null;
	private WrkServer wrkServer;
	private IViewForController viewForController;
	private Stage stage;
	public final static int VINT_HUB_SERIAL = 667681;
	private boolean wallDetected = false;

	public Controller(Stage stage){

		this.stage = stage;
		this.mySQL = new MySQL();
		this.wrkServer = new WrkServer(this);
        try {
		  	Net.enableServerDiscovery(ServerType.DEVICE_REMOTE);
			System.out.println("creation rover");
            //this.rover = new Rover(VINT_HUB_SERIAL);
			System.out.println("creation cam");
			//this.lcdCamera = new LCDCamera(VINT_HUB_SERIAL);
			System.out.println("creation sensor");
			//this.distanceRadar = new DistanceRadar(this, VINT_HUB_SERIAL);
        } catch (PhidgetException e) {
			System.out.println("error");
            throw new RuntimeException(e);
        }
        System.out.println("Creation webcam");
		this.myWebCam = new MyWebCam(this);
        myWebCam.start();
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

	public boolean connexionClient(String tag){
		boolean isValid = false;
		int privilege = 0;
		try {
			privilege = mySQL.userClientPrivilege(tag);
		} catch (SQLException e) {
			JfxPopup.displayError("DB ERROR", "Erreur lors de la lecture des résultats", "");
		}
		if (privilege > 0){
			isValid = true;
			System.out.printf("ok client");
			wrkServer.getClient().ecrire("OK,"+privilege);
            try {
				System.out.println(wrkServer.getClient().getIp());
		   		this.webcamServer = new WebcamServer(this, "WSTEMFA44-14");
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
				viewForController.updateLabelConnectionMessage("Etat de la connexion: client connecté!");
			});
		} else {
			System.out.println("nok client");
		}

		return isValid;
	}

	public void deconnexionClient(){
		Platform.runLater(() -> {
			viewForController.updateLabelConnectionMessage("Etat de la connexion: serveur démarrer!");
		});
	}

	public void wallDetected(boolean isDetected){
		this.wallDetected = isDetected;
		try {
			if (isDetected && rover.getSpeed()) {
				rover.sendValue(0, 0);
			}
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
    }

	public void actionAutoriserConnection(){
		if (!wrkServer.isRunning()) {
			wrkServer.demarrerServeur(3000);
		} else {
			wrkServer.arreterServeur();
		}
	}

	public void move(double move, double turn){
		if (move>0.0d && wallDetected) {
			wrkServer.getClient().ecrire("NOK");
			return;
		}
        try {
            rover.sendValue(move, turn);
        } catch (PhidgetException e) {
            throw new RuntimeException(e);
        }
    }

	public void serveurDemmarer(boolean isDemarrer) {
		if (isDemarrer) {
			viewForController.updateLabelConnectionMessage("Etat de la connexion: serveur démarrer!");
			viewForController.serverOn(true);
		} else {
			viewForController.updateLabelConnectionMessage("Etat de la connexion: serveur éteint!");
			viewForController.serverOn(false);
		}
	}

	public void actionWebcamFrame(BufferedImage frame) {
		if (lcdCamera!=null) {
            try {
                lcdCamera.sendFrame(frame);
            } catch (PhidgetException | IOException e) {
                throw new RuntimeException(e);
            }
        }

		if (webcamServer!=null) {
			webcamServer.sendFrame(frame);
		}
		viewForController.afficherImageWebcam(frame);
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
			String css = this.getClass().getResource("/app/style.css").toExternalForm();
			scene.getStylesheets().add(css);
			stage.setScene(scene);
			stage.show();
			setViewForController(viewController);
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
		if (rover!=null) {
            try {
                rover.closeDcMotor();
				lcdCamera.close();
            } catch (PhidgetException e) {
                throw new RuntimeException(e);
            }
        }

		wrkServer.arreterServeur();
		System.exit(0);
	}
}