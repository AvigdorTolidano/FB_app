package avigdor.projectz.myapplication;

import static avigdor.projectz.myapplication.classes.FBRef.refStorage;
import static avigdor.projectz.myapplication.classes.FBRef.refTasks;
import static avigdor.projectz.myapplication.classes.FBRef.userID;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import avigdor.projectz.myapplication.classes.FBRef;
import avigdor.projectz.myapplication.classes.Tasks;

public class AddTaskActivity extends AppCompatActivity {

    EditText start_et,end_et,desc_et,name_et,num_class_et,class_et,year_et;
    private static final int REQUEST_FULL_IMAGE_CAPTURE = 202;
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private String currentPath;
    StorageReference refImg;
    String schoolYear, keyID,start,end,desc,name,numClass,className;
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

        start = start_et.getText().toString();
        end = end_et.getText().toString();
        desc = desc_et.getText().toString();
        name = name_et.getText().toString();
        numClass = num_class_et.getText().toString();
        className = class_et.getText().toString();
        schoolYear = year_et.getText().toString();
        keyID = refTasks.push().getKey();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
        else {
            // creating local temporary file to store the full resolution photo
            String filename = "tempfile";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File imgFile = File.createTempFile(filename, ".jpg", storageDir);
                currentPath = imgFile.getAbsolutePath();
                Uri imageUri = FileProvider.getUriForFile(this, "avigdor.projectz.myapplication.fileprovider", imgFile);
                Intent takePicIntent = new Intent();
                takePicIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                if (takePicIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePicIntent, REQUEST_FULL_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create temporary file", Toast.LENGTH_LONG);
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data_back) {
        super.onActivityResult(requestCode, resultCode, data_back);

        if (resultCode == Activity.RESULT_OK) {

            final ProgressDialog pd;
            pd = ProgressDialog.show(this, "Upload image", "Uploading...", true);
            refImg = refStorage.child(keyID + ".jpg");
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            refImg.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Tasks task = new Tasks(keyID, className, numClass, name, desc, "active", start, end, keyID, schoolYear);
                            refTasks.child(userID).child(schoolYear).child(className + numClass).child(end).setValue(task);
                            finish();
                            Toast.makeText(AddTaskActivity.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            finish();
                            Toast.makeText(AddTaskActivity.this, "Upload failed", Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

}