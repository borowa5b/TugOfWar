package Client.model;

import Client.view.GameViewController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private static Client client;
    private ProgressBar progressBar;
    private PrintWriter output;
    private Label endGameText;
    private String address;

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }

        return client;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setEndGameText(Label endGameText) {
        this.endGameText = endGameText;
    }

    @Override
    public void run() {
        try (Socket clientSocket = new Socket(address, 9092);
             BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            System.out.println("Connected");
            output.println("DEFAULT");

            loop:
            while (true) {
                String userInput = input.readLine();
                System.out.println("client:" + userInput);
                switch (userInput) {
                    case "End":
                        System.out.println("Closing client");
                        break loop;
                    case "Won":
                        Platform.runLater(() -> {
                            endGameText.setText("Wygrales!");
                            Platform.runLater(() -> progressBar.setProgress(-0.005));
                            GameViewController.setShouldCheck(false);
                        });
                        break;
                    case "Lose":
                        Platform.runLater(() -> {
                            endGameText.setText("Przegrales!");
                            Platform.runLater(() -> progressBar.setProgress(-0.005));
                            GameViewController.setShouldCheck(false);
                        });
                        break;
                    default:
                        Platform.runLater(() -> progressBar.setProgress(Double.parseDouble(userInput)));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(String msg) {
        output.println(msg);
    }

    public void closeConnection() {
        sendToServer("End");
        output.close();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void startConnection() {
        new Thread(client).start();
    }
}
