package com.example.miteshgandhi.mydietryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.miteshgandhi.mydietryapp.R;


public class MainActivity extends AppCompatActivity {

    private Button guest;
    private Button LoginUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        guest=(Button)findViewById(R.id.Guest);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,UploadPrescription.class);
                intent.putExtra("usertype","guest");
                startActivity(intent);

            }
        });










    }
}
