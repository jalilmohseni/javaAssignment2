package org.example.feemaintenanceapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.feemaintenanceapplication.model.Fee;
import org.example.feemaintenanceapplication.model.FeeDB;

import java.io.IOException;
import java.util.List;

public class FeeController {

    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnEdit;
    @FXML private TableColumn<Fee, String> colDescription;
    @FXML private TableColumn<Fee, Double> colFeeAmount;
    @FXML private TableColumn<Fee, String> colID;
    @FXML private TableColumn<Fee, String> colName;
    @FXML private Label lblfeesview;
    @FXML private TableView<Fee> tvFees;

    private ObservableList<Fee> feeList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        setupTable();
        loadFees();

        btnAdd.setOnAction(event -> openFeeForm(null));
        btnEdit.setOnAction(event -> editSelectedFee());
        btnDelete.setOnAction(event -> deleteSelectedFee());
    }

    /**
     * Configures TableView columns and binds them to Fee properties.
     */
    private void setupTable() {
        colID.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFeeId()));
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFeeName()));
        colFeeAmount.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFeeAmount()));
        colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFeeDesc()));

        tvFees.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvFees.setItems(feeList);
    }

    /**
     * Loads fees from the database and updates the TableView.
     */
    private void loadFees() {
        List<Fee> fees = FeeDB.getAllFees();
        feeList.setAll(fees);
        tvFees.refresh();
    }

    /**
     * Opens the Fee Form for adding or editing a fee.
     * @param fee The Fee object to edit, or null to add a new fee.
     */
    private void openFeeForm(Fee fee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/feemaintenanceapplication/fee-form.fxml"));
            Parent root = loader.load();

            FeeFormController controller = loader.getController();
            if (fee != null) {
                controller.setFeeData(fee);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(fee == null ? "Add Fee" : "Edit Fee");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadFees(); // Refresh TableView after form closes
        } catch (IOException e) {
            System.err.println("Error: Unable to open Fee Form.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the Edit button action.
     */
    private void editSelectedFee() {
        Fee selectedFee = tvFees.getSelectionModel().getSelectedItem();
        if (selectedFee != null) {
            openFeeForm(selectedFee);
        } else {
            showAlert("Warning", "Please select a fee to edit.");
        }
    }

    /**
     * Handles the Delete button action.
     */
    private void deleteSelectedFee() {
        Fee selectedFee = tvFees.getSelectionModel().getSelectedItem();
        if (selectedFee != null) {
            if (confirmDialog("Delete Fee", "Are you sure you want to delete this fee?")) {
                FeeDB.deleteFee(selectedFee.getFeeId());
                loadFees(); // Refresh after deletion
            }
        } else {
            showAlert("Warning", "Please select a fee to delete.");
        }
    }

    /**
     * Displays an alert message to the user.
     * @param title Alert title
     * @param message Alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog for deletion.
     * @param title Dialog title
     * @param message Dialog message
     * @return true if the user confirms, false otherwise.
     */
    private boolean confirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
