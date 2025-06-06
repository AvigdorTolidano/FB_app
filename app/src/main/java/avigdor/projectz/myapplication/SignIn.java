package avigdor.projectz.myapplication;


import static avigdor.projectz.myapplication.classes.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class SignIn extends AppCompatActivity {

    Context context = this;
    EditText et_email;
    EditText et_psw;
    Button btn_sign_in_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }

    public void init (){
        et_email = findViewById(R.id.et_email);
        et_psw = findViewById(R.id.et_psw);
        btn_sign_in_up = findViewById(R.id.sign_in_up_btn);

        btn_sign_in_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CORRECTED: Use YourActivityName.this to get the Activity context
                Toast.makeText(context, "btn clicked", Toast.LENGTH_SHORT).show();
                signIn(v); // Assuming signIn is a method in this Activity
            }
        });
    }

    public void signIn(View view) {
        String email = et_email.getText().toString();
        String password = et_psw.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Sign in");
            pd.setMessage("Signing in...");
            pd.show();
            if(refAuth.getCurrentUser() != null){
                refAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()){
                            FirebaseUser user = refAuth.getCurrentUser();
                            Toast.makeText(context, "Sign in uesr " + user.getEmail() + " successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Exeptions(task.getException());
                        }
                    }
                });

            }else{
                refAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pd.dismiss();
                                if (task.isSuccessful()){
                                    FirebaseUser user = refAuth.getCurrentUser();
                                    Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Exeptions(task.getException());
                                }
                            }
                        });
            }

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