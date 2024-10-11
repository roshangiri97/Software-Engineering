package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField verificationCodeField;

    @FXML
    private Label verificationCodeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label newPasswordLabel;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Label confirmNewPasswordLabel;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private Button verifyButton;

    @FXML
    private Button resetButton;

    private String verificationCode;

    @FXML
    protected void handleResetPasswordAction() {
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();

        if (email.isEmpty() || username.isEmpty()) {
            messageLabel.setText("Email and username fields cannot be empty.");
            messageLabel.setVisible(true);
        } else if (!isValidEmail(email)) {
            messageLabel.setText("Invalid email format.");
            messageLabel.setVisible(true);
        } else if (!usernameExistsInDatabase(username)) {
            messageLabel.setText("Username does not exist.");
            messageLabel.setVisible(true);
        } else if (!emailMatchesUsernameInDatabase(email, username)) {
            messageLabel.setText("Email does not match the username.");
            messageLabel.setVisible(true);
        } else {
            // Simulate sending a reset password email
            verificationCode = genVerificationCode();
            sendVerificationCode(email, verificationCode);
            messageLabel.setText("Verification code sent to your email.");
            messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            messageLabel.setVisible(true);
            verificationCodeLabel.setVisible(true);
            verificationCodeField.setVisible(true);
            verifyButton.setVisible(true);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
private boolean usernameExistsInDatabase(String username) {
    try (Connection connection = DatabaseConnection.getConnection(); 
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE Username = ?")) {

        statement.setString(1, username);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Username exists in database");
                return true;
            } else {
                System.out.println("Username does not exist in database");
                return false;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error checking username existence: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

private boolean emailMatchesUsernameInDatabase(String email, String username) {
    try (Connection connection = DatabaseConnection.getConnection(); 
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE Username = ? AND Email = ?")) {

        statement.setString(1, username);
        statement.setString(2, email);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Email matches username in database");
                return true;
            } else {
                System.out.println("Email does not match username in database");
                return false;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error checking email and username match: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

    private void sendPasswordResetEmail(String email) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "2525");
        props.put("mail.smtp.auth", "false");

        Session session = Session.getInstance(props, null);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your-email@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset");
            message.setText("Your password reset link is: ...");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle email errors
        }
    }

    // Generate a random verification code
    public String genVerificationCode() {
        // The character pool
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "1234567890"
                + "abcdefghijklmnopqrstuvwxyz"
                + "!@#$%&*-+=?";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

  // Send the verification code via email
    private void sendVerificationCode(String email, String verificationCode) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "2525");
        props.put("mail.smtp.auth", "false");

        Session session = Session.getInstance(props, null);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your-email@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + verificationCode);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle email errors
        }
    }
    // Verify the verification code
    public boolean verifyVerificationCode(String verificationCode, String userCode) {
        return verificationCode.equals(userCode);
    }

    @FXML
    protected void handleVerifyAction() {
        String userCode = verificationCodeField.getText().trim();
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            messageLabel.setText("Username field cannot be empty.");
            messageLabel.setVisible(true);
        } else if (!verifyVerificationCode(verificationCode, userCode)) {
            messageLabel.setText("Invalid verification code.");
            messageLabel.setVisible(true);
        } else {
            // Verification code is correct
            newPasswordLabel.setVisible(true);
            newPasswordField.setVisible(true);
            confirmNewPasswordLabel.setVisible(true);
            confirmNewPasswordField.setVisible(true);
            resetButton.setVisible(true);
        }
    }

  @FXML
protected void handleResetAction(ActionEvent event) {
    String newPassword = newPasswordField.getText().trim();
    String confirmPassword = confirmNewPasswordField.getText().trim();

    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
        messageLabel.setText("Password fields cannot be empty.");
        messageLabel.setVisible(true);
    } else if (!newPassword.equals(confirmPassword)) {
        messageLabel.setText("Passwords do not match.");
        messageLabel.setVisible(true);
    } else {
        // Update the password in the database
        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET Password = ? WHERE Username = ?")) {

            statement.setString(1, newPassword);
            statement.setString(2, usernameField.getText().trim());
            statement.executeUpdate();

            messageLabel.setText("Password reset successfully.");
            messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            messageLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
                    Stage stage = (Stage) resetButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error loading login page.");
                }
            });
            pause.play();
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
}