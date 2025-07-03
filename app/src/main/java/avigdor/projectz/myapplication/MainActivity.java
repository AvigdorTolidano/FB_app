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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import avigdor.projectz.myapplication.classes.CustomAdapter;
import avigdor.projectz.myapplication.classes.FBRef;
import avigdor.projectz.myapplication.classes.Tasks;
import avigdor.projectz.myapplication.classes.User;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    ListView lv;
    Button btnAdd;
    TextView activeTasks;
    CustomAdapter adp;
    User user;
    Intent intent;
    private ValueEventListener vel;
    ArrayList<Tasks> tasks;

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
        activeTasks = findViewById(R.id.active_txt);
    }
    private void initObgects() {
        query = refTasks.child(userID).orderByChild("taskStatus").equalTo("active");
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
        query.addValueEventListener(vel);
        refTasks.child(userID).addValueEventListener(vel);

    }
    @Override
    protected void onResume() {
        super.onResume();
        refTasks.child(userID).addValueEventListener(vel);
    }
    @Override
    protected void onStop() {
        super.onStop();
        refTasks.child(userID).removeEventListener(vel);
    }



}