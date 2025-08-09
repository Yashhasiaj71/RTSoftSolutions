package com.example.rtsoftsolutions;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rtsoftsolutions.miscallenous.CheckStatus;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class Login_Fragment extends Fragment {
    private FirebaseAuth mAuth ;
    private FirebaseDatabase db ;
    private DatabaseReference ref ;
    private com.example.rtsoftsolutions.databinding.FragmentLoginBinding binding ;
    private CheckStatus status ;
    private String TAG = "debugAuthenticationActivity" ;
    private ActivityResultLauncher<Intent> signInLauncher ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         binding = com.example.rtsoftsolutions.databinding.FragmentLoginBinding.inflate(inflater , container , false) ;
         return binding.getRoot() ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        status = new CheckStatus() ;
        status.createnetworkfunction();
        status.registernetworkcallback();
        status.registerforupdates();

        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        onSignInResult(result);
                    }
                }
        );
        //list service providers like authenticate by users or authenticate by email
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build();
        if (status.getIsconnected()) {
            try {
                mAuth = FirebaseAuth.getInstance();
                mAuth.useEmulator("10.0.2.2" , 4000);
                db = AccessDatabase.getDB();
                ref = db.getReference("User");
            } catch (Exception e) {
                Log.d(TAG, "review line 36 to 39");
            }
        } else {
            Toast.makeText(getContext(), "Check network connectivity", Toast.LENGTH_LONG).show();
        }

        binding.loginButton.setOnClickListener(v -> {
            if (status.getIsconnected()) {
                signInLauncher.launch(signInIntent);
            } else {
                Toast.makeText(getContext(), "Connect to Internet", Toast.LENGTH_LONG).show();
            }
        });
    }
        private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
            IdpResponse response = result.getIdpResponse() ;
            if(result.getResultCode() == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;
                if(user != null) {
                    Intent intent = new Intent(getActivity() , MainActivity.class) ;
                    intent.putExtra("user" , user) ;
                    startActivity(intent) ;
                }else {
                    Log.d(TAG , "No such user") ;
                }
            }else {
                Toast.makeText(getContext() , "Invalid Credentials" , Toast.LENGTH_LONG) ;
            }
    }
}


