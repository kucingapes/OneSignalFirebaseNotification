package com.utsman.kucingapes.fbtest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TerimaData extends AppCompatActivity {
    ImageView imageView;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terima_data);

        final Intent intent = new Intent();
        String data = intent.getStringExtra("datanye");
        String img = intent.getStringExtra("img");

        imageView = findViewById(R.id.imgTerima);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String url = dataSnapshot.getValue(String.class);
                String url = dataSnapshot.child("img").getValue(String.class);

                Glide.with(TerimaData.this)
                        .load(url)
                        .into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(TerimaData.this, MainActivity.class);
                startActivity(intent1);
            }
        }, 5000);*/
    }
}
