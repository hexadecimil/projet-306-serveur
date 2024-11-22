package app.ctrl;


/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:22
 */
public interface IControllerForWrkServer {

	boolean connexionClient(String tag);

	void deconnexionClient();

	void move(double move, double turn);

	void serveurDemmarer(boolean isDemarrer);

}