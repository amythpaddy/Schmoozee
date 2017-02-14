package com.caragiz_studios.talks.schmooze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.caragiz_studios.talks.schmooze.helper.Constants;
import com.caragiz_studios.talks.schmooze.helper.db.realm.UserDataModelRealm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;

public class LetsTalk extends AppCompatActivity implements ValueEventListener{

    Realm realm;
    DatabaseReference dbRef;
    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        UserDataModelRealm userData = realm.where(UserDataModelRealm.class).findFirst();
        dbRef.child(Constants.chatroomtag).child(userData.getRoomId()).addValueEventListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_talk);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.i("Say", dataSnapshot.getValue(String.class));

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
