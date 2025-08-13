package com.example.rtsoftsolutions;

public class Student {
    public String name;
    public String email;
    public String phoneNo;
    public String aadharNo;
    public String address;
    public String fatherName;
    public String motherName;
    public int totalFees;
    public int paidFees;
    public int remainingFees;
    public String birthDate;
    public String selectedCourseId;
    public String selectedCourseName;
    public String selectedBatchName;

    public Student() {} // Needed for Firebase

    public Student(String name, String email, String phoneNo, String aadharNo,
                   String address, String fatherName, String motherName, int totalFees,
                   String birthDate, String selectedCourseId, String selectedCourseName, String selectedBatchName) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.aadharNo = aadharNo;
        this.address = address;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.totalFees = totalFees;
        this.paidFees = 0; // Initialize paid fees to 0
        this.remainingFees = totalFees; // Initialize remaining fees to total fees
        this.birthDate = birthDate;
        this.selectedCourseId = selectedCourseId;
        this.selectedCourseName = selectedCourseName;
        this.selectedBatchName = selectedBatchName;
    }

    // Getters and setters for fees management
    public int getTotalFees() { return totalFees; }
    public void setTotalFees(int totalFees) { 
        this.totalFees = totalFees; 
        updateRemainingFees();
    }
    
    public int getPaidFees() { return paidFees; }
    public void setPaidFees(int paidFees) { 
        this.paidFees = paidFees; 
        updateRemainingFees();
    }
    
    public int getRemainingFees() { return remainingFees; }
    
    private void updateRemainingFees() {
        this.remainingFees = this.totalFees - this.paidFees;
    }
}

