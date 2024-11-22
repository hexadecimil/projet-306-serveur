package app.services;

import app.helpers.JfxPopup;
import java.sql.*;

/**
 * @author GamezT01
 * @version 1.0
 * @created 18-nov.-2024 16:36:23
 */
public class MySQL {

	private static final String JDBC = "jdbc:mysql://localhost:3306/projet_306";
	String user = "root";
	String password = "";
	Connection connection = null;
	PreparedStatement preparedStatement = null;

	public MySQL() {}

	/**
	 * Vérifie si l'utilisateur existe et si le mot de passe est correct
	 * @param user - Nom d'utilisateur
	 * @param password - Mot de passe
	 * @return true si l'utilisateur est valide, false sinon
	 */
	public boolean isUserValid(String user, String password) throws SQLException {
		String sql = "SELECT * FROM t_Utilisateur WHERE utilisateur = ? AND password = ?";
		boolean isValid = false;

		// Appel à la méthode de requête à la base de données
		ResultSet resultSet = requestToDB(sql, user, password);

		// Si le résultat n'est pas nul, vérifiez si l'utilisateur existe
		if (resultSet != null) {
			if (resultSet.next()) {
				isValid = true;
			}
		}

		// Fermer les ressources ici (resultSet et connection)
		closeResources(resultSet);

		return isValid;
	}

	public int userClientPrivilege(String tag) throws SQLException {
		String sql = "SELECT privilege FROM t_Client WHERE tag = ?";
		int privilege = 0;

		// Appel à la méthode de requête à la base de données
		ResultSet resultSet = requestToDB(sql, tag);

		// Si le résultat n'est pas nul, récupérez la valeur du champ "privilege"
		if (resultSet != null) {
			if (resultSet.next()) {
				privilege = resultSet.getInt("privilege"); // Récupère la valeur du champ "privilege"
			}
		}

		// Fermer les ressources ici (resultSet et connection)
		closeResources(resultSet);

		return privilege; // Retourner la valeur du privilège
	}


	/**
	 * Effectue la requête à la base de données et renvoie le ResultSet
	 * @param sql - La requête SQL
	 * @param args - Les paramètres de la requête
	 * @return ResultSet
	 */
	public ResultSet requestToDB(String sql, String... args) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Charger le driver MySQL
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Connexion à la base de données
			connection = DriverManager.getConnection(JDBC, this.user, this.password);

			// Préparer la requête avec les paramètres
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, args[0]);
			if (args.length>1) {
				preparedStatement.setString(2, args[1]); // Deuxième paramètre: mot de passe
			}


			// Exécuter la requête
			resultSet = preparedStatement.executeQuery();

		} catch (ClassNotFoundException e) {
			JfxPopup.displayError("DB ERROR", "Erreur lors du chargement du driver MySQL.", "");
			e.printStackTrace();
		} catch (SQLException e) {
			JfxPopup.displayError("DB ERROR", "Erreur lors de la connexion à la base de données.", "");
			e.printStackTrace();
		}

		return resultSet;
	}

	/**
	 * Ferme les ressources ouvertes comme ResultSet et Connection
	 * @param resultSet - Le ResultSet à fermer
	 */
	private void closeResources(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close(); // Fermer le ResultSet
			}
			if (connection != null) {
				connection.close(); // Fermer la connexion
			}
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}