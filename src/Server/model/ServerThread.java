package Server.model;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable {
    private int serverPort;
    private TextArea textArea;
    private ServerSocket serverSocket;
    private boolean shouldRun = true;

    public ServerThread(int port) {
        this.serverPort = port;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void run() {
        Platform.runLater(() -> textArea.appendText("Server started\n"));
        System.out.println("Server started");

        Map<ClientThread, Side> connections = new HashMap<>();
        try {
            serverSocket = new ServerSocket(serverPort);
            while (shouldRun) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket, connections);
                clientThread.setTextArea(textArea);
                new Thread(clientThread).start();
                connections.put(clientThread, Side.DEFAULT);
            }
            serverSocket.close();
        } catch (IOException e) {
            Platform.runLater(() -> textArea.appendText("Server stopping\n"));
            System.out.println("Server stopping");
        } finally {
            Platform.runLater(() -> textArea.appendText("Server stopped\n"));
            System.out.println("Server stopped");
        }
    }

    public void startServer() {
        this.shouldRun = true;
        new Thread(this).start();
    }

    public void stopServer() {
        this.shouldRun = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
