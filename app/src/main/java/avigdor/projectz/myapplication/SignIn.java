package avigdor.projectz.myapplication;


import static avigdor.projectz.myapplication.classes.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import avigdor.projectz.myapplication.classes.FBRef.*;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
    Context context = this;
    EditText email_et;
    EditText psw_et;
    Button btn_sign_in;

    TextView registration_txtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }

    public void init () {
        email_et = findViewById(R.id.email_et);
        psw_et = findViewById(R.id.psw_et);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        registration_txtBtn = findViewById(R.id.registration_txtBtn);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(v);
            }
        });

        registration_txtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Registration.class);
                startActivity(intent);
            }
        });
    }

    public void signIn(View view) {
        String email = email_et.getText().toString();
        String password = psw_et.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            ProgressDialog pd = new ProgressDialog(context);
            pd.setTitle("Sign in");
            pd.setMessage("Signing in...");
            pd.show();

            refAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()){
                        FirebaseUser user = refAuth.getCurrentUser();
                        Toast.makeText(context, user.getEmail() + " successfuly signed in", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Exeptions(task.getException());
                    }
                }
            });

        }

    }
    public void Exeptions(Exception exp){

        if (exp instanceof FirebaseAuthInvalidUserException){
            Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show();
        } else if (exp instanceof FirebaseAuthWeakPasswordException) {
            Toast.makeText(context, "Weak password", Toast.LENGTH_SHORT).show();
        }
        else if(exp instanceof FirebaseAuthUserCollisionException){
            Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
        } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
        } else if (exp instanceof FirebaseNetworkException) {
            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }

}


