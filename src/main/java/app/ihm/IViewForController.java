package app.ihm;


import java.awt.image.BufferedImage;

/**
 * @author tornarem07
 * @version 1.0
 * @created 18-nov.-2024 16:36:22
 */
public interface IViewForController {

	 void afficherImageWebcam(BufferedImage image);

	void afficherVueConnexion();

	void afficherVuePrincipal();

	void messageErreur(String message);

	void messageInfo(String message);

	void updateLabelConnectionMessage(String message);

	void connectCredentialsError();

	void serverOn(boolean isOn);

}