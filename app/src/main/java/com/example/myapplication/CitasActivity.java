package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.adapters.ListaCitasAdapter;
import com.example.myapplication.models.Cita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CitasActivity extends AppCompatActivity {

    JSONArray citas;
    RecyclerView listaCitas;
    ArrayList<Cita> listadeCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        listaCitas = findViewById(R.id.listaCitas);
        listaCitas.setLayoutManager(new LinearLayoutManager(this));

        try {
            citas = new JSONArray(getIntent().getExtras().getString("Citas"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener citas", Toast.LENGTH_SHORT).show();
        }
        //  Para la lista de citas
        String fec;
        String esp;
        String med;

        listadeCitas = new ArrayList<>();

        for (int i = 0; i < citas.length(); i++) {
            try {
                JSONObject cita = new JSONObject(citas.get(i).toString());
                JSONObject medico = new JSONObject(cita.getJSONObject("medico").toString());
                JSONArray horarios = new JSONArray(medico.getJSONArray("horarios").toString());
                JSONObject especialidad = new JSONObject(medico.getJSONObject("especialidad").toString());
                JSONObject horario = new JSONObject(horarios.get(0).toString());

                fec = "Día: " + horario.get("fechaatencion").toString() + ", a las " + horario.get("horainicio").toString();
                esp = especialidad.get("nombre").toString();
                med = medico.get("nombre").toString() + " " + medico.get("apellido").toString();

                Cita nuevaCita = new Cita(fec,esp,med);
                System.out.println(nuevaCita);
                listadeCitas.add(nuevaCita);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ListaCitasAdapter adapter = new ListaCitasAdapter(listadeCitas);
        listaCitas.setAdapter(adapter);

    }
}