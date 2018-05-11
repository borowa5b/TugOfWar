package Client.view;

import Client.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SideChooserController {
    private Client client;
    @FXML
    private Button leftSideButton;
    @FXML
    private Button rightSideButton;
    @FXML
    private TextField addressField;

 /*   @FXML
    private void initialize() {
        client = Client.getInstance();
    }*/

    @FXML
    private void leftSide() {
        Stage currentStage = (Stage) leftSideButton.getScene().getWindow();
        currentStage.close();

        openGameView();
        client.sendToServer("LEFT");
    }

    @FXML
    private void rightSide() {
        Stage currentStage = (Stage) rightSideButton.getScene().getWindow();
        currentStage.close();

        openGameView();
        client.sendToServer("RIGHT");
    }

    private void openGameView() {
        client = Client.getInstance();
        client.setAddress(addressField.getText());
        client.startConnection();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("GameView.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Tug of War");
            stage.setScene(new Scene(root));
            stage.show();

            root.requestFocus();

            stage.setOnCloseRequest(we -> client.closeConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
