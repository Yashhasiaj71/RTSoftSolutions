package com.example.rtsoftsolutions.Models;

public class Enquiries {
     private String enqr_name;
     private String enqr_date;
     private String enqr_contact_no;
     private String enqr_interest;
     private String enqr_age;

     // 🔹 Empty constructor (required for Firebase)
     public Enquiries() {
     }

     // 🔹 Full constructor
     public Enquiries(String enqr_name, String enqr_date, String enqr_contact_no, String enqr_interest, String enqr_age) {
          this.enqr_name = enqr_name;
          this.enqr_date = enqr_date;
          this.enqr_contact_no = enqr_contact_no;
          this.enqr_interest = enqr_interest;
          this.enqr_age = enqr_age;
     }

     // 🔹 Getters
     public String getEnqr_name() {
          return enqr_name;
     }

     public String getEnqr_date() {
          return enqr_date;
     }

     public String getEnqr_contact_no() {
          return enqr_contact_no;
     }

     public String getEnqr_interest() {
          return enqr_interest;
     }

     public String getEnqr_age() {
          return enqr_age;
     }

     // 🔹 Setters
     public void setEnqr_name(String enqr_name) {
          this.enqr_name = enqr_name;
     }

     public void setEnqr_date(String enqr_date) {
          this.enqr_date = enqr_date;
     }

     public void setEnqr_contact_no(String enqr_contact_no) {
          this.enqr_contact_no = enqr_contact_no;
     }

     public void setEnqr_interest(String enqr_interest) {
          this.enqr_interest = enqr_interest;
     }

     public void setEnqr_age(String enqr_age) {
          this.enqr_age = enqr_age;
     }
}
