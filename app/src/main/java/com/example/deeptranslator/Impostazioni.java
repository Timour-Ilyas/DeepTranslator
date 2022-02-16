package com.example.deeptranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Impostazioni extends AppCompatActivity {
    Spinner lingua1;
    Spinner lingua2;
    TextView authKey;
    Button buttonSalva;
    static SharedPreferences sp;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        lingua1 =  findViewById(R.id.LinguaPrefOrig);
        lingua2 =  findViewById(R.id.LinguaPrefDest);
        authKey =  findViewById(R.id.TestoAuthKey);

        buttonSalva = findViewById(R.id.buttonSalva);
        Button buttonIndietro = findViewById(R.id.pulsanteIndietro);
        buttonIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Si definisce lo stile di apertura degli spinner e gli si assegna un array di valori
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, MainActivity.lingua1Array);
        adapter1.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, MainActivity.lingua2Array);
        adapter2.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        lingua1.setAdapter(adapter1);
        lingua2.setAdapter(adapter2);


        sp = getSharedPreferences("impostazioni",MODE_PRIVATE);
        int sl = sp.getInt("source_lang",0);
        int tl = sp.getInt("target_lang",1);
        String key = sp.getString("key","77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx");

        lingua1.setSelection(sl);
        lingua2.setSelection(tl);
        authKey.setText(key);

        buttonSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salva();
            }
        });


    }

    public void salva(){
        int source_lang = lingua1.getSelectedItemPosition();
        int target_lang = lingua2.getSelectedItemPosition();
        String key = authKey.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences("impostazioni",MODE_PRIVATE).edit();
        editor.putInt("source_lang",source_lang);
        editor.putInt("target_lang",target_lang);
        editor.putString("key",key);
        editor.commit();

        MainActivity.lingua1.setSelection(source_lang);
        MainActivity.lingua2.setSelection(target_lang);
        MainActivity.authKey = key;


    }
}