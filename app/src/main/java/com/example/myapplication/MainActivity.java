package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
                System.out.println("Correo: " + correo.getText().toString());
                System.out.println("Pass: " + pass.getText().toString());

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

    public void login(String correo, String pass) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://192.168.1.6/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String token = object.getString("client_secret");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

}