package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

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
                login(correo.getText().toString(), pass.getText().toString());
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
        JSONObject body = new JSONObject();

        try {
            body.put("correo", correo);
            body.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "https://apipostahuaral.azurewebsites.net/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String token = object.getString("Token");
                            Object Usuario = object.getJSONObject("Usuario");
                            
                            if (token != null ) {
                                Intent newWindow = new Intent(getApplicationContext(), MenuActivity.class);
                                newWindow.putExtra("Token", token);
                                newWindow.putExtra("Correo", correo);
                                startActivity(newWindow);
                            } else {
                                Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

}