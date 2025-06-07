package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import avigdor.projectz.myapplication.classes.User;

public class Registration extends AppCompatActivity {
    EditText email_et, password_et, confirmPassword_et, fname_et, lname_et, phone_et, age_et;
    String email, password, fname, lname, phone, confirmPassword;
    Button register_btn;
    int age;
    User myUser;

    ProgressDialog pd;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

    }

    private void init(){
        register_btn = findViewById(R.id.regitration_btn);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.psw_et);
        confirmPassword_et = findViewById(R.id.cpsw_et);
        fname_et = findViewById(R.id.fname_et);
        lname_et = findViewById(R.id.lname_et);
        phone_et = findViewById(R.id.phone_et);
        age_et = findViewById(R.id.age_et);


        pd = new ProgressDialog(context);
        pd.setTitle("Registration");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

        email = email_et.getText().toString();
        password = password_et.getText().toString();
        confirmPassword = confirmPassword_et.getText().toString();
        fname = fname_et.getText().toString();
        lname = lname_et.getText().toString();
        phone = phone_et.getText().toString();
        age = Integer.parseInt(age_et.getText().toString());

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(view);
            }
        });
    }
    public void submit(View view) {
        refAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()){
                        FirebaseUser user = refAuth.getCurrentUser();
                        myUser = new User(fname, lname, email, phone, user.getUid(), age);
                        Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Exeptions(task.getException());
                    }
                }
            }
        );
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