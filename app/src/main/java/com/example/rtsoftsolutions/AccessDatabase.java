package com.example.rtsoftsolutions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccessDatabase {

    static FirebaseDatabase singletondb ;
    public static FirebaseDatabase getDB() {
            if (AccessDatabase.singletondb == null) {
                singletondb = FirebaseDatabase.getInstance();
                singletondb.setPersistenceEnabled(false);
                singletondb.useEmulator("10.0.2.2", 9000);
            }
            return singletondb;
        }
}
