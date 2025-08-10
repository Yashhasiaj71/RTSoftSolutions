package com.example.rtsoftsolutions;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rtsoftsolutions.databinding.ActivityAuthenticationBinding;
import com.example.rtsoftsolutions.miscallenous.CheckStatus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity extends AppCompatActivity {

    private FirebaseDatabase db ;
    private DatabaseReference ref ;
    private ActivityAuthenticationBinding binding ;
    private CheckStatus status ;
    private String TAG = "debugAuthenticationActivity" ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);
        status = new CheckStatus();
        status.createnetworkfunction();
        status.registernetworkcallback();
        status.registerforupdates();
        if (status.getIsconnected()) {
            try {
                db = AccessDatabase.getDB();
                ref = db.getReference("User");
            } catch (Exception e) {
                Log.d(TAG, "review line 36 to 39");
            }
        } else {
            Toast.makeText(getApplicationContext() ,"Check network connectivity" , Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController controller = Navigation.findNavController(AuthenticationActivity.this ,binding.fragmentContainerView.getId()) ;
     }
}
