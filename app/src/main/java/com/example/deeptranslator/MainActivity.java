package com.example.deeptranslator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    boolean scambiato = true;

    private TextToSpeech mTTS;
    private EditText mEditText;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak;

    private String testo1;
    private String testo2;
    private boolean start = true;

    String authKey = "77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api-free.deepl.com/v2/translate?auth_key=77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx&text=ciao%20mondo&target_lang=FR";

        HashMap<String,String> lingue = new HashMap<String,String>();


        lingue.put("Rileva Lingua","");
        lingue.put("Bulgarian","BG");
        lingue.put("Czech","CS");
        lingue.put("Danish","DA");
        lingue.put("German","DE");
        lingue.put("Greek","EL");
        lingue.put("English UK","EN-GB");
        lingue.put("English US","EN-US");
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






        HashMap<String,String> lingueVocali = new HashMap<String,String>();

        lingue.put("English","ENGLISH");
        lingue.put("Italian","ITALIAN");
        lingue.put("Chinese","CHINESE");
        lingue.put("French","FR");
        lingue.put("Greek","EL");
        lingue.put("English (British)","EN-GB");
        lingue.put("English (American)","EN-US");
        lingue.put("Spanish","ES");
        lingue.put("Estonian","ET");










        String[] lingua1Array = new String[] {
                "Rileva Lingua", "Bulgarian", "Czech", "Danish", "German", "Greek", "English UK", "English US", "Spanish", "Estonian", "Finnish",
                "French", "Hungarian", "Italian", "Japanese", "Lithuanian", "Latvian", "Dutch", "Polish", "Portuguese (Brazilian)", "Portuguese (European)",
                "Romanian", "Russian", "Slovak", "Slovenian", "Swedish", "Chinese"
        };
        String[] lingua2Array = new String[] {
                "Bulgarian", "Czech", "Danish", "German", "Greek", "English (British)", "English (American)", "Spanish", "Estonian", "Finnish",
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
        EditText editText1 =  findViewById(R.id.EditText1);
        EditText editText2 =  findViewById(R.id.editText2);
        ImageView suono1 =  findViewById(R.id.Suono1);
        ImageView suono2 =  findViewById(R.id.Suono2);

        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView5 = findViewById(R.id.imageView5);


        //Si definisce lo stile di apertura degli spinner e gli si assegna un array di valori
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lingua1Array);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lingua2Array);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lingua1.setAdapter(adapter1);
        lingua2.setAdapter(adapter2);

        lingua1.setSelection(0);
        lingua2.setSelection(1);

        start = false;
        scambiato = false;

        Button button1 = (Button) findViewById(R.id.button1);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        Button buttonTraduci = (Button) findViewById(R.id.buttonTraduci);
        tvLingua1.setText(lingua1.getSelectedItem().toString());

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos;
                String appoggio = editText1.getText().toString();
                String appoggioTesto = testo1;

                pos = lingua1.getSelectedItemPosition();
                scambiato = true;

                lingua1.setSelection(lingua2.getSelectedItemPosition());
                editText1.setText(editText2.getText().toString());
                testo1 = testo2;

                lingua2.setSelection(pos+1);
                editText2.setText(appoggio);
                testo2 = appoggioTesto;

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ripeti(1, lingua1.getSelectedItem().toString());

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ripeti(2, tvLingua2.getText().toString());

            }
        });















        buttonTraduci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button4.setClickable(true);
                tvLingua2.setVisibility(View.VISIBLE);
                suono2.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                editText2.setClickable(false);
                imageView3.setVisibility(View.VISIBLE);
                imageView5.setVisibility(View.VISIBLE);
                String dominio = "https://api-free.deepl.com/v2/translate?";
                String auth_key = "auth_key=" + authKey + "&";
                String text = "text=" + (editText1.getText().toString()).replace(" ","%20") + "&";
                String target_lang = "target_lang=" + lingue.get(lingua2.getSelectedItem());

                if(lingua1.getSelectedItemPosition() != 0) {
                    String source_lang = "source_lang=" + lingue.get(lingua1.getSelectedItem() + "&");
                    String url = dominio + auth_key + text + source_lang + target_lang;
                }else{
                    String url = dominio + auth_key + text + target_lang;
                }


                String url = dominio + auth_key + text + target_lang;

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


                                    editText2.setText(t);
                                    System.out.println("Lezzo:  " + editText2.getText().toString());
                                    testo2 = editText2.getText().toString();
                                    System.out.println("Lezzo2:  " + testo2);
                                }
                                catch(JSONException e)
                                {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        editText2.setText("That didn't work!");
                    }
                });

                /**TRADUZIONE DEL TESTO E STAMPA SUL SECONDO EDIT TEXT**/

                tvLingua2.setText(lingua2.getSelectedItem().toString());
                //editText2.setText("Hey how's it going, is everything good?");


                // Add the request to the RequestQueue.
                queue.add(stringRequest);




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
                    if(scambiato == false && start == false) {
                        Toast.makeText(MainActivity.this, item.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                tvLingua1.setText(lingua1.getSelectedItem().toString());

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
                    if(scambiato == false && start == false) {
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

        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && !((editText1.getText().toString()).trim().isEmpty() && (editText1.getText().toString()).matches("[\\n\\r]+"))){
                    suono1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);

                    testo1 = editText1.getText().toString();
                }else if(s.length() == 0 || ((editText1.getText().toString()).trim().isEmpty() && (editText1.getText().toString()).matches("[\\n\\r]+")))
                    suono1.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);


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

            case "English UK":
                result = mTTS.setLanguage(Locale.UK);
                break;

            case "English US":
                result = mTTS.setLanguage(Locale.US);
                break;
            default:

                break;
        }

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "Linguaggio non supportato");
        } else {

        }

        mTTS.setPitch(1f);
        mTTS.setSpeechRate(1f);

        if(lingua1oLingua2 == 1) {
            mTTS.speak(testo1, TextToSpeech.QUEUE_FLUSH, null);
        }else{
            mTTS.speak(testo2, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

}