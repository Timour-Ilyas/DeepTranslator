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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api-free.deepl.com/v2/translate?auth_key=77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx&text=ciao%20mondo&target_lang=FR";

        HashMap<String,String> lingue = new HashMap<String,String>();

        lingue.put("Italiano","IT");
        lingue.put("Inglese","EN");
        lingue.put("Francese","FR");
        lingue.put("Spagnolo","ES");
        lingue.put("Russo","RU");
        lingue.put("Cinese","ZH");
        lingue.put("Giapponese","JA");
        lingue.put("Tedesco","DE");
        lingue.put("Portoghese","PT");



        String[] lingua1Array = new String[] {
                "Italiano", "Inglese", "Francese", "Spagnolo", "Russo", "Cinese", "Giapponese", "Tedesco", "Portoghese"
        };

        String[] acronLingue = new String[] {
                "IT", "EN", "FR", "ES", "RU", "ZH", "JA", "DE", "PT"
        };

        Spinner lingua1 = (Spinner) findViewById(R.id.spinner1);
        Spinner lingua2 = (Spinner) findViewById(R.id.spinner2);
        TextView tvLingua1 = (TextView) findViewById(R.id.tvLingua1);
        TextView tvLingua2 = (TextView) findViewById(R.id.tvLingua2);
        EditText editText1 = (EditText) findViewById(R.id.EditText1);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        ImageView suono1 = (ImageView) findViewById(R.id.Suono1);
        ImageView suono2 = (ImageView) findViewById(R.id.Suono2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lingua1Array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lingua1.setAdapter(adapter);
        lingua2.setAdapter(adapter);

        lingua1.setSelection(1);
        lingua2.setSelection(2);

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
                pos = lingua1.getSelectedItemPosition();
                scambiato = true;
                lingua1.setSelection(lingua2.getSelectedItemPosition());
                lingua2.setSelection(pos);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ripeti(1);

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ripeti(2);

            }
        });


        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);

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





















        buttonTraduci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button4.setClickable(true);
                tvLingua2.setVisibility(View.VISIBLE);
                suono2.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                editText2.setClickable(false);
                String dominio = "https://api-free.deepl.com/v2/translate?";
                String auth_key = "auth_key=77cbbe56-2d6a-5990-0a0d-795fac3884c5:fx&";
                String text = "text=" + (editText1.getText().toString()).replace(" ","%20") + "&";



                String target_lang = "target_lang=" + lingue.get(lingua2.getSelectedItem());
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

                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                /**TRADUZIONE DEL TESTO E STAMPA SUL SECONDO EDIT TEXT**/

                //editText2.setText("Hey how's it going, is everything good?");
                testo2 = editText2.getText().toString();

            }
        });

























        lingua1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if(scambiato == false) {
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
                    if(scambiato == false) {
                        Toast.makeText(MainActivity.this, item.toString(),
                                Toast.LENGTH_SHORT).show();
                        scambiato = false;
                    }
                }

                tvLingua2.setText(lingua2.getSelectedItem().toString());

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
                    testo1 = editText1.getText().toString();
                }else if(s.length() == 0 || ((editText1.getText().toString()).trim().isEmpty() && (editText1.getText().toString()).matches("[\\n\\r]+")))
                    suono1.setVisibility(View.GONE);
            }

        });


    }
    private void ripeti(int lingua1oLingua2) {



        mTTS.setPitch(1f);
        mTTS.setSpeechRate(1f);

        if(lingua1oLingua2 == 1) {
            mTTS.speak(testo1, TextToSpeech.QUEUE_FLUSH, null);
        }else{
            mTTS.speak(testo2, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

}