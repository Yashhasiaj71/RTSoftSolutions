package com.example.rtsoftsolutions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeesFragment newInstance(String param1, String param2) {
        FeesFragment fragment = new FeesFragment();
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

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fees, container, false);

        //        Whatsapp icon click listner
        ImageButton whatsapp = view.findViewById(R.id.WhatsappButton);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PhoneNo = "+916266381188";
                String Message = "Dear Student, your fees ($ xxx) is pending, Kindly pay it by the due date team Interns";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/"+PhoneNo+"?text="+Uri.encode(Message)));

                try{
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(requireContext(),"Whatsapp not installed",Toast.LENGTH_SHORT).show();
                }
            }
        });
   return view ;
    }
}