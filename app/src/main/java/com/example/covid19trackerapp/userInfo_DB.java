package com.example.covid19trackerapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userInfo_DB {
    private DatabaseReference databaseReference;

    public userInfo_DB(){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        databaseReference=db.getReference(userInfo_DB.class.getSimpleName());

    }

    public Task<Void> add(userInfo_Adapter user){
       return databaseReference.push().setValue(user);
    }
}
