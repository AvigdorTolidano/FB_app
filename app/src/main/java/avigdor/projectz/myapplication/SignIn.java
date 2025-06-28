package avigdor.projectz.myapplication;


import static avigdor.projectz.myapplication.classes.FBRef.refAuth;
import static avigdor.projectz.myapplication.classes.FBRef.refUser;

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
import avigdor.projectz.myapplication.classes.User;

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
    EditText email_et , psw_et , name_et , lname_et ;
    TextView email_txt , name_txt , lname_txt , title_txt , registration_txtBtn , error_txt;
    Button btn_sign_in;
    boolean isNewUser;
    ProgressDialog pd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }

    public void init () {
        email_et = findViewById(R.id.email_et);
        psw_et = findViewById(R.id.psw_et);
        name_et = findViewById(R.id.name_et);
        lname_et = findViewById(R.id.lname_et);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        email_txt = findViewById(R.id.email_txt);
        name_txt = findViewById(R.id.name_txt);
        lname_txt = findViewById(R.id.lname_txt);
        title_txt = findViewById(R.id.title_txt);
        registration_txtBtn = findViewById(R.id.registration_txtBtn);
        error_txt = findViewById(R.id.error_txt);
        isNewUser = false;

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIU(v);
            }
        });

        registration_txtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setView(v); }
        });

        pd = new ProgressDialog(context);
        pd.setTitle("Sign in");
        pd.setMessage("Signing in...");
        intent = new Intent(context, MainActivity.class);
    }

    public void signIU(View view) {
        String email = email_et.getText().toString();
        String password = psw_et.getText().toString();

        if (isNewUser){
            String name = name_et.getText().toString();
            String lname = lname_et.getText().toString();
            pd.setTitle("Sign up");
            pd.setMessage("Signing up...");
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lname.isEmpty()) {
                error_txt.setText("please fill all fields!");
                error_txt.setVisibility(View.VISIBLE);
                if (name.isEmpty()) {
                    name_et.requestFocus();
                }
                else if (lname.isEmpty()) {
                    lname_et.requestFocus();
                }
                else if (email.isEmpty()) {
                    email_et.requestFocus();
                }
                else if (password.isEmpty()) {
                    psw_et.requestFocus();
                }
            }
            else{
                pd.show();
                refAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()){
                            FirebaseUser user = refAuth.getCurrentUser();
                            refUser.child(user.getUid()).setValue(new User(name,lname,email,user.getUid()));

                            startActivity(intent);
                        }
                        else{
                            Exeptions(task.getException());
                        }
                    }
                });
            }


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
                        startActivity(intent);

                    }
                    else{
                        Exeptions(task.getException());
                    }
                }
            });

        }

    }
    public void Exeptions(Exception exp){
        error_txt.setVisibility(View.VISIBLE);
        String message = exp.getMessage();

        if (exp instanceof FirebaseAuthInvalidCredentialsException){
            error_txt.setText("email is not valid\nex: john.mclean@examplepetstore.com");
            email_et.requestFocus();
        }
        else if (message.contains("PASSWORD_DOES_NOT_MEET_REQUIREMENTS")) {
            int startIndex = message.indexOf("[Password");
            int endIndex = message.lastIndexOf("]");
            message = message.substring(startIndex + 1, endIndex - 2);
            String [] massages = message.split(",");
            message = "";
            for(String msg : massages){
                message += "•"+msg + "\n";
            }
            error_txt.setText(message);
            psw_et.requestFocus();
        }
        else if(exp instanceof FirebaseAuthUserCollisionException){
            error_txt.setText(exp.getMessage());
            email_et.requestFocus();
        }
        else if (exp instanceof FirebaseNetworkException) {
            error_txt.setText(exp.getMessage());
        }
        else{
            error_txt.setText("There was a problem with the server.\n" +
                    "Please check your internet connection and try again," +
                    " or wait a moment and try again.");
        }

    }

    public void setView(View v){
        isNewUser = !isNewUser;
        if (isNewUser){

            name_txt.setVisibility(View.VISIBLE);
            name_et.setVisibility(View.VISIBLE);
            lname_txt.setVisibility(View.VISIBLE);
            lname_et.setVisibility(View.VISIBLE);
            registration_txtBtn.setText("allready have an account? click here !");
            btn_sign_in.setText("Sign up");
            title_txt.setText("Sign Up");
            name_et.requestFocus();
        }
        else{
            name_txt.setVisibility(View.GONE);
            name_et.setVisibility(View.GONE);
            lname_txt.setVisibility(View.GONE);
            lname_et.setVisibility(View.GONE);
            registration_txtBtn.setText("dont have an account yet? click here !");
            btn_sign_in.setText("Sign in");
            title_txt.setText("Sign In");
            email_et.requestFocus();
        }
    }

}


