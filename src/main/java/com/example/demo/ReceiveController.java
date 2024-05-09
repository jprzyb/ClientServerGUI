package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class ReceiveController {
    public static final int PORT = 12345;
    private Server server;

    @FXML
    Label dataLabel;
    @FXML
    ListView<String> guiLogs;
    @FXML
    VBox box;
    @FXML
    Label infoLabel;
    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    ComboBox<String> destinationPicker;
    @FXML
    Label destinationlabel;

    @FXML
    public void initialize() throws UnknownHostException{
        server = new Server(this);
        dataLabel.setText("SERVER\n"+ InetAddress.getLocalHost().getHostName() + ":" +PORT + "\nor\n" + InetAddress.getLocalHost().getHostAddress()+ ":" +PORT);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        destinationPicker.getItems().addAll("Downloads", "Documents","This program files", "Other");
        destinationlabel.setText(server.destination);
        destinationPicker.valueProperty().addListener((observableValue, s, t1) -> {
//
            if(t1.equals("Other")){
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Pick directory");
                File f = directoryChooser.showDialog(null);
                if(f != null){
                    String dir = f.getAbsolutePath() + "\\";
                    server.setDestination(dir);
                    destinationlabel.setText(dir);
                }
            } else if (t1.equals("This program files")) {
                String dir = "received_files/";
                server.setDestination(dir);
                destinationlabel.setText(dir);
            } else {
                String dir = System.getProperty("user.home") + "\\" + t1+ "\\";
                server.setDestination(dir);
                destinationlabel.setText(dir);
            }
        });
    }

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void onStartButtonClick() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        destinationPicker.setDisable(true);
        server.start();
        Logger.addLog("Server started.");
    }

    @FXML
    void onStopButtonClick() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        destinationPicker.setDisable(false);
        server.stop();
        Logger.addLog("Server stopped.");
    }
    void addElementToGuiLogs(String msg){
        Platform.runLater(() -> guiLogs.getItems().addAll(msg));
    }
}
