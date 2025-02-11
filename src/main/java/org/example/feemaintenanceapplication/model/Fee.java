package org.example.feemaintenanceapplication.model;

public class Fee {
    private String feeId;
    private String feeName;
    private double feeAmount;
    private String feeDesc;

    // Constructor
    public Fee(String feeId, String feeName, double feeAmount, String feeDesc) {
        this.feeId = feeId;
        this.feeName = feeName;
        this.feeAmount = feeAmount;
        this.feeDesc = feeDesc;
    }

    // Getter Methods
    public String getFeeId() { return feeId; }
    public String getFeeName() { return feeName; }
    public double getFeeAmount() { return feeAmount; }
    public String getFeeDesc() { return feeDesc; }

    // Add Missing Setter Methods
    public void setFeeName(String feeName) { this.feeName = feeName; }
    public void setFeeAmount(double feeAmount) { this.feeAmount = feeAmount; }
    public void setFeeDesc(String feeDesc) { this.feeDesc = feeDesc; }
}
