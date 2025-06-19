package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.refAuth;
import static avigdor.projectz.myapplication.classes.FBRef.refBirthday;
import static avigdor.projectz.myapplication.classes.FBRef.refUser;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final String SPECIAL_CHAR_SET_DISPLAY = "@, #, $, %, ^, &, +, =, ?, !";
    private static final String SPECIAL_CHAR_REGEX_PART = "@#$%^&+=?!";
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,14}$";

    EditText email_et, password_et, confirmPassword_et, fname_et, lname_et, phone_et;
    String email, password, fname, lname, phone, confirmPassword, dateOfBirth;
    Button datePicker;
    DatePickerDialog datePickerDialog;

    User myUser;

    ProgressDialog pd;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

    }

    private void init() {

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.psw_et);
        confirmPassword_et = findViewById(R.id.cpsw_et);
        fname_et = findViewById(R.id.fname_et);
        lname_et = findViewById(R.id.lname_et);
        phone_et = findViewById(R.id.phone_et);
        datePicker = findViewById(R.id.date_picker);

        Calendar today=Calendar.getInstance();

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, -18);

        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -100);

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
                        dateOfBirth = String.format("%04d%02d%02d", year, month + 1, dayOfMonth);
                    }
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH) );

        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        pd = new ProgressDialog(context);
        pd.setTitle("Registration");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

    }
    public void submit(View view) {
        email = email_et.getText().toString();
        password = password_et.getText().toString();
        confirmPassword = confirmPassword_et.getText().toString();
        fname = fname_et.getText().toString();
        lname = lname_et.getText().toString();
        phone = phone_et.getText().toString();

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
       if (datePicker.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Age is missing.", Toast.LENGTH_LONG).show();
            datePicker.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Phone is missing.", Toast.LENGTH_LONG).show();
            phone_et.requestFocus();
            return;
        }
        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(context, "Please enter a valid phone number (e.g., 10-13 digits, optionally starting with '+').", Toast.LENGTH_LONG).show();
            phone_et.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Email is missing.", Toast.LENGTH_LONG).show();
            email_et.requestFocus();
            return;
        }
        if (!isValidEmailPattern(email)) {
            Toast.makeText(context, "Invalid email format.", Toast.LENGTH_SHORT).show();
            email_et.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Password is missing.", Toast.LENGTH_LONG).show();
            password_et.requestFocus();
            return;
        }

        String passwordError = getPasswordValidationError(password);

        if (passwordError != null) {
            Toast.makeText(context, passwordError, Toast.LENGTH_SHORT).show();
            password_et.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields. Confirm Password is missing.", Toast.LENGTH_LONG).show();
            confirmPassword_et.requestFocus();
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
                            myUser = new User(fname, lname, email, phone, user.getUid(), dateOfBirth);
                            refUser.child(user.getUid()).setValue(myUser);
                            refBirthday.child(dateOfBirth.substring(4,8)).child(user.getUid()).setValue(myUser.getUid());
                            Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show();
                            refAuth.signOut();
                        }
                        else{
                            Exeptions(task.getException());
                        }
                    }
            });
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches(PHONE_REGEX);
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

    public void pick(View view){
        datePickerDialog.show();
    }
}