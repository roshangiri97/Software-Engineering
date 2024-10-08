package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    protected void handleResetPasswordAction() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            messageLabel.setText("Email field cannot be empty.");
            messageLabel.setVisible(true);
        } else if (!isValidEmail(email)) {
            messageLabel.setText("Invalid email format.");
            messageLabel.setVisible(true);
        } else {
            // Simulate sending a reset password email
            sendPasswordResetEmail(email);
            messageLabel.setText("Password reset link sent to your email.");
            messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            messageLabel.setVisible(true);

            // Redirect back to login page after a delay
            redirectToLoginPage();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private void sendPasswordResetEmail(String email) {
        // Simulate sending an email
        System.out.println("Password reset link sent to " + email);
    }

private void redirectToLoginPage() {
    PauseTransition pause = new PauseTransition(Duration.seconds(2)); // 2-second delay
    pause.setOnFinished(event -> {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading login page.");
        }
    });
    pause.play();
}
}
