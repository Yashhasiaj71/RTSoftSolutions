package com.example.rtsoftsolutions.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeesTransaction {
    public String studentId;
    public String studentName;
    public int amountPaid;
    public String paymentMode;
    public String transactionDate;
    public String transactionId;

    public FeesTransaction() {} // Needed for Firebase

    public FeesTransaction(String studentId, String studentName, int amountPaid, String paymentMode) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.transactionDate = new Date().toString();
        this.transactionId = "TXN_" + System.currentTimeMillis();
    }

    // Getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getAmountPaid() { return amountPaid; }
    public void setAmountPaid(int amountPaid) { this.amountPaid = amountPaid; }
    
    // Alias for getAmountPaid() for PDF generation
    public int getAmount() { return amountPaid; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
    
    // Formatted date for PDF generation
    public String getDate() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(transactionDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return transactionDate;
        }
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
