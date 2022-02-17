package com.example.deeptranslator;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityVisualizzazioneIniziale extends AppCompatActivity {
    private Timer timer;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_visualizzazione_iniziale);

        i = new Intent(getApplicationContext(),MainActivity.class);
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        },1500);
    }
}