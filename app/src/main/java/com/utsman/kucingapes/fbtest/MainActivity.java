package com.utsman.kucingapes.fbtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "log nya";


    private TextView textView;
    private ImageView imageView;
    DatabaseReference databaseReference;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("topik");

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotifOneSignal())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //startService(new Intent(this, FCMService.class));
        textView = findViewById(R.id.judul);
        imageView = findViewById(R.id.img);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String url = dataSnapshot.getValue(String.class);
                String url = dataSnapshot.child("img").getValue(String.class);
                String msg = dataSnapshot.child("msg").getValue(String.class);

                Glide.with(MainActivity.this)
                        .load(url)
                        .into(imageView);

                textView.setText(msg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (getIntent().getExtras() != null){
            for (String key: getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString(key);
                String img = getIntent().getExtras().getString("imgUrl");
                textView.setText(img);
                //Log.i(TAG, img);

                if (key.equals("TerimaData") && value.equals("True")){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
                    databaseReference.child("img").setValue(img);
                    /*Intent intent = new Intent(this, TerimaData.class);
                    startActivity(intent);
                    finish();*/
                }
            }
        }
    }

    private class NotifOneSignal implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;

            String imgUrl = data.optString("url", null);
            String msg = data.optString("msg", null);
            Object activityToLaunch = MainActivity.class;

            databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
            databaseReference.child("img").setValue(imgUrl);
            databaseReference.child("msg").setValue(msg);

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("imgUrl", imgUrl);
            // startActivity(intent);
            startActivity(intent);
        }
    }
}
