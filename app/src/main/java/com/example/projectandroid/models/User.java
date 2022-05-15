package com.example.projectandroid.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class User {
    private String uid;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String age;
    private String category;
    private String phone;
    private String year;

    public User() {}


    public String getUid() {
        return uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {

        this.firstname = firstname;
        return this;
    }


    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAge() {
        return age;
    }

    public User setAge(String age) {
        this.age = age;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public User setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getYear() {
        return year;
    }

    public User setYear(String year) {
        this.year = year;
        return this;
    }

    static public void getUser(String id, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference document = db.collection("professors").document("r");
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> userDoc = task.getResult().getData();
                    Log.d("Get user", "DocumentSnapshot data: " + userDoc.get("email"));

                    if (userDoc != null) {
                        user.setEmail((String) userDoc.get("email"))
                                .setPhone((String) userDoc.get("phone"))
                                .setFirstname((String) userDoc.get("firstname"))
                                .setLastname((String) userDoc.get("lastname"))
                                .setAge((String) userDoc.get("age"))
                                .setYear((String) userDoc.get("year"));
                        Log.d("Get user", "DocumentSnapshot data: " + userDoc);
                    } else {
                        Log.d("Get user", "No such document");
                    }
                } else {
                    Log.d("Get user", "get failed with ", task.getException());
                }
            }
        });

}}
