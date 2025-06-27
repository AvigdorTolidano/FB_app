package avigdor.projectz.myapplication.classes;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String fname, lname, email, uid;
    public User(){}
    public User(String fname, String lname, String email, String uid) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }
}
