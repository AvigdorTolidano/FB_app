package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.refAuth;
import static avigdor.projectz.myapplication.classes.FBRef.refUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import avigdor.projectz.myapplication.classes.User;

public class MainActivity extends AppCompatActivity {
    Context context = this;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(context, SignIn.class);
        startActivity(intent);

    }

}