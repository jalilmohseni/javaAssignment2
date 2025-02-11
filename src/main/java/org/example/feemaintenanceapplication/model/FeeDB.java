package org.example.feemaintenanceapplication.model;

import org.example.feemaintenanceapplication.utils.DbConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeDB {

    /**
     * Establishes and returns a connection to the database.
     */
    public static Connection getConnection() throws SQLException {
        String url = DbConfig.getProperty("url");
        String user = DbConfig.getProperty("user");
        String password = DbConfig.getProperty("password");

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves all records from the Fees table.
     * @return List of Fee objects
     */
    public static List<Fee> getAllFees() {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT * FROM fees";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Fee fee = new Fee(
                        rs.getString("feeid"),
                        rs.getString("feename"),
                        rs.getDouble("feeamt"),
                        rs.getString("feedesc")
                );
                fees.add(fee);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching fees from database: " + e.getMessage());
        }
        return fees;
    }

    /**
     * Checks if a Fee ID already exists in the database.
     * @param feeId The Fee ID to check
     * @return true if Fee ID exists, false otherwise
     */
    public static boolean feeExists(String feeId) {
        String sql = "SELECT COUNT(*) FROM fees WHERE feeid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if Fee ID exists: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a Fee record from the database.
     * @param feeId The Fee ID of the record to be deleted
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteFee(String feeId) {
        String sql = "DELETE FROM fees WHERE feeid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, feeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting Fee ID " + feeId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new Fee record to the database.
     * @param fee The Fee object containing data to be added
     * @return true if the addition was successful, false otherwise
     */
    public static boolean addFee(Fee fee) {
        String sql = "INSERT INTO fees (feeid, feename, feeamt, feedesc) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fee.getFeeId());
            pstmt.setString(2, fee.getFeeName());
            pstmt.setDouble(3, fee.getFeeAmount());
            pstmt.setString(4, fee.getFeeDesc());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding fee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing Fee record in the database.
     * @param fee The Fee object containing updated data
     * @return true if update was successful, false otherwise
     */
    public static boolean updateFee(Fee fee) {
        String sql = "UPDATE fees SET feename = ?, feeamt = ?, feedesc = ? WHERE feeid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fee.getFeeName());
            pstmt.setDouble(2, fee.getFeeAmount());
            pstmt.setString(3, fee.getFeeDesc());
            pstmt.setString(4, fee.getFeeId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating fee: " + e.getMessage());
            return false;
        }
    }
}
