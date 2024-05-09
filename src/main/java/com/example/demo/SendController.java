package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SendController {
    public static final int PORT = 12345;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    ListView fileList;
    @FXML
    TextField hostName;
    @FXML
    Label infoLabel;
    List<File> files;

    @FXML
    public void initialize(){
        files = new ArrayList<>();
    }
    @FXML
    void fileChooser(){
        FileChooser fileChooser = new FileChooser();
        files = fileChooser.showOpenMultipleDialog(null);
        fileList.getItems().setAll(files);
        Logger.addLog("Added files: " + files);
    }

    @FXML
    void sendFiles(){
        if(!validateProperties()) return;
        infoLabel.setText("Sending!");
        Logger.addLog("Sending files.");

        try {
            Socket socket = new Socket(hostName.getText(), PORT);
            Logger.addLog("Connected to server ("+socket+").");

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            for(File f : files){
                // file name sending
                dataOutputStream.flush();
                String[] fileName = f.getAbsolutePath().split("\\\\");
                String fileName2 = fileName[fileName.length-1];
                dataOutputStream.writeUTF(fileName2);
                Logger.addLog("File name send: " + fileName2);

                // file sending
                dataOutputStream.flush();
                FileInputStream fileInputStream = new FileInputStream(f.getAbsolutePath());
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
                Logger.addLog("File send: " + fileName2);
            }

            socket.close();
        } catch (IOException e) {
            Logger.addLog("Something went wrong while sending!\n" + e.getMessage());
        }


    }

    private boolean validateProperties() {
        infoLabel.setText("");
        if(fileList.getItems().isEmpty() && hostName.getText().isEmpty()){
            infoLabel.setText("Choose files and enter a host!");
            return false;
        } else if (fileList.getItems().isEmpty()) {
            infoLabel.setText("Choose files!");
            return false;
        } else if (hostName.getText().isEmpty()) {
            infoLabel.setText("Enter a host!");
            return false;
        }

        try {
            InetAddress host = InetAddress.getByName(hostName.getText());
            if (host.getHostAddress().isEmpty()) return false;
        } catch (UnknownHostException e) {
            infoLabel.setText("Unknown host or server is off!");
            Logger.addLog("Validation: Unknown host or server is off!");
            return false;
        }

        return true;
    }

    @FXML
    void onBackButtonClick(ActionEvent event) throws IOException{

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
