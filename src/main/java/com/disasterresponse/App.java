// File: src/main/java/com/disasterresponse/App.java
package com.disasterresponse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
        Parent root = loader.load();

        // Set the scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set the stage with the scene and other configurations
        primaryStage.setTitle("Disaster Response System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);  // Prevent resizing for simplicity
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
