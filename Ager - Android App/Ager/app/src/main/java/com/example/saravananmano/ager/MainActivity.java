package com.example.saravananmano.ager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button detect;
    public Button motor;
    public void Onclick(){
        detect = (Button)findViewById(R.id.button2);
        detect.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Intent i = new Intent(MainActivity.this, Crop_feature.class);
                        startActivity(i);
                    }
                }
        );
        motor = (Button)findViewById(R.id.button3);
        motor.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, Auto_manual.class);
                        startActivity(i);
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Onclick();
    }
}
