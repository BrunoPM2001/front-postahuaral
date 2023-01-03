package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    Button programarCita;
    Button visualizarCita;
    String token;
    String correo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //  Recuperando token del login
        token = getIntent().getExtras().getString("Token");
        correo1 = getIntent().getExtras().getString("Correo");

        //  Recuperando botones
        programarCita = findViewById(R.id.programarcita);
        visualizarCita = findViewById(R.id.visualizarcita);

        //  Eventos de click
        programarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                programarCita();
            }
        });

        visualizarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualizarCitas();
            }
        });

    }

    public void visualizarCitas() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://apipostahuaral.azurewebsites.net/getCitasFromUsuario",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            if (token != null ) {
                                Intent newWindow = new Intent(getApplicationContext(), CitasActivity.class);
                                newWindow.putExtra("Citas", object.getJSONArray("Citas").toString());
                                startActivity(newWindow);
                            } else {
                                Toast.makeText(MenuActivity.this, "Credenciales incorectas", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MenuActivity.this, "Credenciales incorectas", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth", token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MenuActivity.this);
        requestQueue.add(stringRequest);
    }

    public void programarCita() {
        Intent newWindow = new Intent(getApplicationContext(), ProgramarCitaActivity.class);
        newWindow.putExtra("Token", token);
        startActivity(newWindow);
    }
}