package com.example.rtsoftsolutions.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rtsoftsolutions.Models.Student;
import com.example.rtsoftsolutions.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentProfile#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class StudentProfile extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private EditText namebox ;
    private EditText addressbox ;
    private EditText feesbox ;
    private EditText emailbox ;
    private EditText phonebox ;
    private EditText genderbox;
    private EditText fathernamebox ;
    private EditText mothernamebox ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String SearchQuery = "";
    private Student student;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentProfile newInstance(String param1, String param2) {
        StudentProfile fragment = new StudentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StudentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        initializeall(view);
        SharedPreferences nameShare = getActivity().getSharedPreferences("myPref",MODE_PRIVATE);
       String name =  nameShare.getString("Name","yash");
Log.d("Name",name);

SearchQuery = name.trim();
        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");

        studentsRef.orderByChild("name").startAt(SearchQuery).endAt(SearchQuery+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if any student was found
                if (!snapshot.exists()) {
                    Log.d("Student", "No student found with that name.");
                    return;
                }

                for(DataSnapshot studentSnapshot : snapshot.getChildren()){
                    student = studentSnapshot.getValue(Student.class);

                    // ✅ CORRECT: Put the code that uses the student object HERE.
                    if(student != null){
                        Log.d("Student", "Name: " + student.name + ", Fees: " + student.totalFees);
                        namebox.setText(student.name);
                        addressbox.setText(student.address);
                        feesbox.setText(student.totalFees) ;
                        emailbox.setText(student.email) ;
                        phonebox.setText(student.phoneNo) ;
                        genderbox.setText("male") ;
                        fathernamebox.setText(student.fatherName);
                        mothernamebox.setText(student.motherName);
                        // Here you would update your UI elements (TextViews, etc.)
                        // e.g., studentNameTextView.setText(student.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Database error: " + error.getMessage());
            }
        });

        // ❌ INCORRECT: Don't try to use the 'student' object here. It will still be null.
        // if(student!=null){ ... }

        return view;
    }

    public void initializeall(View rootview) {
        namebox = rootview.findViewById(R.id.nameinput) ;
         addressbox = rootview.findViewById(R.id.studentaddressinput) ;
         feesbox = rootview.findViewById(R.id.feesinput) ;
        emailbox = rootview.findViewById(R.id.emailinput) ;
     phonebox = rootview.findViewById(R.id.phoneinput) ;
         genderbox = rootview.findViewById(R.id.studentgenderinput) ;
       fathernamebox = rootview.findViewById(R.id.fathernameinput) ;
         mothernamebox = rootview.findViewById(R.id.mothernameinput) ;
    }
}