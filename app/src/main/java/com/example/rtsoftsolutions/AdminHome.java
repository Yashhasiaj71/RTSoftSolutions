package com.example.rtsoftsolutions;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        // Birthday WhatsApp quick action
        ImageButton whatsapp = view.findViewById(R.id.WhatsappButton);
        whatsapp.setOnClickListener(v -> {
            String PhoneNo = "+916266381188";
            String Message = "Wishing you a very happy birthday!";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://wa.me/"+PhoneNo+"?text="+Uri.encode(Message)));
            try{ startActivity(intent);} catch(ActivityNotFoundException e){ Toast.makeText(requireContext(),"Whatsapp not installed",Toast.LENGTH_SHORT).show(); }
        });

        return view;
    }
}