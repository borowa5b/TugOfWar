package Client.view;

import Client.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameViewController {
    @FXML
    private Label endGameText;
    @FXML
    private ProgressBar progressBar;
    private Client client;
    private static boolean shouldCheck;

    @FXML
    private void initialize() {
        client = Client.getInstance();
        client.setProgressBar(progressBar);
        client.setEndGameText(endGameText);
        shouldCheck = true;
    }

    @FXML
    private void keyAction(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.SPACE && shouldCheck) {
            client.sendToServer(String.valueOf(progressBar.getProgress()));
        }
    }

    public static void setShouldCheck(boolean shouldCheck) {
        GameViewController.shouldCheck = shouldCheck;
    }
}
