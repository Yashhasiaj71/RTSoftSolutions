package com.example.rtsoftsolutions.Fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtsoftsolutions.Models.Student;
import com.example.rtsoftsolutions.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHome extends Fragment {

//    course images id's array '
private int[] imageId= {
        R.drawable.java,
        R.drawable.mern
};

private Runnable ImageSwitch;
private Handler handler = new Handler();
private int CurrentIndex =0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Birthday management
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private LinearLayout birthdayContainer;
    private TextView birthdayText;
    private List<Student> birthdayStudents;

    public AdminHome() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AdminHome newInstance(String param1, String param2) {
        AdminHome fragment = new AdminHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        
        // Initialize Firebase
        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");
        birthdayStudents = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_home,container,false);
        // Inflate the layout for this fragment

//        change course image
        ImageView courseImg = view.findViewById(R.id.CourseImage);
  ImageSwitch = new Runnable() {
      @Override
      public void run() {
          courseImg.setImageResource(imageId[CurrentIndex]);
//          set index to 0 after the index reaches end
          CurrentIndex = (CurrentIndex+1) % imageId.length;
          handler.postDelayed(this,3000);
      }
  };

  handler.post(ImageSwitch);

        NavController controller = findNavController(AdminHome.this);

        // Navigate to Courses when header tapped
        courseImg.setOnClickListener(v -> controller.navigate(R.id.action_AdminHome_to_courseFragment));

        // Student Manager
        ImageButton studentManagerBtn = view.findViewById(R.id.StudentManagerButton);
        studentManagerBtn.setOnClickListener(v -> controller.navigate(R.id.action_global_studentmanager));

        // Fees
        ImageButton fees = view.findViewById(R.id.FeesButton);
        fees.setOnClickListener(v -> controller.navigate(R.id.action_AdminHome_to_feesFragment));

        // Attendance -> open marking screen via sub graph id if available, else replace fragment directly
        ImageButton attendanceBtn = view.findViewById(R.id.AttendanceButton);
        attendanceBtn.setOnClickListener(v -> {
            // Fallback: open MarkAttendanceFragment inside current container (requires nav entry)
            try {
                controller.navigate(R.id.markAttendanceFragment);
            } catch (Exception e) {
                // If nav id missing, do nothing
            }
        });

        // Reports placeholder
        ImageButton reportsBtn = view.findViewById(R.id.ReportsButton);
        reportsBtn.setOnClickListener(v -> controller.navigate(R.id.action_global_fragmentReport));

        // Initialize birthday components
        birthdayContainer = view.findViewById(R.id.BirthdayContainer);
        birthdayText = view.findViewById(R.id.BirthdayText);
        
        // Check for birthdays
        checkBirthdays(view);

        return view;
    }
    
    private void checkBirthdays(View view) {
        String today = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());
        
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                birthdayStudents.clear();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null && student.birthDate != null) {
                        // Extract day and month from birth date
                        String birthDayMonth = extractDayMonth(student.birthDate);
                        if (birthDayMonth.equals(today)) {
                            birthdayStudents.add(student);
                        }
                    }
                }
                
                updateBirthdayUI(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminHome", "Failed to load students for birthday check", databaseError.toException());
            }
        });
    }
    
    private String extractDayMonth(String birthDate) {
        try {
            // Assuming birthDate is in format "dd/MM/yyyy" or "dd-MM-yyyy"
            String[] parts = birthDate.split("[/-]");
            if (parts.length >= 2) {
                return parts[0] + "/" + parts[1];
            }
        } catch (Exception e) {
            Log.e("AdminHome", "Error parsing birth date: " + birthDate, e);
        }
        return "";
    }
    
    private void updateBirthdayUI(View view) {
        View birthdayCard = view.findViewById(R.id.BirthdayCard);
        
        if (birthdayStudents.isEmpty()) {
            // No birthdays today
            birthdayCard.setVisibility(View.GONE);
            return;
        }else{
            birthdayCard.setVisibility(View.VISIBLE);
        }
        

        if (birthdayStudents.size() == 1) {
            // Single birthday
            Student student = birthdayStudents.get(0);
            birthdayText.setText("Happy Birthday, " + student.name + "! 🎉");
            
            // Show single WhatsApp button
            ImageButton singleBtn = getActivity().findViewById(R.id.WhatsappButton);
            singleBtn.setVisibility(View.VISIBLE);
            singleBtn.setOnClickListener(v -> sendBirthdayWish(student));
            
            // Hide multiple buttons container
            LinearLayout multipleContainer = getActivity().findViewById(R.id.MultipleWhatsAppContainer);
            multipleContainer.setVisibility(View.GONE);
            
        } else {
            // Multiple birthdays
            StringBuilder names = new StringBuilder();
            for (int i = 0; i < birthdayStudents.size(); i++) {
                if (i > 0) {
                    if (i == birthdayStudents.size() - 1) {
                        names.append(" & ");
                    } else {
                        names.append(", ");
                    }
                }
                names.append(birthdayStudents.get(i).name);
            }
            birthdayText.setText("Happy Birthday to " + names.toString() + "! 🎉");
            
            // Create multiple WhatsApp buttons for each student
            createMultipleWhatsAppButtons(view);
        }
    }
    
    private void createMultipleWhatsAppButtons(View view) {
        // Hide single WhatsApp button
        ImageButton singleBtn = view.findViewById(R.id.WhatsappButton);
        if (singleBtn != null) {
            singleBtn.setVisibility(View.GONE);
        }
        
        // Get the multiple buttons container
        LinearLayout multipleContainer = view.findViewById(R.id.MultipleWhatsAppContainer);
        multipleContainer.removeAllViews();
        
        // Add buttons for each student
        for (Student student : birthdayStudents) {
            // Create container for button and label
            LinearLayout studentContainer = new LinearLayout(requireContext());
            studentContainer.setOrientation(LinearLayout.VERTICAL);
            studentContainer.setGravity(android.view.Gravity.CENTER);
            studentContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            studentContainer.setPadding(8, 0, 8, 0);
            // Create WhatsApp button
            ImageButton whatsappBtn = new ImageButton(requireContext());
            whatsappBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    48, 48
            ));
            whatsappBtn.setBackgroundResource(R.drawable.whatsapp_button_background);
            whatsappBtn.setImageResource(R.drawable.whatsapp_logo);
            whatsappBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            whatsappBtn.setPadding(10, 10, 10, 10);
            whatsappBtn.setContentDescription("Send birthday wish to " + student.name);
            
            // Add student name label
            TextView nameLabel = new TextView(requireContext());
            nameLabel.setText(student.name);
            nameLabel.setTextSize(10);
            nameLabel.setTextColor(requireContext().getResources().getColor(R.color.black));
            nameLabel.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            nameLabel.setPadding(4, 4, 4, 0);
            nameLabel.setGravity(android.view.Gravity.CENTER);
            
            // Add views to container
            studentContainer.addView(whatsappBtn);
            studentContainer.addView(nameLabel);
            
            // Set click listener
            whatsappBtn.setOnClickListener(v -> sendBirthdayWish(student));
            
            multipleContainer.addView(studentContainer);
        }
        
        // Show the multiple buttons container
        multipleContainer.setVisibility(View.VISIBLE);
    }
    
    private void sendBirthdayWish(Student student) {
        String phoneNo = student.phoneNo;
        if (phoneNo.startsWith("+91")) {
            phoneNo = phoneNo.substring(3); // Remove +91 prefix
        }
        
        String message = "Happy Birthday " + student.name + "! 🎉🎂\n\nWishing you a fantastic day filled with joy and success!\n\nBest regards,\nTeam RT Soft Solutions";
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/91" + phoneNo + "?text=" + Uri.encode(message)));
        
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}