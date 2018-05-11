package Server.model;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientThread implements Runnable {
    private Socket clientSocket;
    private Map<ClientThread, Side> connections;
    private PrintWriter output;
    private TextArea textArea;

    ClientThread(Socket clientSocket, Map<ClientThread, Side> connections) {
        this.clientSocket = clientSocket;
        this.connections = connections;
    }

    void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            String userInput;
            loop:
            while (true) {
                userInput = input.readLine();
                System.out.println("Received: " + userInput);
                String finalUserInput = userInput;
                Platform.runLater(() -> textArea.appendText("Received: " + finalUserInput + "\n"));
                switch (userInput) {
                    case "End":
                        sendToThisClient("End");
                        break loop;
                    case "DEFAULT":
                        connections.put(this, Side.DEFAULT);
                        break;
                    case "LEFT":
                        connections.put(this, Side.LEFTSIDE);
                        break;
                    case "RIGHT":
                        connections.put(this, Side.RIGHTSIDE);
                        break;
                    default:
                        double progressValue = Double.parseDouble(userInput);
                        if (connections.get(this) == Side.LEFTSIDE) {
                            progressValue += 0.01;
                            if (progressValue >= 1) {
                                sendToOtherClients("Lose");
                                sendToThisClient("Won");
                            }
                        } else if (connections.get(this) == Side.RIGHTSIDE) {
                            progressValue -= 0.01;
                            if (progressValue <= 0) {
                                sendToOtherClients("Lose");
                                sendToThisClient("Won");
                            }
                        }
                        sendToThisClient(String.valueOf(progressValue));
                        sendToOtherClients(String.valueOf(progressValue));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Closed client thread");
            Platform.runLater(() -> textArea.appendText("Closed client thread\n"));
            output.close();
        }
    }

    private void sendToThisClient(String msg) {
        output.println(msg);
    }

    private void sendToOtherClients(String msg) {
        for (ClientThread client : connections.keySet()) {
            if (client != this) {
                client.sendToThisClient(msg);
            }
        }
    }
}
