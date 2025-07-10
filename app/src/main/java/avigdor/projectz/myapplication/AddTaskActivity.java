package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.refTasks;
import static avigdor.projectz.myapplication.classes.FBRef.userID;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

import avigdor.projectz.myapplication.classes.FBRef;
import avigdor.projectz.myapplication.classes.Tasks;

public class AddTaskActivity extends AppCompatActivity {

    EditText start_et,end_et,desc_et,name_et,num_class_et,class_et,year_et;
    String schoolYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initView();

    }

    private void initView() {
        start_et = findViewById(R.id.start_et);
        end_et = findViewById(R.id.end_et);
        desc_et = findViewById(R.id.desc_et);
        name_et = findViewById(R.id.name_et);
        num_class_et = findViewById(R.id.num_class_et);
        class_et = findViewById(R.id.class_et);
        year_et = findViewById(R.id.year_et);
    }

    public void addTask(View view) {

        String start = start_et.getText().toString();
        String end = end_et.getText().toString();
        String desc = desc_et.getText().toString();
        String name = name_et.getText().toString();
        String numClass = num_class_et.getText().toString();
        String className = class_et.getText().toString();
        String schoolYear = year_et.getText().toString();

        Tasks task = new Tasks(refTasks.push().getKey(),className,numClass,name,desc,"active",start,end,"",schoolYear);
        refTasks.child(userID).child(schoolYear).child(className+numClass).child(end).setValue(task);
        finish();
    }
}