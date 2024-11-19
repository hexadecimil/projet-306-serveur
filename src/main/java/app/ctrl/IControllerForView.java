package app.ctrl;


/**
 * @author tornarem07
 * @version 1.0
 * @created 18-nov.-2024 16:36:22
 */
public interface IControllerForView {

	void actionAutoriserConnection(boolean autoriser);

	void actionConnectionUser(String utilisateur, String motDePasse);

	void actionDisconnectionUser();

}