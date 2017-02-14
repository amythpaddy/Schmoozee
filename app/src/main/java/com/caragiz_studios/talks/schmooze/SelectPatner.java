package com.caragiz_studios.talks.schmooze;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.caragiz_studios.talks.schmooze.helper.Constants;
import com.caragiz_studios.talks.schmooze.helper.db.firebase.UserDataModelFB;
import com.caragiz_studios.talks.schmooze.helper.db.realm.UserDataModelRealm;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Iterator;

import io.realm.Realm;

public class SelectPatner extends AppCompatActivity {

    EditText searchUser;
    EditText registerAs;
    EditText registerNumber;

    DatabaseReference dbRef;

    LinearLayout registerSelf;
    LinearLayout searchPartner;

    Boolean usernameAvailableFlag = true;

    ProgressDialog searchingProgress;

    UserDataModelFB userList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patner);

        searchUser = (EditText) findViewById(R.id.searchThisUser);
        registerAs = (EditText) findViewById(R.id.registerAsThisUser);
        registerNumber = (EditText) findViewById(R.id.registerWithThisNumber);

        registerSelf = (LinearLayout) findViewById(R.id.registerself);
        searchPartner = (LinearLayout) findViewById(R.id.findUser);
    }

    public void registerUser(View view) {
        searchingProgress = new ProgressDialog(this);
        searchingProgress.setMessage("Checking Availability");
        searchingProgress.show();
        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child(Constants.fbUserNameSearchTag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> ds = dataSnapshot.getChildren().iterator();
                while (ds.hasNext()) {
                    UserDataModelFB userData = ds.next().getValue(UserDataModelFB.class);
                    if (userData.getUsername().equalsIgnoreCase(registerAs.getText().toString())) {
                        usernameAvailableFlag = false;
                        userList = userData;
                        break;
                    }
                }

                registerUserResult();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerUserResult() {

        String key = String.valueOf(new Date().getTime());
        if (usernameAvailableFlag) {
            searchingProgress.setMessage("Registering User");
            UserDataModelFB userData = new UserDataModelFB(key + registerAs.getText().toString(), registerAs.getText().toString(), registerNumber.getText().toString());
            dbRef.child(Constants.fbUserNameSearchTag).child(key).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    animate();
                }
            });

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            UserDataModelRealm userDataRealm = new UserDataModelRealm();
            userDataRealm.setUserId(key + registerAs.getText().toString());
            realm.commitTransaction();
            realm.close();

        } else {


            if (registerNumber.getText().toString().equals(userList.getRegisterNumber())) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                UserDataModelRealm userDataRealm = new UserDataModelRealm();
                userDataRealm.setUserId(key + registerAs.getText().toString());
                realm.commitTransaction();
                realm.close();
                animate();
            } else {

                TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
                shake.setRepeatMode(Animation.REVERSE);
                shake.setRepeatCount(10);
                shake.setDuration(100);
                registerSelf.startAnimation(shake);
                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
            }
        }
        searchingProgress.dismiss();
    }

    private void animate() {
        registerSelf.animate()
                .alpha(0).scaleX(.1f).scaleY(.1f)
                .setDuration(1000)
                .setInterpolator(new AccelerateInterpolator())
                .start();
        searchPartner.setScaleX(.1f);
        searchPartner.setScaleY(.1f);
        searchPartner.setAlpha(0);
        searchPartner.setVisibility(View.VISIBLE);
        searchPartner.animate()
                .alpha(1).scaleX(1).scaleY(1)
                .setStartDelay(1000)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    String partnerId = null;

    public void findUser(View view) {
        searchingProgress = new ProgressDialog(this);
        searchingProgress.setMessage("Sending request to User...");
        searchingProgress.show();

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(Constants.fbUserNameSearchTag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> ds = dataSnapshot.getChildren().iterator();
                while (ds.hasNext()) {
                    UserDataModelFB userData = ds.next().getValue(UserDataModelFB.class);
                    if (userData.getUsername().equalsIgnoreCase(searchUser.getText().toString())) {
                        usernameAvailableFlag = true;
                        partnerId = userData.getUserid();
                        break;
                    }

                }

                connectUsers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void connectUsers() {
        if (partnerId == null) {
            searchingProgress.dismiss();
            Toast.makeText(this, "User Doesn't exist", Toast.LENGTH_SHORT).show();
        } else {
            searchingProgress.setMessage("Connecting ...");
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            UserDataModelRealm userData = realm.where(UserDataModelRealm.class).findFirst();
            if (userData == null) {
                userData = new UserDataModelRealm();
            }
            userData.setPartnerId(partnerId);
            userData.setRoomId(partnerId + userData.getUserId());
            realm.copyToRealmOrUpdate(userData);
            realm.commitTransaction();


            dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child(Constants.chatroomtag).child(userData.getRoomId()).setValue("Hello");

            searchingProgress.dismiss();
            startActivity(new Intent(this, LetsTalk.class));
            realm.close();
            finish();
        }
    }
}
