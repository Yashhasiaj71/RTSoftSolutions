package com.example.rtsoftsolutions;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link #} factory method to
 * create an instance of this fragment.
 */
public class studentmanager extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Button addnewstudent ;

    private Button ViewStudent;

    private Button std_mgr_view ;
    private FrameLayout frame ;
    private FragmentContainerView std_mgr;

    public studentmanager() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initializing all data in that is required once for all==========================================
        addnewstudent = getActivity().findViewById(R.id.addstudent) ;

        //call to add student fragment so that admin can add new student==============================================
        addnewstudent.setOnClickListener(v -> {
            Fragment formfragment = new AddStudent_Fragment() ;
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3 , formfragment).commit() ;
        }) ;


        ViewStudent = getActivity().findViewById(R.id.viewstudent);
        ViewStudent.setOnClickListener(v->{
            NavController controller = findNavController(studentmanager.this);
            controller.navigate(R.id.action_studentmanager_to_viewStudent_Fragment3);
        });


}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_studentmanager, container, false);
    }


}