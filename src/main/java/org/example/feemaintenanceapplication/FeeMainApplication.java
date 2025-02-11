package org.example.feemaintenanceapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.feemaintenanceapplication.model.FeeDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main application class for the Fee Maintenance Application.
 * This class initializes and displays the main application window.
 * Author: Jalil
 * Date: February 2025
 */
public class FeeMainApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Test database connection
            Connection conn = FeeDB.getConnection();
            System.out.println("Database connection successful!");
            conn.close();

            // Load UI
            FXMLLoader fxmlLoader = new FXMLLoader(FeeMainApplication.class.getResource("/org/example/feemaintenanceapplication/fees-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Fee Maintenance App");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error: Unable to load FXML file.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
