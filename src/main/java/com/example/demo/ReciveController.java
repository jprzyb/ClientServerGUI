package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class ReciveController {
    public static final int PORT = 12345;
    private Server server;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label dataLabel;
    @FXML
    ListView filesRecived;
    @FXML
    VBox box;
    @FXML
    Label infoLabel;

    @FXML
    public void initialize() throws UnknownHostException{
        server = new Server();
        dataLabel.setText("SERVER\n"+ InetAddress.getLocalHost().getHostName() + ":" +PORT + "\nor\n" + InetAddress.getLocalHost().getHostAddress()+ ":" +PORT);
    }

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void onStartButtonClick(ActionEvent event) throws IOException {
        box.setStyle("-fx-background-color: #c4b858");
        server.start();
    }

    @FXML
    void onStopButtonClick(ActionEvent event) throws IOException {
        box.setStyle("-fx-background-color: #d44b42");
        server.stop();
    }
}
