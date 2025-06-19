package avigdor.projectz.myapplication.classes;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String fname, lname, email, phone, uid, dateOfBirth;
    private int age;

    private static boolean isOnline ;
    public User(){}
    public User(String fname, String lname, String email, String phone, String uid, String dateOfBirth) {
        this.dateOfBirth=dateOfBirth;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
        this.isOnline = false;
        calculateAge();
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUid() {
        return uid;
    }

    public boolean isOnline() {
        return isOnline;
    }
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    private void calculateAge(){
        Calendar today=Calendar.getInstance();
        Calendar birthDate=Calendar.getInstance();
        birthDate.set(Integer.parseInt(dateOfBirth.substring(0,4)),
                Integer.parseInt(dateOfBirth.substring(4,6)),
                Integer.parseInt(dateOfBirth.substring(6,8)));
        this.age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            this.age--;
        }
    }





}
