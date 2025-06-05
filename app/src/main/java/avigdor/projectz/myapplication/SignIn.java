package avigdor.projectz.myapplication;


import static avigdor.projectz.myapplication.classes.FBRef.refAuth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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

    EditText et_email;
    EditText et_psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

    }

    public void init (){
        et_email = findViewById(R.id.et_email);
        et_psw = findViewById(R.id.et_psw);
    }

    public void signIn(View view) {
        String email = et_email.getText().toString();
        String password = et_psw.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Sign in");
            pd.setMessage("Signing in...");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           pd.dismiss();
                           if (task.isSuccessful()){
                               FirebaseUser user = refAuth.getCurrentUser();
                               Toast.makeText(SignIn.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                           }
                           else{
                               Exception exp = task.getException();
                               if (exp instanceof FirebaseAuthInvalidUserException){
                                   Toast.makeText(SignIn.this, "Invalid email", Toast.LENGTH_SHORT).show();
                               } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                                   Toast.makeText(SignIn.this, "Weak password", Toast.LENGTH_SHORT).show();
                               }
                               else if(exp instanceof FirebaseAuthUserCollisionException){
                                   Toast.makeText(SignIn.this, "User already exists", Toast.LENGTH_SHORT).show();
                               } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                   Toast.makeText(SignIn.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                               } else if (exp instanceof FirebaseNetworkException) {
                                   Toast.makeText(SignIn.this, "Network error", Toast.LENGTH_SHORT).show();
                               } else{
                                   Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                               }
                           }
                        }
                    });
        }

    }
}