package com.example.ehar.serviceexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button play = null;
    Button stop = null;
    private static final int PERMISSION_CODE = 999;
    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check permissions
        if (checkSelfPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_CODE
            );
        }
        else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE)
            if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // the user clicked "Allow"
                init();
            }
            else {
                Log.e(LOG_TAG, "Permission was not granted to read a file");
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop_option:
                stopService(
                        new Intent(MainActivity.this,
                                MusicService.class
                        ));
                return true;
            case R.id.restart_option:
                if (MusicService.getInstance() == null)
                    startService(
                            new Intent(MainActivity.this,
                                    MusicService.class
                            ));
                else
                    MusicService.getInstance().replay();
                return true;

            default:
                return false;
        }
    }

    void init() {
        play = (Button) findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // We don't want to start service if already
                // playing
                if (MusicService.getInstance() == null)
                    startService(
                        new Intent(MainActivity.this,
                        MusicService.class
                            ));
                else
                    MusicService.getInstance().replay();
            }
        });

        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(
                        new Intent(MainActivity.this,
                                MusicService.class
                        ));
            }
        });

    }
}
