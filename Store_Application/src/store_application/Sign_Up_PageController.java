package store_application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Sign_Up_PageController implements Initializable {

    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField11;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField setpasswordPassField;
    @FXML
    private Button registerButton;
    @FXML
    private TextField emailTextField1;
    @FXML
    private Hyperlink loginHyperlink;
    @FXML
    private Button backToHomePageButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Event handler untuk tombol register
        registerButton.setOnAction(event -> handleRegisterButton());

        // Event handler untuk tombol back to homepage
        backToHomePageButton.setOnAction(event -> goBackToHomePage());
    }

    private void handleRegisterButton() {
        if (validateInput()) {
            String username = usernameTextField.getText();
            String email = emailTextField1.getText();

            // Memeriksa apakah username atau email sudah ada di database
            if (isUserExists(username, email)) {
                showAlert("Error", "Username or email already exists!", Alert.AlertType.ERROR);
            } else {
                registerUser();
            }
        }
    }

    // Navigasi kembali ke halaman utama
    private void goBackToHomePage() {
        try {
            // Memuat FXML halaman utama
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Store_Main_Page.fxml"));
            AnchorPane homePage = loader.load();

            // Membuat scene baru dan menampilkan halaman utama
            Stage currentStage = (Stage) backToHomePageButton.getScene().getWindow();
            Scene scene = new Scene(homePage);
            currentStage.setScene(scene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load homepage.", Alert.AlertType.ERROR);
        }
    }

    // Validasi input pengguna
    private boolean validateInput() {
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField11.getText();
        String username = usernameTextField.getText();
        String password = setpasswordPassField.getText();
        String email = emailTextField1.getText();

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.WARNING);
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showAlert("Validation Error", "Invalid email address!", Alert.AlertType.WARNING);
            return false;
        }

        if (password.length() < 8) {
            showAlert("Validation Error", "Password must be at least 8 characters long!", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    // Mengecek apakah username atau email sudah ada di database
    private boolean isUserExists(String username, String email) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        
        // Mendapatkan koneksi menggunakan Database_Connection
        Database_Connection connectNow = new Database_Connection();
        try (Connection connection = connectNow.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Error checking user existence.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        return false;
    }

    // Mendaftarkan pengguna baru
    private void registerUser() {
        String username = usernameTextField.getText();
        String email = emailTextField1.getText();
        String password = setpasswordPassField.getText();
        String fullName = firstnameTextField.getText() + " " + lastnameTextField11.getText();
        String role = "customer";

        String query = "INSERT INTO users (username, email, password_hash, full_name, role) VALUES (?, ?, ?, ?, ?)";

        // Mendapatkan koneksi menggunakan Database_Connection
        Database_Connection connectNow = new Database_Connection();
        try (Connection connection = connectNow.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password); // Gantilah dengan hashed password jika perlu
            preparedStatement.setString(4, fullName);
            preparedStatement.setString(5, role);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "User registered successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to register user.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Error during registration process.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Menampilkan alert dengan pesan tertentu
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
