package com.example.saravananmano.ager;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.io.DataInputStream;
import java.io.DataOutputStream;


import com.example.saravananmano.ager.R;

public class Auto_manual extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    public BluetoothDevice mmDevice;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    TextView myLabel;
    TextView moisture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_manual);
        Button openButton = (Button) findViewById(R.id.button);
        Button autoButton = (Button) findViewById(R.id.auto);
        Button manualButton = (Button) findViewById(R.id.manual);

        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    final ProgressDialog progress = new ProgressDialog(Auto_manual.this);
                    progress.setMessage("Connecting...");
                    progress.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progress.dismiss();
                        }
                    }, 2000);
                    findBT();
                    openBT();
                } catch (IOException ex) {
                    Log.d("Hey","Something wrong in onclick button");
                }
            }
        });

        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    setAutomatic();
                }
                catch (IOException ex){
                    Log.d("Error","Not working connection");
                }
            }
        });

        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Auto_manual.this,ManualMode.class);
                startActivity(i);
            }
        });
    }
    void findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            Toast.makeText(this,"No bluetooth adapter available",Toast.LENGTH_SHORT).show();
        }

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for(BluetoothDevice device : pairedDevices){
                //Log.d("Hey",device.getAddress().toString());
                if(device.getAddress().toString().equals("00:21:13:01:3C:15")){
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        Toast.makeText(Auto_manual.this,"Connection Successful!\uD83D\uDE07",Toast.LENGTH_SHORT).show();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        //beginListenForData();
    }

    void setAutomatic() throws IOException{
        mmOutputStream.write('o');
        Toast.makeText(Auto_manual.this,"Set to Automatic",Toast.LENGTH_SHORT).show();
    }
}
