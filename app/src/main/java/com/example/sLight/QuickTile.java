package com.example.sLight;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickTile extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.11.101", 50545);

                    PrintWriter bufferedWriter = new PrintWriter(socket.getOutputStream(), true);
                    bufferedWriter.print(R.string.light_cmd);
                    bufferedWriter.flush();

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("MYAPP", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
}
