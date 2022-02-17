package com.example.deeptranslator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    boolean scambiato = true;

    private TextToSpeech mTTS;

    private String testo1;
    private String testo2;
    private boolean start = true;
    private boolean avanti = false;

    static String authKey = null;
    private EditText boxInserimento;
    private EditText boxTradotto;
    private HashMap<String,String> lingue;
    private HashMap<String,String> lingueVocali;

    private boolean inseritoQualcosa = false;
    boolean tradotto = false;
    public static String[] lingua1Array;
    public static String[] lingua2Array;

    TextView tvLingua1;
    TextView tvLingua2;

    static SharedPreferences spTraduzioni;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    static Spinner lingua1;
    static Spinner lingua2;
    private boolean salvato = false;


    //metodo per gestire i risultati della registrazione vocale
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                boxInserimento.setText(Objects.requireNonNull(result).get(0));

            }
        }
    }


//metodo per aggiungere la traduzione fatta alla cronologia
    public void aggiungiTraduzione(){
        String lang1 = lingua1.getSelectedItem().toString();
        String lang2 = lingua2.getSelectedItem().toString();
        String txt1 = boxInserimento.getText().toString();
        String txt2 = boxTradotto.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences("traduzioni",MODE_PRIVATE).edit();
        Integer nTrad = spTraduzioni.getAll().size();

        editor.putString(nTrad.toString(),(lang1+"§"+lang2+"§"+txt1+"§"+txt2));
        editor.commit();

        nTrad = spTraduzioni.getAll().size();


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        RequestQueue queue = Volley.newRequestQueue(this);

        spTraduzioni = getSharedPreferences("traduzioni",MODE_PRIVATE);






        //hashmap per contenere tutte le lingue con abbreviazione
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
        lingua1Array = new String[] {
                "Rileva Lingua", "Bulgarian", "Czech", "Danish", "German", "Greek", "English", "Spanish", "Estonian",
                "Finnish", "French", "Hungarian", "Italian", "Japanese", "Lithuanian", "Latvian", "Dutch", "Polish", "Portuguese (Brazilian)",
                "Portuguese (European)", "Romanian", "Russian", "Slovak", "Slovenian", "Swedish", "Chinese"
        };
        lingua2Array = new String[] {
                "Bulgarian", "Czech", "Danish", "German", "Greek", "English", "Spanish", "Estonian", "Finnish",
                "French", "Hungarian", "Italian", "Japanese", "Lithuanian", "Latvian", "Dutch", "Polish", "Portuguese (Brazilian)", "Portuguese (European)",
                "Romanian", "Russian", "Slovak", "Slovenian", "Swedish", "Chinese"
        };


        String[] acronLingue = new String[] {
                "IT", "EN", "FR", "ES", "RU", "ZH", "JA", "DE", "PT"
        };



        tvLingua1 =  findViewById(R.id.tvLingua1);
        tvLingua2 =  findViewById(R.id.tvLingua2);

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
        TextView tvFunzioni2 = findViewById(R.id.TVfunzioni2);

        lingua1 =  findViewById(R.id.spinner1);
        lingua2 =  findViewById(R.id.spinner2);

        //Si definisce lo stile di apertura degli spinner e gli si assegna un array di valori
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, lingua1Array);
        adapter1.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, lingua2Array);
        adapter2.setDropDownViewResource(R.layout.stile_spinner_dropdown);

        lingua1.setAdapter(adapter1);
        lingua2.setAdapter(adapter2);

        //Si impostano i valori salvati nelle impostazioni per gli spinner e la authkey
        Impostazioni.sp = getSharedPreferences("impostazioni",MODE_PRIVATE);
        int sl = Impostazioni.sp.getInt("source_lang",0);
        int tl = Impostazioni.sp.getInt("target_lang",1);
        authKey = Impostazioni.sp.getString("key","77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx");

        lingua1.setSelection(sl);
        lingua2.setSelection(tl);


        start = false;
        scambiato = false;

        Button button1 = findViewById(R.id.pulsanteScambiaLingue);
        Button buttonCopia = findViewById(R.id.pulsanteDiCopia2);
        Button buttonCopia2 = findViewById(R.id.pulsanteDiCopia);
        Button button3 = findViewById(R.id.pulsanteSuonoPrimaLingua);
        Button button4 = findViewById(R.id.pulsanteSuonoSecondaLingua);

        Button buttonRecord = findViewById(R.id.buttonMicrofono);





        //Si Crea la finestra di dialogo google per la registrazione vocale quando il bottone è premuto
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast
                            .makeText(MainActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });



        //Quando si preme il pulsante impostazioni si va nella corrispondente activity
        Button buttonImpostazioni = findViewById(R.id.pulsanteImpostazioni);
        buttonImpostazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Impostazioni.class);
                startActivity(i);

            }
          });



        Button buttonTraduci = findViewById(R.id.buttonTraduci);
        tvLingua1.setText(lingua1.getSelectedItem().toString());



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

        //Metodi per copiare i testi da tradurre e tradotto
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




        //onClickListener per cosa fare quando si preme il pulsante scambia lingua
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();

                if (lingua1.getSelectedItemPosition() != 0) {
                    int pos;
                    pos = lingua1.getSelectedItemPosition();
                    scambiato = true;

                    lingua1.setSelection(lingua2.getSelectedItemPosition() + 1);

                    lingua2.setSelection(pos - 1);

                } else {
                    Toast.makeText(MainActivity.this, "Per poter scambiare scegli la lingua di partenza",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        //Bottone per la riproduzione vocale del primo testo
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();
                ripeti(1, lingua1.getSelectedItem().toString());

            }
        });

        //Bottone per la riproduzione vocale del secondo testo
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();
                ripeti(2, tvLingua2.getText().toString());

            }
        });



        //Metodo per gestire cosa avviene quando si preme il pulsante per tradurre
        buttonTraduci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFocus();

                if(inseritoQualcosa) {

                    animationTradottoAvanti.start();
                    tvFunzioni2.setVisibility(View.VISIBLE);

                    immagineCopia.setVisibility(View.VISIBLE);

                    buttonCopia2.setClickable(true);
                    buttonCopia2.setVisibility(View.VISIBLE);


                    tvLingua2.setText(lingua2.getSelectedItem().toString());
                    copiaClipBoardImmagine.setVisibility(View.VISIBLE);

                    tvLingua2.setVisibility(View.VISIBLE);


                    if (lingueVocali.get(lingua2.getSelectedItem().toString()) != null) {
                        suono2.setVisibility(View.VISIBLE);
                        button4.setClickable(true);

                    } else {
                        suono2.setVisibility(View.INVISIBLE);
                        button4.setClickable(false);
                    }

                    boxTradotto.setVisibility(View.VISIBLE);
                    boxTradotto.setClickable(false);

                    copiaClipBoardImmagine.setVisibility(View.VISIBLE);


                    //si definiscono i dati per la richiesta
                    String dominio = "https://api-free.deepl.com/v2/translate?";
                    String auth_key = "auth_key=" + authKey + "&";
                    String text = "text=" + (boxInserimento.getText().toString()).replace(" ", "%20") + "&";
                    String target_lang = "target_lang=" + lingue.get(lingua2.getSelectedItem());

                    String url = "";



                    if (lingua1.getSelectedItemPosition() != 0) {
                        String source_lang = "source_lang=" + lingue.get(lingua1.getSelectedItem()) + "&";
                        url = dominio + auth_key + text + source_lang + target_lang;
                    } else {
                        url = dominio + auth_key + text + target_lang;
                    }

                    //Si invia la richiesta http e si gestisce la risposta
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("API", response);

                                    try {
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

                                        aggiungiTraduzione();
                                        tradotto = true;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Impossibile connettersi, verificare di essere connessi ad Internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    /**TRADUZIONE DEL TESTO E STAMPA SUL SECONDO EDIT TEXT**/



                    queue.add(stringRequest);






                }else{
                    Toast.makeText(MainActivity.this, "Inserisci il testo da tradurre",
                            Toast.LENGTH_SHORT).show();
                }





            }
        });




        //oggetto per la riproduzione vocale, si imposta la lingua iniziale
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



        //Si definiscono le azioni da eseguire e i controlli da fare quando una nuova lingua di origine viene scelta dallo spinner
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


                    } else {
                        suono1.setVisibility(View.INVISIBLE);
                        button3.setClickable(false);

                    }

                }else{
                    suono1.setVisibility(View.GONE);
                    button3.setClickable(false);

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        //Si definiscono le azioni da eseguire e i controlli da fare quando una nuova lingua di destinazione viene scelta dallo spinner
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

                tvLingua2.setText(lingua2.getSelectedItem().toString());
                if(tradotto){
                    if (lingueVocali.get(lingua2.getSelectedItem().toString()) != null) {
                        suono2.setVisibility(View.VISIBLE);
                        button4.setClickable(true);


                    } else {
                        suono2.setVisibility(View.INVISIBLE);
                        button4.setClickable(false);

                    }

                }else{
                    suono2.setVisibility(View.GONE);
                    button4.setClickable(false);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        //Si gestiscono le animazioni della prima edittext boxInserimento
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

        //Si inserisce un textWatcher sulla prima edittext così da tenere traccia dei cambiamenti del testo via via
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


                    }else {
                        suono1.setVisibility(View.INVISIBLE);
                        button3.setClickable(false);


                    }
                    inseritoQualcosa = true;
                    testo1 = boxInserimento.getText().toString();
                }else if(s.length() == 0 || ((boxInserimento.getText().toString()).trim().isEmpty() && (boxInserimento.getText().toString()).matches("[\\n\\r]+"))) {
                    suono1.setVisibility(View.GONE);
                    button3.setClickable(false);
                    inseritoQualcosa = false;

                }

            }

        });


    }

    //Metodo per ripetere vocalmente il messaggio passato come parametro nella lingua passata anch'essa come parametro
    private void ripeti(int lingua1oLingua2, String lingua) {

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

    //Si rimuove il focus dalla editText boxInserimento ogni qual volta venga cliccato qualcos'altro nella pagina
    //Metodo invocato in quasi ogni onClickListener per l'appunto
    public void removeFocus(){
        boxInserimento.clearFocus();
        float scale = getResources().getDisplayMetrics().density;
        int pLeft = (int) (130*scale + 0.5f);
        int pTop = (int) (10*scale + 0.5f);
        boxInserimento.setPadding(pLeft,pTop,0,0);
        boxInserimento.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);


    }

    //Metodo per spostare il testo delle editText in relazione con le animazioni
    public void spostaTesto(){

        boxInserimento.setPadding(125,10,125,0);
        boxInserimento.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}

