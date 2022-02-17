package com.example.deeptranslator;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Cronologia extends AppCompatActivity {
    static LinearLayout layoutStringhe;
    int i = 0;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronologia);

        Button buttonIndietro = findViewById(R.id.bottoneIndietro2);
        buttonIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LinearLayout sv = (LinearLayout) findViewById(R.id.contenitoreStringhe);
        sv.setBackgroundColor(Color.TRANSPARENT);


        Button buttonXcronologia = findViewById(R.id.buttonEliminaCronologia);
        buttonXcronologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences spTraduzione = getSharedPreferences("traduzioni",MODE_PRIVATE);
                spTraduzione.edit().clear().commit();
                Integer nTrad = spTraduzione.getAll().size();

                //Si aggiorna la pagina rimuovendo tutta la cronologia
                layoutStringhe.removeAllViews();
                sv.setBackgroundColor(Color.TRANSPARENT);

            }
        });

        layoutStringhe = (LinearLayout) findViewById(R.id.contenitoreStringhe);



        Integer nTrad = MainActivity.spTraduzioni.getAll().size();
        //Si crea la textView da visualizzare
        Log.e("NUMERO: ", nTrad.toString());
        for(Integer i = 0; i < nTrad; i++) {
            if(i == 0){
                sv.setBackgroundColor(Color.parseColor("#8332ac"));
            }
            String traduzione = MainActivity.spTraduzioni.getString(i.toString(),null);
            String[] valori = traduzione.split("§");
            String lang1 = valori[0];
            String lang2 = valori[1];
            String txt1 = valori[2];
            String txt2 = valori[3];




            TextView tvAdd = new TextView(Cronologia.this);
            tvAdd.setBackgroundResource(R.drawable.textview_cliccabile);
            tvAdd.setText(lang1 + ": "+ txt1 + "\n\n" + lang2 + ": " + txt2);
           // tvAdd.setText("lingua " + i + ": Ciao, come va?\n\n" + "lingua " + (i + 1) + ": Hi, how is it going?");
            tvAdd.setHeight(220);
            tvAdd.setTextSize(15);
            tvAdd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvAdd.setGravity(Gravity.CENTER_VERTICAL);
            tvAdd.setTextColor(Color.WHITE); // for example
            layoutStringhe.addView(tvAdd);

        }




    }


    void aggiungiTVHistory(String lingua1, String lingua2, String testo1, String testo2){
        //Si crea la textView da visualizzare
        TextView tvAdd = new TextView(Cronologia.this);
        tvAdd.setBackgroundResource(R.drawable.edit_text_border);
        tvAdd.setText(lingua1 + ": "+ testo1 + "\n\n" + lingua2 + ": " + testo2);
        tvAdd.setHeight(220);
        tvAdd.setTextSize(15);
        tvAdd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvAdd.setGravity(Gravity.CENTER_VERTICAL);
        tvAdd.setTextColor(Color.WHITE); // for example
        layoutStringhe.addView(tvAdd);

    }
}