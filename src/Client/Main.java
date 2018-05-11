package Client;

import Client.model.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/SideChooser.fxml"));
        primaryStage.setTitle("Tug of War - Wybierz stronę");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Client client = Client.getInstance();
        primaryStage.setOnCloseRequest(event -> client.closeConnection());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
