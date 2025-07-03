package avigdor.projectz.myapplication.classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBRef {
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();

    public static String userID = refAuth.getCurrentUser().getUid();
    public static FirebaseDatabase FBDP = FirebaseDatabase.getInstance();
    public static DatabaseReference refUser = FBDP.getReference("Users");
    public static DatabaseReference refTasks = FBDP.getReference("Tasks");
}
