package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TempTests extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wybierz katalog");

        ComboBox<String> directoryComboBox = new ComboBox<>();
        directoryComboBox.getItems().addAll(
                "Zapisz w katalogu x",
                "Zapisz w katalogu y",
                "Wybierz katalog"
        );

        directoryComboBox.setValue("Wybierz katalog");

        // Dodajemy ChangeListener do ComboBox
        directoryComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("Wybrany katalog: " + newValue);
                // Tutaj możesz dodać kod reagujący na wybór katalogu
            }
        });

        VBox vBox = new VBox(directoryComboBox);
        Scene scene = new Scene(vBox, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
