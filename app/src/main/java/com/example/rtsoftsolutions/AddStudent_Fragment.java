package com.example.rtsoftsolutions;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

public class AddStudent_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String TAG =  "debugaddstudent" ;
    private Button savedetails ;
    private EditText nameinput ;
    private EditText emailinput ;
    private EditText phoneinput ;
    private EditText addressinput ;
    private EditText aadharinput ;
    private EditText fathernameinput ;
    private EditText motherenameinput;
    private EditText tpainput ;
    private EditText amountfilled ;
    private LinearLayout imageinputlayout ;
    private ImageView imageholder ;
    Activity currentactivity ;
    ActivityResultLauncher<Intent> imagePickerLauncher ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          //Initialize all datatypes============================
        currentactivity = getActivity();
        imageinputlayout = currentactivity.findViewById(R.id.imageview);
        imageholder = imageinputlayout.findViewById(R.id.imageholder);

        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        // Proceed with your URI
                    } else {
                        Log.e("ImagePicker", "Picker cancelled or data is null");
                    }
                }
        );

        imageinputlayout.setOnClickListener(v -> {
            Toast.makeText(getContext() , "successfully clicked" , Toast.LENGTH_LONG).show() ;
            /// /////////////////////////////////////
            if(launchmediainputactivity(getContext() , imagePickerLauncher)) {
                Log.d(TAG , "Operation Successfull now f**k around") ;
            }else {
                Log.d(TAG , "you stupid!! watch line 62  something is null check stupid") ;
            }
        });


    }

    public boolean launchmediainputactivity(Context current , ActivityResultLauncher<Intent> imagePickerLauncher) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
            return true ;
        } catch (NullPointerException e) {
            Log.d(TAG, "Null Pointer exception please watch it carefully you stupid");
        }
        return false ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment================================================
        return inflater.inflate(R.layout.fragment_add_student_, container, false);
    }


}