package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.FBDP;
import static avigdor.projectz.myapplication.classes.FBRef.refAuth;
import static avigdor.projectz.myapplication.classes.FBRef.refTasks;
import static avigdor.projectz.myapplication.classes.FBRef.refUser;
import static avigdor.projectz.myapplication.classes.FBRef.userID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import avigdor.projectz.myapplication.classes.CustomAdapter;
import avigdor.projectz.myapplication.classes.FBRef;
import avigdor.projectz.myapplication.classes.Tasks;
import avigdor.projectz.myapplication.classes.User;

public class MainActivity extends AppCompatActivity{
    Context context = this;
    ListView lv;
    Button btnAdd;
    TextView next_Year , correctYear , all;
    CustomAdapter adp;
    User user;
    Intent intent;
    private ValueEventListener vel,vel2;
    ArrayList<Tasks> tasks;
    Date date;
    String correct_year,next_year;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initObgects();
    }

    private void initViews() {
        lv = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.add_btn);
        correctYear = findViewById(R.id.correct_txt);
        next_Year = findViewById(R.id.next_txt);
        all = findViewById(R.id.all_txt);

    }
    private void initObgects() {

        date = new Date();
        int year = date.getYear() + 1900;
        int nextYear = year+1;
        correct_year = String.valueOf(year).substring(2) +
                "-" + String.valueOf(nextYear).substring(2);
        next_year = String.valueOf(year+1).substring(2) +
                "-" + String.valueOf(nextYear+1).substring(2);

        correctYear.setText(correct_year);
        next_Year.setText(next_year);

        query = refTasks.child(userID).child(correct_year).orderByChild("taskStatus");

        tasks = new ArrayList<Tasks>();
        adp = new CustomAdapter(context, tasks);
        lv.setAdapter(adp);

        btnAdd.setOnClickListener(v -> {
            intent = new Intent(context, AddTaskActivity.class);
            startActivity(intent);
        });


        vel = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.clear();
                Log.i("DATABASE REALTIME","clear arryList");
                for (DataSnapshot year : dataSnapshot.getChildren()) {
                    Log.i("DATABASE REALTIME","get year");
                    for (DataSnapshot cls : year.getChildren()) {
                        Log.i("DATABASE REALTIME","get class");
                        for (DataSnapshot endDate : cls.getChildren()) {
                            Tasks task = endDate.getValue(Tasks.class);
                            tasks.add(task);
                        }

                    }
                }
                adp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        };
        vel2 = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.clear();
                Log.i("DATABASE REALTIME","clear arryList");
                for (DataSnapshot cls : dataSnapshot.getChildren()) {
                    Log.i("DATABASE REALTIME","get year");
                    for (DataSnapshot endDate : cls.getChildren()) {
                        Log.i("DATABASE REALTIME","get class");
                        Tasks task = endDate.getValue(Tasks.class);
                        tasks.add(task);
                    }
                }
                adp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        };
        query.addListenerForSingleValueEvent(vel2);


    }
    @Override
    protected void onResume() {
        super.onResume();
        refTasks.child(userID).child(correct_year).addListenerForSingleValueEvent(vel2);
    }



    public void order(View view) {

        if (((TextView) view).getText().toString().equals("all")){

            query = refTasks.child(userID);
            query.addListenerForSingleValueEvent(vel);
        }
        else {
            String year = ((TextView) view).getText().toString();
            query = refTasks.child(userID).child(year).orderByChild("taskStatus");
            query.addListenerForSingleValueEvent(vel2);
        }

    }
}