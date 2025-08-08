package com.example.rtsoftsolutions;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
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
    private int[] imageId = {
            R.drawable.java,
            R.drawable.mern
    };

    private Runnable ImageSwitch;
    private Handler handler = new Handler();
    private int CurrentIndex = 0;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_home, container, false);
        // Inflate the layout for this fragment

//        change course image
        ImageView courseImg = view.findViewById(R.id.CourseImage);
        ImageSwitch = new Runnable() {
            @Override
            public void run() {
                courseImg.setImageResource(imageId[CurrentIndex]);
//          set index to 0 after the index reaches end
                CurrentIndex = (CurrentIndex + 1) % imageId.length;
                handler.postDelayed(this, 3000);
            }
        };

        handler.post(ImageSwitch);

        NavController controller = findNavController(AdminHome.this);

        ImageView Course = view.findViewById(R.id.CourseImage);
  Course.setOnClickListener(v->{
      controller.navigate(R.id.action_AdminHome_to_courseFragment);
  });
//        Fees Button ClickListner
        ImageButton fees = view.findViewById(R.id.FeesButton);
        fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_AdminHome_to_feesFragment);
            }
        });

//        Whatsapp icon click listner
        ImageButton whatsapp = view.findViewById(R.id.WhatsappButton);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PhoneNo = "+916266381188";
                String Message = "Wishing you A very happy birthday Student, team Interns";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + PhoneNo + "?text=" + Uri.encode(Message)));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(requireContext(), "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                }
            }
        }) ;

        GridLayout grid = view.findViewById(R.id.BirthdayInfo) ;
        grid.setClickable(true);
        grid.setFocusable(true);
        grid.findViewById(R.id.BirthdayText).setClickable(true);
        grid.findViewById(R.id.BirthdayText).setFocusable(true);
        grid.findViewById(R.id.WhatsappButton).setOnTouchListener(new SwipeTouchListener(requireContext(), new SwipeTouchListener.SwipeCallback() {
            @Override
            public void onSwipeLeft() {
                Log.d("Swipe", "Swiped Left");
                Animation leftsiwp = AnimationUtils.loadAnimation(getContext() , R.anim.hardleftswipe) ;
                grid.startAnimation(leftsiwp);
                if(grid.getAlpha() == 0) {
                     grid.setAlpha(1);
                }else {
                    grid.setAlpha(0);
                }
            }

            @Override
            public void onSwipeRight() {
                Log.d("Swipe", "Swiped Right");
                // Do something
                if(grid.getAlpha() == 0) {
                    grid.setAlpha(1);
                }else {
                    grid.setAlpha(0);
                }

            }

            @Override
            public void onSwipeDown() {
                Log.d("Swipe" , "Swiped Right") ;
              if(grid.getAlpha() == 0) {
                  grid.setAlpha(1);
              }else {
                  grid.setAlpha(0);
              }
            }

            @Override
            public void onSwipeUp() {
                 Log.d("Swipe" , "Swiped Right") ;
                 if(grid.getAlpha() == 0) {
                      grid.setAlpha(1);
                 }else {
                      grid.setAlpha(0);
                 }
            }
        }));
        return view;
    }
}


class SwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public interface SwipeCallback {
        void onSwipeLeft();
        void onSwipeRight();
        void onSwipeDown() ;
        void onSwipeUp() ;
    }

    public SwipeTouchListener(Context context, SwipeCallback callback) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 50;
            private static final int SWIPE_VELOCITY_THRESHOLD = 50;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) >= Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            callback.onSwipeRight();
                        } else {
                            callback.onSwipeLeft();
                        }
                        return true;
                    }
                }else {
                     if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                          if(diffY> 0) {
                              callback.onSwipeDown();
                          }else {
                               callback.onSwipeUp() ;
                          }
                     }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}