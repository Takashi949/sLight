package com.example.sLight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity{
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.connect).setOnClickListener(this.onClickListener);
        findViewById(R.id.disconnect).setOnClickListener(this.onClickListener);
        findViewById(R.id.send).setOnClickListener(this.onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.connect){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (socket == null) socket = new Socket("192.168.11.101", 50545);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("MYAPP", e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
            else if(view.getId() == R.id.disconnect){
                try {
                    if(socket != null)socket.close();
                    socket = null;
                }
                catch (Exception e){
                    Log.e("MYAPP", e.getMessage());
                }
            }
            else if(view.getId() == R.id.send){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PrintWriter bufferedWriter = new PrintWriter(socket.getOutputStream(), true);
                            bufferedWriter.print(R.string.light_cmd);
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        }
    };
}
