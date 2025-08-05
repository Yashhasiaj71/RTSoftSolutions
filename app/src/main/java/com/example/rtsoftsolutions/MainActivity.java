package com.example.rtsoftsolutions;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log; // For logging the value
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //changes from madhav
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your layout
        Fragment AdminHome = new AdminHome(); // your target fragment

    }

    @Override

    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this , R.id.ContainerView);


       ImageButton homeB = findViewById(R.id.HomeButton);
       ImageButton studentB = findViewById(R.id.StudentButton);
       ImageButton reportB = findViewById(R.id.ReportButton);

       homeB.setOnClickListener(v->{
           navController.navigate(R.id.action_global_AdminHome);
       });

       studentB.setOnClickListener(v->{
           navController.navigate(R.id.action_global_studentmanager);
       });

       reportB.setOnClickListener(v->{

           navController.navigate(R.id.action_global_fragmentReport);
       });


//      ========= :TODO report fragment button ========
//       reportB.setOnClickListener(v->{
//           navController.navigate(R.id.action_global_AdminHome);
//       });
    }
}