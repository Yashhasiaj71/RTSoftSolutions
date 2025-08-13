package com.example.rtsoftsolutions;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class studentmanager extends Fragment {

    private static final String TAG = "StudentManager";

    // UI Components
    private Button addnewstudent;
    private Button ViewStudent;
    private TextView studentCountTextView;
    private FrameLayout frame;
    private FragmentContainerView std_mgr;

    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;

    public studentmanager() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studentmanager, container, false);
        
        // Initialize Firebase
        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");
        
        // Initialize UI components
        initializeViews(view);
        setupClickListeners();
        
        // Load student count
        loadStudentCount();
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initializeViews(View view) {
        addnewstudent = view.findViewById(R.id.addstudent);
        ViewStudent = view.findViewById(R.id.viewstudent);
        
        // Find the TextView that shows the student count
        // Based on the layout, it's the second TextView in the CardView
        FrameLayout showCountFrame = view.findViewById(R.id.showcount);
        if (showCountFrame != null && showCountFrame.getChildCount() > 0) {
            View cardView = showCountFrame.getChildAt(0);
            if (cardView instanceof androidx.cardview.widget.CardView) {
                androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) cardView;
                if (card.getChildCount() > 1) {
                    View secondChild = card.getChildAt(1);
                    if (secondChild instanceof TextView) {
                        studentCountTextView = (TextView) secondChild;
                    }
                }
            }
        }
    }

    private void setupClickListeners() {
        // Add new student button
        addnewstudent.setOnClickListener(v -> {
            Fragment formfragment = new AddStudent_Fragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView3, formfragment).commit();
        });

        // View student button
        ViewStudent.setOnClickListener(v -> {
            NavController controller = findNavController(studentmanager.this);
            controller.navigate(R.id.action_studentmanager_to_viewStudent_Fragment3);
        });
    }

    private void loadStudentCount() {
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int studentCount = (int) dataSnapshot.getChildrenCount();
                updateStudentCountDisplay(studentCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load student count", databaseError.toException());
                updateStudentCountDisplay(0);
            }
        });
    }

    private void updateStudentCountDisplay(int count) {
        if (studentCountTextView != null) {
            studentCountTextView.setText(String.valueOf(count));
        }
    }
}