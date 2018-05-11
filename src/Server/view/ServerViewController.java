package Server.view;

import Server.model.ServerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ServerViewController {
    @FXML
    private Button startServerButton;
    @FXML
    private Button stopServerButton;
    @FXML
    private TextArea serverLog;
    private ServerThread serverThread;

    @FXML
    private void initialize() {
        serverThread = new ServerThread(9092);
        serverThread.setTextArea(serverLog);
        stopServerButton.setDisable(true);
    }

    @FXML
    private void startServer() {
        startServerButton.setDisable(true);
        stopServerButton.setDisable(false);
        serverThread.startServer();
    }

    @FXML
    private void stopServer() {
        stopServerButton.setDisable(true);
        startServerButton.setDisable(false);
        serverThread.stopServer();
    }
}
