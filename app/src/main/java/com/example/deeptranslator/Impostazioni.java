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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        lingua1 =  findViewById(R.id.LinguaPrefDest);
        lingua2 =  findViewById(R.id.LinguaPrefOrig);
        authKey =  findViewById(R.id.TestoAuthKey);

        buttonSalva = findViewById(R.id.buttonSalva);

        //Si definisce lo stile di apertura degli spinner e gli si assegna un array di valori
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, MainActivity.lingua1Array);
        adapter1.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, MainActivity.lingua2Array);
        adapter2.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        lingua1.setAdapter(adapter1);
        lingua2.setAdapter(adapter2);

        lingua1.setSelection(0);
        lingua2.setSelection(1);

        SharedPreferences sp = getSharedPreferences("impostazioni",MODE_PRIVATE);
        int sl = sp.getInt("source_lang",0);
        int tl = sp.getInt("target_lang",1);
        String key = sp.getString("key",null);

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

    }
}