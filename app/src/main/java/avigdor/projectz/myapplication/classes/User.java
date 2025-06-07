package avigdor.projectz.myapplication.classes;

import java.util.Date;

public class User {
    private String fname, lname, email, phone, uid;
    private int age;

    public User(String fname, String lname, String email, String phone, String uid, int age) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
        this.age = age;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUid() {
        return uid;
    }




}
