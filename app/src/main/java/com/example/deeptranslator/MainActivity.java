package com.example.deeptranslator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    boolean scambiato = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.google.com";

        String[] arraySpinner = new String[] {
                "Italiano", "Inglese", "Francese", "Spagnolo", "Russo", "Cinese", "Giapponese", "Tedesco"
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
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lingua1.setAdapter(adapter);
        lingua2.setAdapter(adapter);

        lingua1.setSelection(1);
        lingua2.setSelection(2);

        Button button1 = (Button) findViewById(R.id.button1);
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

        buttonTraduci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button4.setClickable(true);
                tvLingua2.setVisibility(View.VISIBLE);
                suono2.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                editText2.setClickable(false);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                editText2.setText("Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        editText2.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

        lingua1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
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
                }else if(s.length() == 0 || ((editText1.getText().toString()).trim().isEmpty() && (editText1.getText().toString()).matches("[\\n\\r]+")))
                    suono1.setVisibility(View.GONE);
                }
            
        });


    }
}