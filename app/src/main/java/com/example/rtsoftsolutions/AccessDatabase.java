package com.example.rtsoftsolutions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccessDatabase {

    public static FirebaseDatabase getDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(false);
        db.useEmulator("10.0.2.2" , 9000);
        return db ;
    }
}
