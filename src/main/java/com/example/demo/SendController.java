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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    }

    @FXML
    void sendFiles(){
        if(!validateProperties()) return;
        infoLabel.setText("Sending!");
        try(Socket socket = new Socket(InetAddress.getByName(hostName.getText()),PORT)){

            for(File f : files){
                FileInputStream fileInputStream = new FileInputStream(f.getAbsolutePath());
                OutputStream outputStream = socket.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                String[] x = f.getAbsolutePath().split("/");
                infoLabel.setText(x[x.length-1] + " successfully send!");

            }
        } catch (IOException e) {
            e.printStackTrace();
            infoLabel.setText("Something went wrong!");
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
