package com.example.deeptranslator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    boolean scambiato = true;

    private TextToSpeech mTTS;

    private String testo1;
    private String testo2;
    private boolean start = true;
    private boolean avanti = false;

    private String authKey = "77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx";
    private EditText boxInserimento;
    private EditText boxTradotto;
    private HashMap<String,String> lingue;
    private HashMap<String,String> lingueVocali;

    private boolean inseritoQualcosa = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        lingue = new HashMap<String,String>();

        lingue.put("Rileva Lingua","");
        lingue.put("Bulgarian","BG");
        lingue.put("Czech","CS");
        lingue.put("Danish","DA");
        lingue.put("German","DE");
        lingue.put("Greek","EL");
        lingue.put("English","EN");
        lingue.put("Spanish","ES");
        lingue.put("Estonian","ET");
        lingue.put("Finnish","FI");
        lingue.put("French","FR");
        lingue.put("Hungarian","HU");
        lingue.put("Italian","IT");
        lingue.put("Japanese","JA");
        lingue.put("Lithuanian","LT");
        lingue.put("Latvian","LV");
        lingue.put("Dutch","NL");
        lingue.put("Polish","PL");
        lingue.put("Portuguese (Brazilian)","PT-BR");
        lingue.put("Portuguese (European)","PT-PT");
        lingue.put("Romanian","RO");
        lingue.put("Russian","RU");
        lingue.put("Slovak","SK");
        lingue.put("Slovenian","SL");
        lingue.put("Swedish","SV");
        lingue.put("Chinese","ZH");





        //Si crea un dizionario per associare alle lingue la eventualmente disponibile riproduzione vocale
        lingueVocali = new HashMap<String,String>();

        lingueVocali.put("English","UK");
        lingueVocali.put("Italian","ITALIAN");
        lingueVocali.put("Chinese","CHINESE");
        lingueVocali.put("French","FR");
        lingueVocali.put("Japanese","JP");
        lingueVocali.put("German","DE");









        //Si definiscono le lingue sia per il primo che per il secondo spinner
        String[] lingua1Array = new String[] {
                "Rileva Lingua", "Bulgarian", "Czech", "Danish", "German", "Greek", "English", "Spanish", "Estonian",
                "Finnish", "French", "Hungarian", "Italian", "Japanese", "Lithuanian", "Latvian", "Dutch", "Polish", "Portuguese (Brazilian)",
                "Portuguese (European)", "Romanian", "Russian", "Slovak", "Slovenian", "Swedish", "Chinese"
        };
        String[] lingua2Array = new String[] {
                "Bulgarian", "Czech", "Danish", "German", "Greek", "English", "Spanish", "Estonian", "Finnish",
                "French", "Hungarian", "Italian", "Japanese", "Lithuanian", "Latvian", "Dutch", "Polish", "Portuguese (Brazilian)", "Portuguese (European)",
                "Romanian", "Russian", "Slovak", "Slovenian", "Swedish", "Chinese"
        };


        String[] acronLingue = new String[] {
                "IT", "EN", "FR", "ES", "RU", "ZH", "JA", "DE", "PT"
        };

        Spinner lingua1 =  findViewById(R.id.spinner1);
        Spinner lingua2 =  findViewById(R.id.spinner2);

        TextView tvLingua1 =  findViewById(R.id.tvLingua1);
        TextView tvLingua2 =  findViewById(R.id.tvLingua2);

        boxInserimento =  findViewById(R.id.etInserimentoMessaggio);
        boxTradotto =  findViewById(R.id.etMessaggioTradotto);
        boxTradotto.setKeyListener(null) ;



        ConstraintLayout cr = findViewById(R.id.ed);
        cr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                removeFocus();
                return true;//always return true to consume event
            }
        });


        //Animazioni per le edit Text
        ObjectAnimator animationAvanti = ObjectAnimator.ofFloat(boxInserimento, "translationX", 330f);
        animationAvanti.setDuration(500);
        ObjectAnimator animationIndietro = ObjectAnimator.ofFloat(boxInserimento, "translationX", 0);
        animationIndietro.setDuration(500);

        ObjectAnimator animationTradottoAvanti = ObjectAnimator.ofFloat(boxTradotto, "translationX", 975f);
        animationIndietro.setDuration(500);


        ImageView suono1 =  findViewById(R.id.Suono1);
        ImageView suono2 =  findViewById(R.id.Suono2);
        ImageView copiaClipBoardImmagine = findViewById(R.id.immagineCopiaMessaggioInserito);
        ImageView immagineCopia = findViewById(R.id.immaginePulsanteCopia);
        ImageView immaginePreferiti = findViewById(R.id.immaginePulsantePreferiti);
        TextView tvFunzioni2 = findViewById(R.id.TVfunzioni2);


        //Si definisce lo stile di apertura degli spinner e gli si assegna un array di valori
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, lingua1Array);
        adapter1.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, lingua2Array);
        adapter2.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        lingua1.setAdapter(adapter1);
        lingua2.setAdapter(adapter2);

        lingua1.setSelection(0);
        lingua2.setSelection(1);

        start = false;
        scambiato = false;

        Button button1 = findViewById(R.id.pulsanteScambiaLingue);
        Button buttonCopia = findViewById(R.id.pulsanteDiCopia2);
        Button buttonCopia2 = findViewById(R.id.pulsanteDiCopia);
        Button button3 = findViewById(R.id.pulsanteVisualizzaPrimaLingua);
        Button button4 = findViewById(R.id.pulsanteVisualizzaSecondaLingua);

        Button buttonSalvataggio = findViewById(R.id.pulsanteDiSalvataggio);

        Button buttonTraduci = findViewById(R.id.buttonTraduci);
        tvLingua1.setText(lingua1.getSelectedItem().toString());


        //si definisce il colore del testo dell'oggetto selezionato nello spinner per la scelta della lingua
        lingua1.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeFocus();
                return false;
            }

        });



        lingua2.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeFocus();
                return false;
            }

        });


        buttonCopia.setOnClickListener(v -> {
            removeFocus();
            getApplicationContext();
            ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", boxInserimento.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copiato!", Toast.LENGTH_LONG).show();
        });

        buttonCopia2.setOnClickListener(v -> {
            removeFocus();
            getApplicationContext();
            ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", boxTradotto.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copiato!", Toast.LENGTH_LONG).show();
        });





        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();

                if(lingua1.getSelectedItemPosition() != 0) {
                    int pos;
                    String appoggio = boxInserimento.getText().toString();
                    String appoggioTesto = testo1;

                    pos = lingua1.getSelectedItemPosition();
                    scambiato = true;

                    lingua1.setSelection(lingua2.getSelectedItemPosition() + 1);
                    boxInserimento.setText(boxTradotto.getText().toString());
                    testo1 = testo2;

                    lingua2.setSelection(pos - 1);
                    boxTradotto.setText(appoggio);
                    testo2 = appoggioTesto;
                }else{
                    Toast.makeText(MainActivity.this, "Per poter scambiare scegli la lingua di partenza",
                            Toast.LENGTH_LONG).show();
                }

            }
        });



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();
                ripeti(1, lingua1.getSelectedItem().toString());

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();
                ripeti(2, tvLingua2.getText().toString());

            }
        });





















        buttonTraduci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();
                buttonCopia2.setClickable(true);
                buttonCopia2.setVisibility(View.VISIBLE);

                copiaClipBoardImmagine.setVisibility(View.VISIBLE);

                tvLingua2.setVisibility(View.VISIBLE);


                if(lingueVocali.get(lingua2.getSelectedItem().toString()) != null) {
                    suono2.setVisibility(View.VISIBLE);
                    button4.setClickable(true);

                }else {
                    suono2.setVisibility(View.INVISIBLE);
                    button4.setClickable(false);
                }

                boxTradotto.setVisibility(View.VISIBLE);
                boxTradotto.setClickable(false);

                copiaClipBoardImmagine.setVisibility(View.VISIBLE);
                immaginePreferiti.setVisibility(View.VISIBLE);


                String dominio = "https://api-free.deepl.com/v2/translate?";
                String auth_key = "auth_key=" + authKey + "&";
                String text = "text=" + (boxInserimento.getText().toString()).replace(" ","%20") + "&";
                String target_lang = "target_lang=" + lingue.get(lingua2.getSelectedItem());

                String url = "";

                System.out.println("Leroleroleroleroelro: " + lingue.get(lingua1.getSelectedItem()));

                if(lingua1.getSelectedItemPosition() != 0) {
                    String source_lang = "source_lang=" + lingue.get(lingua1.getSelectedItem()) + "&";
                    url = dominio + auth_key + text + source_lang + target_lang;
                }else{
                    url = dominio + auth_key + text + target_lang;
                }




                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("API",response);

                                try
                                {
                                    JSONObject j = new JSONObject(response);
                                    JSONArray a = j.getJSONArray("translations");
                                    JSONObject testoJSON = a.getJSONObject(0);

                                    String t = null;
                                    try {
                                        t = new String((testoJSON.getString("text")).getBytes("ISO-8859-1"), "UTF-8");
                                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                                        unsupportedEncodingException.printStackTrace();
                                    }


                                    boxTradotto.setText(t);

                                    testo2 = boxTradotto.getText().toString();

                                }
                                catch(JSONException e)
                                {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        boxTradotto.setText("That didn't work!");
                    }
                });

                /**TRADUZIONE DEL TESTO E STAMPA SUL SECONDO EDIT TEXT**/

                tvLingua2.setText(lingua2.getSelectedItem().toString());
                //editText2.setText("Hey how's it going, is everything good?");


                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                animationTradottoAvanti.start();
                tvFunzioni2.setVisibility(View.VISIBLE);
                immaginePreferiti.setVisibility(View.VISIBLE);
                immagineCopia.setVisibility(View.VISIBLE);
                buttonCopia2.setVisibility(View.VISIBLE);
                buttonSalvataggio.setVisibility(View.VISIBLE);





            }
        });












        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {


                    int result = mTTS.setLanguage(Locale.ITALIAN);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Linguaggio non supportato");
                    } else {

                    }
                } else {
                    Log.e("TTS", "Inizializzazione fallita");
                }
            }
        });












        lingua1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if(!scambiato && !start) {
                        Toast.makeText(MainActivity.this, item.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                tvLingua1.setText(lingua1.getSelectedItem().toString());
                //Se si cambia lingua quando il testo è già scritto si controlla che la lingua sia riproducibile o meno dal sintetizzatore
                if(inseritoQualcosa){
                    if (lingueVocali.get(lingua1.getSelectedItem().toString()) != null) {
                        suono1.setVisibility(View.VISIBLE);
                        button3.setClickable(true);
                        Log.e("CIAO_1: ", "Sono passato dall'if, " + lingueVocali.get(lingua1.getSelectedItem().toString()));

                    } else {
                        suono1.setVisibility(View.INVISIBLE);
                        button3.setClickable(false);
                        Log.e("CIAO_2: ", "Sono passato dall'else, " + lingueVocali.get(lingua1.getSelectedItem().toString()));
                    }

                }else{
                    suono1.setVisibility(View.GONE);
                    button3.setClickable(false);
                    Log.e("hellooooooO: ", "Sono passato dall'elseMAGGIORE, " + lingueVocali.get(lingua1.getSelectedItem().toString()));
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        lingua2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if(!scambiato && !start) {
                        Toast.makeText(MainActivity.this, item.toString(),
                                Toast.LENGTH_SHORT).show();
                        scambiato = false;
                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        boxInserimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!avanti) {
                    animationAvanti.start();
                    spostaTesto();
                    avanti = true;
                }else{
                    animationIndietro.start();
                    avanti = false;
                }
            }
        });
/*
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(avanti == false) {
                    animationAvanti.start();
                    avanti = true;
                }else{
                    animationIndietro.start();
                    avanti = false;
                }
            }
        });*/

        boxInserimento.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.length() != 0 && !((boxInserimento.getText().toString()).trim().isEmpty() && (boxInserimento.getText().toString()).matches("[\\n\\r]+"))){
                    //Si mostra il bottone per replicare vocalmente solo se la lingua è presente tra quelle sintetizzabili
                    if(lingueVocali.get(lingua1.getSelectedItem()) != null) {
                        suono1.setVisibility(View.VISIBLE);
                        button3.setClickable(true);
                        Log.e("SIUM1: ", "Sono stato dal barbiere1, " + lingua1.getSelectedItem().toString());

                    }else {
                        suono1.setVisibility(View.INVISIBLE);
                        button3.setClickable(false);
                        Log.e("SIUM2: ", "Sono stato dal dentista2, " + lingueVocali.get(lingua1.getSelectedItem()));

                    }
                    inseritoQualcosa = true;
                    testo1 = boxInserimento.getText().toString();
                }else if(s.length() == 0 || ((boxInserimento.getText().toString()).trim().isEmpty() && (boxInserimento.getText().toString()).matches("[\\n\\r]+"))) {
                    suono1.setVisibility(View.GONE);
                    button3.setClickable(false);
                    inseritoQualcosa = false;
                    Log.e("KEKZ: ", "Sono stato dal dentista965");
                }

            }

        });


    }
    private void ripeti(int lingua1oLingua2, String lingua) {

        System.out.println("lingua: " + lingua);



        int result = 0;
        switch(lingua){
            case "Italian":
                result = mTTS.setLanguage(Locale.ITALIAN);
                break;

            case "Chinese":
                result = mTTS.setLanguage(Locale.CHINESE);
                break;

            case "French":

                result = mTTS.setLanguage(Locale.FRENCH);
                break;

            case "German":
                result = mTTS.setLanguage(Locale.GERMAN);
                break;

            case "Japanese":
                result = mTTS.setLanguage(Locale.JAPANESE);
                break;

            case "English":
                result = mTTS.setLanguage(Locale.UK);
                break;
            default:
                break;
        }

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            //Log.e("TTS", "Linguaggio non supportato");
            Toast.makeText(MainActivity.this, "Linguaggio non supportato", Toast.LENGTH_LONG).show();

        } else {

            mTTS.setPitch(1f);
            mTTS.setSpeechRate(1f);

            if (lingua1oLingua2 == 1) {
                mTTS.speak(testo1, TextToSpeech.QUEUE_FLUSH, null);
            } else {
                mTTS.speak(testo2, TextToSpeech.QUEUE_FLUSH, null);
            }
        }

    }
    public void removeFocus(){
        boxInserimento.clearFocus();
        float scale = getResources().getDisplayMetrics().density;
        int pLeft = (int) (130*scale + 0.5f);
        int pTop = (int) (10*scale + 0.5f);
        boxInserimento.setPadding(pLeft,pTop,0,0);
        boxInserimento.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);


    }

    public void spostaTesto(){

        boxInserimento.setPadding(125,10,125,0);
        boxInserimento.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}

