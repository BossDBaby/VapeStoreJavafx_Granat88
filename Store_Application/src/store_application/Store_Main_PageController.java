package store_application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Store_Main_PageController implements Initializable {

    @FXML
    private TextField tfSearch;
    @FXML
    private ImageView btnNotif;
    @FXML
    private ImageView btnCart;
    @FXML
    private ImageView btnMail;
    @FXML
    private ImageView btnMenu;
    @FXML
    private ImageView imageHero;
    @FXML
    private AnchorPane productRecommend1;
    @FXML
    private ImageView imageRecommend1;
    @FXML
    private Label nameLabelRecommend1;
    @FXML
    private Label priceLabelRecommend1;
    @FXML
    private Label ratingLabelRecommend1;
    @FXML
    private AnchorPane productRecommend2;
    @FXML
    private ImageView imageRecommend2;
    @FXML
    private Label nameLabelRecommend2;
    @FXML
    private Label priceLabelRecommend2;
    @FXML
    private Label ratingLabelRecommend2;
    @FXML
    private AnchorPane productRecommend3;
    @FXML
    private ImageView imageRecommend3;
    @FXML
    private Label nameLabelRecommend3;
    @FXML
    private Label priceLabelRecommend3;
    @FXML
    private Label ratingLabelRecommend3;
    @FXML
    private AnchorPane productRecommend4;
    @FXML
    private ImageView imageRecommend4;
    @FXML
    private Label nameLabelRecommend4;
    @FXML
    private Label priceLabelRecommend4;
    @FXML
    private Label ratingLabelRecommend4;
    @FXML
    private AnchorPane productRecommend5;
    @FXML
    private ImageView imageRecommend5;
    @FXML
    private Label nameLabelRecommend5;
    @FXML
    private Label priceLabelRecommend5;
    @FXML
    private Label ratingLabelRecommend5;
    @FXML
    private AnchorPane productRecommend6;
    @FXML
    private ImageView imageRecommend6;
    @FXML
    private Label nameLabelRecommend6;
    @FXML
    private Label priceLabelRecommend6;
    @FXML
    private Label ratingLabelRecommend6;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRecommendedProducts(); // Load recommended products on initialization
        setupSearchListener(); // Setup search functionality
        setupButtonListeners(); // Setup button event listeners
    }

    // Method to load recommended products from the database
    private void loadRecommendedProducts() {
    // Koneksi ke database
    Database_Connection connectNow = new Database_Connection();
    Connection connectDB = connectNow.getConnection();

    // Query untuk mengambil produk yang direkomendasikan
    String query = "SELECT name AS product_name, price AS product_price, image_url AS product_image, rating AS product_rating FROM tabel_product LIMIT 6";

    try {
        PreparedStatement preparedStatement = connectDB.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        // Array untuk menyimpan komponen UI (untuk menghindari if-else berulang)
        Label[] nameLabels = {nameLabelRecommend1, nameLabelRecommend2, nameLabelRecommend3, nameLabelRecommend4, nameLabelRecommend5, nameLabelRecommend6};
        Label[] priceLabels = {priceLabelRecommend1, priceLabelRecommend2, priceLabelRecommend3, priceLabelRecommend4, priceLabelRecommend5, priceLabelRecommend6};
        Label[] ratingLabels = {ratingLabelRecommend1, ratingLabelRecommend2, ratingLabelRecommend3, ratingLabelRecommend4, ratingLabelRecommend5, ratingLabelRecommend6};
        ImageView[] imageViews = {imageRecommend1, imageRecommend2, imageRecommend3, imageRecommend4, imageRecommend5, imageRecommend6};

        int index = 0;
        while (rs.next() && index < 6) {
            // Ambil data dari database
            String productName = rs.getString("product_name");
            double productPrice = rs.getDouble("product_price");
            String productImage = rs.getString("product_image"); // Path gambar
            double productRating = rs.getDouble("product_rating");

            // Set data ke UI (Label dan ImageView)
            nameLabels[index].setText(productName);
            priceLabels[index].setText("$" + productPrice);
            ratingLabels[index].setText("Rating: " + productRating);
            imageViews[index].setImage(new Image("file:" + productImage)); // Load gambar

            index++;
        }

        // Jika ada komponen UI yang tidak diisi data, sembunyikan
        for (int i = index; i < 6; i++) {
            nameLabels[i].setVisible(false);
            priceLabels[i].setVisible(false);
            ratingLabels[i].setVisible(false);
            imageViews[i].setVisible(false);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // Method to search products based on user input
    private void setupSearchListener() {
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue);
        });
    }

    // Method to search products in the database based on query
    private void searchProducts(String query) {
        // Koneksi ke database
        Database_Connection connectNow = new Database_Connection();
        Connection connectDB = connectNow.getConnection();
        
        // Query untuk mencari produk berdasarkan nama
        String searchQuery = "SELECT product_name, product_price, product_image, product_rating FROM products WHERE product_name LIKE ?";
        
        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(searchQuery);
            preparedStatement.setString(1, "%" + query + "%");
            ResultSet rs = preparedStatement.executeQuery();

            // Reset the recommended product fields
            resetProductFields();

            int index = 1;
            while (rs.next()) {
                String productName = rs.getString("product_name");
                double productPrice = rs.getDouble("product_price");
                String productImage = rs.getString("product_image");
                double productRating = rs.getDouble("product_rating");

                // Setting the UI for each search result product
                if (index == 1) {
                    nameLabelRecommend1.setText(productName);
                    priceLabelRecommend1.setText("$" + productPrice);
                    ratingLabelRecommend1.setText("Rating: " + productRating);
                    imageRecommend1.setImage(new Image("file:" + productImage));
                } else if (index == 2) {
                    nameLabelRecommend2.setText(productName);
                    priceLabelRecommend2.setText("$" + productPrice);
                    ratingLabelRecommend2.setText("Rating: " + productRating);
                    imageRecommend2.setImage(new Image("file:" + productImage));
                } else if (index == 3) {
                    nameLabelRecommend3.setText(productName);
                    priceLabelRecommend3.setText("$" + productPrice);
                    ratingLabelRecommend3.setText("Rating: " + productRating);
                    imageRecommend3.setImage(new Image("file:" + productImage));
                } else if (index == 4) {
                    nameLabelRecommend4.setText(productName);
                    priceLabelRecommend4.setText("$" + productPrice);
                    ratingLabelRecommend4.setText("Rating: " + productRating);
                    imageRecommend4.setImage(new Image("file:" + productImage));
                } else if (index == 5) {
                    nameLabelRecommend5.setText(productName);
                    priceLabelRecommend5.setText("$" + productPrice);
                    ratingLabelRecommend5.setText("Rating: " + productRating);
                    imageRecommend5.setImage(new Image("file:" + productImage));
                } else if (index == 6) {
                    nameLabelRecommend6.setText(productName);
                    priceLabelRecommend6.setText("$" + productPrice);
                    ratingLabelRecommend6.setText("Rating: " + productRating);
                    imageRecommend6.setImage(new Image("file:" + productImage));
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to reset product fields
    private void resetProductFields() {
        // Reset all fields before adding new products
        nameLabelRecommend1.setText("");
        priceLabelRecommend1.setText("");
        ratingLabelRecommend1.setText("");
        imageRecommend1.setImage(null);

        nameLabelRecommend2.setText("");
        priceLabelRecommend2.setText("");
        ratingLabelRecommend2.setText("");
        imageRecommend2.setImage(null);

        nameLabelRecommend3.setText("");
        priceLabelRecommend3.setText("");
        ratingLabelRecommend3.setText("");
        imageRecommend3.setImage(null);

        nameLabelRecommend4.setText("");
        priceLabelRecommend4.setText("");
        ratingLabelRecommend4.setText("");
        imageRecommend4.setImage(null);

        nameLabelRecommend5.setText("");
        priceLabelRecommend5.setText("");
        ratingLabelRecommend5.setText("");
        imageRecommend5.setImage(null);

        nameLabelRecommend6.setText("");
        priceLabelRecommend6.setText("");
        ratingLabelRecommend6.setText("");
        imageRecommend6.setImage(null);
    }

    // Method to handle button clicks for notifications, cart, and mail
    private void setupButtonListeners() {
        // Menu Button
        btnMenu.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Store_Menu.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.setTitle("Store Menu");
                stage.show();
                closeCurrentStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Notification Button
        btnNotif.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Store_Notif.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.setTitle("Notifications");
                stage.show();
                closeCurrentStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Cart Button
        btnCart.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Store_Cart.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.setTitle("Shopping Cart");
                stage.show();
                closeCurrentStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Mail Button
        btnMail.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Store_Inbox.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.setTitle("Inbox");
                stage.show();
                closeCurrentStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Helper method to close the current stage (current window)
    private void closeCurrentStage() {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        currentStage.close();
    }
}
