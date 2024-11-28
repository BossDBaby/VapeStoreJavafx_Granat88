package store_application;

import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


public class Login_Page {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPassField;
    @FXML
    private Label invalidLoginLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signUpHyperlink;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private Button backToHomePageButton;  
    
    public void loginButtonOnAction(){
        invalidLoginLabel.setText("Invalid Login! Please try again. ");
    }
    
    public void backToHomePageButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) backToHomePageButton.getScene().getWindow();
        stage.close();
    }

    private void validateLogin() {
        // Inisialisasi koneksi ke database
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();

        // Pastikan username dan password tidak kosong sebelum mencoba login
        String username = usernameTextField.getText();
        String password = passwordPassField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            invalidLoginLabel.setText("Username or Password cannot be empty.");
            invalidLoginLabel.setStyle("-fx-text-fill: red;");
            return; // Stop execution
        }

        // Kueri login dengan parameterized query untuk menghindari SQL Injection
        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = ? AND password = ?";

        try {
            // Gunakan PreparedStatement
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                int count = queryResult.getInt(1);
                if (count == 1) {
                    invalidLoginLabel.setText("Login successful!");
                    invalidLoginLabel.setStyle("-fx-text-fill: green;");

                    // Tampilkan alert atau buka halaman baru
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Login Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Welcome, " + username + "!");
                    successAlert.showAndWait();
                } else {
                    invalidLoginLabel.setText("Invalid username or password.");
                    invalidLoginLabel.setStyle("-fx-text-fill: red;");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            invalidLoginLabel.setText("Database connection error.");
            invalidLoginLabel.setStyle("-fx-text-fill: red;");
    }
}

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Load SignUpPage.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sign_Up_Page.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            // Set up a new Stage for the Sign-Up page
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(signUpRoot));
            stage.show();

            // Close the current login window
            Stage currentStage = (Stage) signUpHyperlink.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert if the FXML file cannot be loaded
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Failed to load the Sign-Up page. Please try again later.");
            errorAlert.showAndWait();
        }
    }
}

