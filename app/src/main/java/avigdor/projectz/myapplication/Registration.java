package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.refAuth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final String SPECIAL_CHAR_SET_DISPLAY = "@, #, $, %, ^, &, +, =, ?, !";
    private static final String SPECIAL_CHAR_REGEX_PART = "@#$%^&+=?!";

    EditText email_et, password_et, confirmPassword_et, fname_et, lname_et, phone_et, age_et;
    String email, password, fname, lname, phone, confirmPassword, age;
    Button register_btn;

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

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(view);
            }
        });
    }
    public void submit(View view) {
        email = email_et.getText().toString();
        password = password_et.getText().toString();
        confirmPassword = confirmPassword_et.getText().toString();
        fname = fname_et.getText().toString();
        lname = lname_et.getText().toString();
        phone = phone_et.getText().toString();
        age = age_et.getText().toString();

        if (fname.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. First Name is missing.", Toast.LENGTH_LONG).show();
            fname_et.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Last Name is missing.", Toast.LENGTH_LONG).show();
            lname_et.requestFocus();
            return;
        }
        if (age_et.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Age is missing.", Toast.LENGTH_LONG).show();
            age_et.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Phone is missing.", Toast.LENGTH_LONG).show();
            phone_et.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Email is missing.", Toast.LENGTH_LONG).show();
            email_et.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Password is missing.", Toast.LENGTH_LONG).show();
            password_et.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Confirm Password is missing.", Toast.LENGTH_LONG).show();
            confirmPassword_et.requestFocus();
            return;
        }
        if (!isValidEmailPattern(email)) {
            Toast.makeText(context, "Invalid email format.", Toast.LENGTH_SHORT).show();
            email_et.requestFocus();
            return;
        }

        String passwordError = getPasswordValidationError(password);

        if (passwordError != null) {
            Toast.makeText(context, passwordError, Toast.LENGTH_SHORT).show();
            password_et.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {

            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_LONG).show();
            confirmPassword_et.requestFocus();
            return;
        }


        pd.show();
        refAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()){
                            FirebaseUser user = refAuth.getCurrentUser();
                            myUser = new User(fname, lname, email, phone, user.getUid(), Integer.parseInt(age));
                            Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Exeptions(task.getException());
                        }
                    }
            });
    }


    private String getPasswordValidationError(String password) {

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.";
        }
        if (password.matches(".*\\s.*")) { // \s matches any whitespace character
            return "Password cannot contain spaces or whitespace characters.";
        }
        if (!password.matches(".*[0-9].*")) {
            return "Password must include at least one digit (0-9).";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must include at least one lowercase letter (a-z).";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must include at least one uppercase letter (A-Z).";
        }
        // Pattern.quote() is good practice if SPECIAL_CHAR_REGEX_PART could contain regex metacharacters
        if (!password.matches(".*[" + Pattern.quote(SPECIAL_CHAR_REGEX_PART) + "].*")) {
            return "Password must include a special character (e.g., " + SPECIAL_CHAR_SET_DISPLAY + ").";
        }
        return null; // All checks passed
    }

    private boolean isValidEmailPattern(String email){
        String emailRegex = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();

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