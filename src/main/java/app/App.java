package app;

import app.ctrl.Controller;
import app.helpers.JfxPopup;
import app.ihm.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public enum SceneName {
        MAIN, LOGIN, MORE;
    }

    private static final String TITLE = "Client View";
    private static final String LOGO = "resources/images/emf-info.png";
    private static final String FXML = "/app/login.fxml";
    private static final String ERROR_TITLE = "Erreur";
    private static final String ERROR_MSG = "App.start:\nProblème avec le fichier";

    @Override
    public void start(Stage stage) {


        // charger la vue principale
        FXMLLoader loader = null;
        Parent loginView = null;
        try {
            loader = new FXMLLoader(getClass().getResource(FXML));
            loginView = loader.load();
        } catch (java.lang.IllegalStateException | IOException ex) {
            String errMsg = ERROR_MSG + " " + FXML + ".\n\n" + ex.getMessage();
            JfxPopup.displayError(ERROR_TITLE, null, errMsg);
            ex.printStackTrace();
            System.exit(-1);
        }

        Controller ctrl = new Controller(stage);
        View viewController = loader.getController();

        ctrl.setViewForController(viewController);
        viewController.setController(ctrl);

        // préparer la première scène
        Scene scene1 = new Scene(loginView);
        String css = this.getClass().getResource("/app/style.css").toExternalForm();
        scene1.getStylesheets().add(css);

        // modifier l'estrade pour la première scène
        stage.setScene(scene1);

        // choisir un titre pour la fenêtre principale (pour la pièce de théâtre)
        stage.setTitle(TITLE);


        // afficher cette première vue (tirer le rideau)
        stage.show();

        // ajout d'un écouteur pour contrôler la sortie de l'application.
        stage.setOnCloseRequest(e -> {

            // pour éviter que la fenêtre principale ne se ferme dans tous les cas
            e.consume();

            // lors d'une demande de sortie, laisser le travail à faire au contrôleur
            ctrl.quitter();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}