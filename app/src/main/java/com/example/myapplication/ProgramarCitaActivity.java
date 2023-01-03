package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProgramarCitaActivity extends AppCompatActivity {


    String [] items = {"Seleccione algo"};

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterString;

    AutoCompleteTextView autoCompleteTextView2;

    AutoCompleteTextView autoCompleteTextView3;

    String idEspecialidades [];
    String especialidades [];
    String idMedicos [];
    String medicos [];
    String idHorarios [];
    String horarios [];

    //  Datos a enviar a la siguiente interfaz
    String idMedico;
    String token;
    String espF;
    String medF;
    String horF;
    String costoT;

    String correo;

    Button back;
    Button reservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programar_cita);

        token = getIntent().getExtras().getString("Token");
        correo = getIntent().getExtras().getString("correo");

        autoCompleteTextView = findViewById(R.id.auto_complete_esp);
        autoCompleteTextView2 = findViewById(R.id.auto_complete_med);
        autoCompleteTextView3 = findViewById(R.id.auto_complete_hor);
        reservar = findViewById(R.id.reservar_btn);
        back = findViewById(R.id.programarcita_back);

        adapterString = new ArrayAdapter<String>(this, R.layout.list_items, items);

        autoCompleteTextView.setAdapter(adapterString);
        autoCompleteTextView2.setAdapter(adapterString);
        autoCompleteTextView3.setAdapter(adapterString);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getMedicos(i);
                espF = adapterView.getItemAtPosition(i).toString();
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getHorarios(i);
                idMedico = idMedicos[i];
                medF = adapterView.getItemAtPosition(i).toString();
            }
        });

        autoCompleteTextView3.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getHorarios(i);
                horF = adapterView.getItemAtPosition(i).toString();
            }
        });


        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWindow = new Intent(getApplicationContext(), PagoHistorial.class);
                newWindow.putExtra("Token", token);
                newWindow.putExtra("idMedico", idMedico);
                newWindow.putExtra("espF", espF);
                newWindow.putExtra("medF", medF);
                newWindow.putExtra("horF", horF);
                newWindow.putExtra("costo", costoT);
                newWindow.putExtra("correo", correo);
                startActivity(newWindow);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWindow = new Intent(getApplicationContext(), MenuActivity.class);
                newWindow.putExtra("Token", token);
                newWindow.putExtra("correo", correo);
                startActivity(newWindow);
            }
        });


        getEspecialidades();

    }

    public void getEspecialidades() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://apipostahuaral.azurewebsites.net/getEspecialidades",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray object = new JSONArray(response);
                            idEspecialidades = new String[object.length()];
                            especialidades = new String[object.length()];
                            for (int i = 0; i < object.length(); i++) {
                                especialidades[i] = object.getJSONObject(i).get("nombre").toString();
                                idEspecialidades[i] = object.getJSONObject(i).get("idespecialidad").toString();
                            }
                            ArrayAdapter<String> adapterString1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_items, especialidades);
                            autoCompleteTextView.setAdapter(adapterString1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProgramarCitaActivity.this, "Credenciales incorectas", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ProgramarCitaActivity.this);
        requestQueue.add(stringRequest);
    }

    public void getMedicos(int idEsp) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://apipostahuaral.azurewebsites.net/getMedicosByEspecialidad?idEspecialidad=" + idEspecialidades[idEsp],
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray object = new JSONArray(response);
                            idMedicos = new String[object.length()];
                            medicos = new String[object.length()];
                            for (int i = 0; i < object.length(); i++) {
                                idMedicos[i] = object.getJSONObject(i).get("idmedico").toString();
                                medicos[i] = object.getJSONObject(i).get("nombre").toString() + " " + object.getJSONObject(i).get("apellido").toString();
                            }
                            ArrayAdapter<String> adapterString1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_items, medicos);
                            autoCompleteTextView2.setAdapter(adapterString1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProgramarCitaActivity.this, "Credenciales incorectas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ProgramarCitaActivity.this);
        requestQueue.add(stringRequest);
    }

    public void getHorarios(int idMed) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://apipostahuaral.azurewebsites.net/getMedico?id=" + idMedicos[idMed],
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject esp = new JSONObject(obj.getJSONObject("especialidad").toString());
                            JSONArray object = new JSONArray(obj.getJSONArray("horarios").toString());
                            costoT = esp.getString("costo").toString();
                            idHorarios = new String[object.length()];
                            horarios = new String[object.length()];
                            for (int i = 0; i < object.length(); i++) {
                                idHorarios[i] = object.getJSONObject(i).get("idhorario").toString();
                                horarios[i] = object.getJSONObject(i).get("fechaatencion").toString() + " a las " + object.getJSONObject(i).get("horainicio").toString();
                            }
                            ArrayAdapter<String> adapterString1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_items, horarios);
                            autoCompleteTextView3.setAdapter(adapterString1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProgramarCitaActivity.this, "Credenciales incorectas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ProgramarCitaActivity.this);
        requestQueue.add(stringRequest);
    }
}