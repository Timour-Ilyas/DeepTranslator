package com.example.deeptranslator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        String[] arraySpinner = new String[] {
                "Italiano", "Inglese", "Francese", "Spagnolo", "Russo", "Cinese", "Giapponese", "Tedesco"
        };

        Spinner lingua1 = (Spinner) findViewById(R.id.spinner1);
        Spinner lingua2 = (Spinner) findViewById(R.id.spinner2);
        TextView tvLingua1 = (TextView) findViewById(R.id.tvLingua1);
        EditText editText1 = (EditText) findViewById(R.id.EditText1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lingua1.setAdapter(adapter);
        lingua2.setAdapter(adapter);

        lingua1.setSelection(1);
        lingua2.setSelection(2);

        TextView button1 = (Button) findViewById(R.id.button1);
        tvLingua1.setText(lingua1.getSelectedItem().toString());

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos;
                pos = lingua1.getSelectedItemPosition();
                lingua1.setSelection(lingua2.getSelectedItemPosition());
                lingua2.setSelection(pos);
            }
        });

        lingua1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Toast.makeText(MainActivity.this, item.toString(),
                            Toast.LENGTH_SHORT).show();
                }

                tvLingua1.setText(lingua1.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });




    }
}