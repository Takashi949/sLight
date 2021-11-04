package com.example.myapplication;

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
    ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.connect).setOnClickListener(this.onClickListener);
        findViewById(R.id.disconnect).setOnClickListener(this.onClickListener);
        findViewById(R.id.send).setOnClickListener(this.onClickListener);

        connectivityManager = getSystemService(ConnectivityManager.class);
        Network network = connectivityManager.getActiveNetwork();

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                //Log.d("MYAPP", "AVai");
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                //Log.d("MYAPP", "Lost" + networkInfo.getType());
            }

            @Override
            public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
                //Log.d("MYAPP", linkProperties.getInterfaceName() + linkProperties.getLinkAddresses());
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectivityManager.registerDefaultNetworkCallback(null);
    }

    private void discon(){
        try {
            if(socket != null)socket.close();
            socket = null;
        }
        catch (Exception e){
            Log.e("MYAPP", e.getMessage());
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.connect){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (socket == null) socket = new Socket("192.168.11.101", 10545);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("MYAPP", e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
            else if(view.getId() == R.id.disconnect){
                discon();
            }
            else if(view.getId() == R.id.send){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PrintWriter bufferedWriter = new PrintWriter(socket.getOutputStream(), true);
                            bufferedWriter.print("lightonoff");
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        }
    };
}
