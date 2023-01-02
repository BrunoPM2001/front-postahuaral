package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /*
    String [] items = {"Holi", "gaa"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterString;
*/
    EditText correo;
    EditText pass;
    Button iniciar_sesion;
    Button registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = findViewById(R.id.correoLogin);
        pass = findViewById(R.id.passLogin);
        iniciar_sesion = findViewById(R.id.iniciarsesion);
        registrarse = findViewById(R.id.registrarse);

        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        /*
        autoCompleteTextView = findViewById(R.id.auto_complete_esp);
        adapterString = new ArrayAdapter<String>(this, R.layout.list_items, items);

        autoCompleteTextView.setAdapter(adapterString);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    */
    }
}