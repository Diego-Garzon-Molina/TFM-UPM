package com.example.diego.baymax;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Diego on 14/06/2017.
 */

public class Alarma extends AppCompatActivity {
    MediaPlayer mp;
    int volume;
    AudioManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_alarma);
        TextView textView = (TextView) findViewById(R.id.medicinas_alarma);
        // this.setVolumeControlStream(AudioManager.STREAM_RING);
        mgr = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
        // mgr.setMode(AudioManager.MODE_RINGTONE);
        volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        textView.setText("Ha olvidado tomar sus medicinas.\n PREDNISONA KERN PHARMA Comp. 30 mg");
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, 50, 50);
        mp = MediaPlayer.create(this, R.raw.alarma);
        mp.setVolume(10.f, 10.f);
        mp.setLooping(true);
        mp.start();

        final Button button = (Button) findViewById(R.id.button_alarma);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent escanear = new Intent(getApplicationContext(), com.example.diego.realmedic.escanear.class);
                // startActivity(escanear);
                mp.stop();
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 50);
            }
        });
    }

    @Override
    protected void onStop() {
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, volume);
        mp.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, volume);
        mp.stop();
        super.onDestroy();
    }
}
