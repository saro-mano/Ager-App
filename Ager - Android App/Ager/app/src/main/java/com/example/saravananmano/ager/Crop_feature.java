package com.example.saravananmano.ager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

public class Crop_feature extends AppCompatActivity {

    Button button;
    TextView info,backinfo;
    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_feature);
        backinfo = (TextView)findViewById(R.id.more_details);
        button = (Button)findViewById(R.id.button);
        info = (TextView)findViewById(R.id.details);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(Crop_feature.this, button);
                pop.getMenuInflater().inflate(R.menu.popup_menu,pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        test = item.getTitle().toString();
                        if(test.equals("Carrot")){
                            info.setText("CARROT: \n Duration : 2 months\n Type : Short\n ");
                            backinfo.setText("Temperature : 20 C \n Humidity : 50% \n Soil Moisture : 80");
                        }
                        else if(test.equals("Cabbage")){
                            info.setText("CABBAGE: \n Duration : 2 months\n Type : Short\n ");
                            backinfo.setText("Temperature : 30-35 C \n Humidity : 70% \n Soil Moisture : 70");
                        }
                        else if(test.equals("Beetroot")){
                            info.setText("BEETROOT: \n Duration : 3 months\n Type : Short\n ");
                            backinfo.setText("Temperature : 20-25 C \n Humidity : 50% \n Soil Moisture : 80");
                        }
                        else if(test.equals("Mint")){
                            info.setText("MINT: \n Duration : 15 days\n Type : Short\n ");
                            backinfo.setText("Temperature : 20-30 C \n Humidity : 40% \n Soil Moisture : 80");
                        }
                        return true;
                    }
                });

                pop.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setVisibility(View.INVISIBLE);
                backinfo.setVisibility(View.VISIBLE);
            }
        });
        backinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backinfo.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);
            }
        });
    }
}
