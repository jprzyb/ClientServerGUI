package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Logger.addLog("========= Application START =========");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("I/O files in LAN!");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            if(Server.xyz != null) Server.abort();
            Logger.addLog("===== Application STOP =====");
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}