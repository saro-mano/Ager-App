package com.example.saravananmano.ager;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ManualMode extends AppCompatActivity {

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
        setContentView(R.layout.activity_manual_mode);
        Button openButton = (Button) findViewById(R.id.open);
        Button sendButton = (Button) findViewById(R.id.send);
        Button closeButton = (Button) findViewById(R.id.close);
        Button offButton = (Button) findViewById(R.id.off);
        Button fetchButton = (Button) findViewById(R.id.fetchButton);
        myLabel = (TextView) findViewById(R.id.label);
        moisture = (TextView) findViewById(R.id.moisture);
        myLabel.setText("Not Connected");

        //Open Button
        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    final ProgressDialog progress = new ProgressDialog(ManualMode.this);
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
                }
            }
        });

        //On button
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {

                }
            }
        });

        //Close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                }
            }
        });

        //Off Button
        offButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    offMotor();
                } catch (IOException ex) {
                }
            }
        });

        //Fetch Button
        fetchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    fetchData();
                }
                catch (IOException e){

                }
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
                    myLabel.setText("Bluetooth Connected");
                    break;
                }
            }
        }
    }

    void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        Toast.makeText(ManualMode.this,"Connection Successful!\uD83D\uDE07",Toast.LENGTH_SHORT).show();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        //beginListenForData();
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++) {
                                byte b = packetBytes[i];
                                if(b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            moisture.setText(data);
                                        }
                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }
    void sendData() throws IOException {
        mmOutputStream.write('a');
        Toast.makeText(ManualMode.this,"Motor ON\uD83D\uDC4D",Toast.LENGTH_SHORT).show();
        myLabel.setText("");
    }

    void offMotor() throws IOException {
        mmOutputStream.write('b');
        Toast.makeText(ManualMode.this,"Motor OFF\uD83D\uDC4E",Toast.LENGTH_SHORT).show();
        myLabel.setText("");
    }

    void closeBT() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth Closed");
    }

    void fetchData() throws IOException{
        mmOutputStream.write('b');
        InputStream tmpIn = null;
        tmpIn = mmSocket.getInputStream();
        mmInputStream = new DataInputStream(tmpIn);
        byte[] buffer = new byte[256];  // buffer store for the stream
        int bytes; // bytes returned from read()
        bytes = mmInputStream.read(buffer);
        String readMessage = new String(buffer, "US-ASCII");
        Log.d("Tag", readMessage);
        moisture.setText(readMessage);
    }
//    void sendData() throws IOException {
//        String msg = "a";
//        //mmOutputStream.write(msg.getBytes());
//        mmOutputStream.write('a');
//        Log.d("Sent","Sennt");
//    }
//    private void listview(){
//        BluetoothDevice result = null;
//        listView = (ListView) findViewById(R.id.listView);
//        List<String> s = new ArrayList<>();
//        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        for(BluetoothDevice bt : pairedDevices)
//            s.add(bt.getName());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list,s);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String value = (String)listView.getItemAtPosition(position);
//
//                Toast.makeText(MainActivity.this,"Name : " + value,Toast.LENGTH_LONG).show();
//            }
//        });
//    }



}