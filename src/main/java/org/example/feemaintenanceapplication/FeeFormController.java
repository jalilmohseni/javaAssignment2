package org.example.feemaintenanceapplication;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.feemaintenanceapplication.model.Fee;
import org.example.feemaintenanceapplication.model.FeeDB;

public class FeeFormController {

    @FXML private TextField txtFeeID;
    @FXML private TextField txtFeeName;
    @FXML private TextField txtFeeAmount;
    @FXML private TextArea txtFeeDesc;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private Fee currentFee; // Stores the fee being edited (null if adding new)

    @FXML
    void initialize() {
        btnCancel.setOnAction(event -> closeForm()); // Closes the form when Cancel is clicked
        btnSave.setOnAction(event -> saveFee()); // Saves the fee when Save is clicked
    }

    /**
     * Populates the form fields based on the provided Fee object.
     * If fee is null, it means a new record is being added.
     */
    public void setFeeData(Fee fee) {
        this.currentFee = fee;
        if (fee != null) { // Editing an existing fee
            txtFeeID.setText(fee.getFeeId());
            txtFeeID.setDisable(true); // Fee ID should not be editable during edit
            txtFeeName.setText(fee.getFeeName());
            txtFeeAmount.setText(String.valueOf(fee.getFeeAmount()));
            txtFeeDesc.setText(fee.getFeeDesc());
        } else { // Adding a new fee
            txtFeeID.setDisable(false);
            txtFeeID.clear();
            txtFeeName.clear();
            txtFeeAmount.clear();
            txtFeeDesc.clear();
        }
    }

    /**
     * Validates the form fields and saves the fee to the database.
     */
    private void saveFee() {
        String feeId = txtFeeID.getText().trim();
        String feeName = txtFeeName.getText().trim();
        String feeDesc = txtFeeDesc.getText().trim();

        // ✅ Fee ID Validation (Length & Format)
        if (feeId.isEmpty()) {
            showAlert("Error", "Fee ID cannot be empty.");
            return;
        }
        if (feeId.length() > 10) {
            showAlert("Error", "Fee ID cannot exceed 10 characters.");
            return;
        }
        if (!feeId.matches("^[a-zA-Z0-9]+$")) { // Only alphanumeric allowed
            showAlert("Error", "Fee ID must contain only letters and numbers.");
            return;
        }

        // ✅ Fee Name Validation
        if (feeName.isEmpty()) {
            showAlert("Error", "Fee Name cannot be empty.");
            return;
        }

        // ✅ Fee Amount Validation (Must be a positive number)
        double feeAmount;
        try {
            feeAmount = Double.parseDouble(txtFeeAmount.getText().trim());
            if (feeAmount < 0) {
                showAlert("Error", "Fee Amount cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount. Please enter a valid number.");
            return;
        }

        // ✅ Check for Duplicate Fee ID When Adding a New Fee
        if (currentFee == null) {
            if (FeeDB.feeExists(feeId)) {
                showAlert("Error", "Fee ID already exists. Please enter a unique Fee ID.");
                return;
            }
            Fee newFee = new Fee(feeId, feeName, feeAmount, feeDesc);
            boolean success = FeeDB.addFee(newFee);
            if (!success) {
                showAlert("Error", "Failed to add fee. Please try again.");
                return;
            }
            showAlert("Success", "Fee added successfully!");
        } else { // ✅ If Editing, Update the Existing Fee
            currentFee.setFeeName(feeName);
            currentFee.setFeeAmount(feeAmount);
            currentFee.setFeeDesc(feeDesc);
            boolean success = FeeDB.updateFee(currentFee);
            if (!success) {
                showAlert("Error", "Failed to update fee.");
                return;
            }
            showAlert("Success", "Fee updated successfully!");
        }

        closeForm(); // ✅ Close form after successful save
    }


    /**
     * Closes the form window.
     */
    private void closeForm() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert dialog with the provided title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
