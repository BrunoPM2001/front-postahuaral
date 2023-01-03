package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Cita;

import java.util.ArrayList;

public class ListaCitasAdapter extends RecyclerView.Adapter<ListaCitasAdapter.CitaViewHolder> {

    ArrayList<Cita> listCitas;

    public ListaCitasAdapter(ArrayList<Cita> listCitas) {
        this.listCitas = listCitas;
    }


    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_cita, null, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        holder.viewFecha.setText(listCitas.get(position).getFecha());
        holder.viewEsp.setText(listCitas.get(position).getEspecialidad());
        holder.viewMedico.setText(listCitas.get(position).getMedico());
    }

    @Override
    public int getItemCount() {
        return listCitas.size();
    }

    public class CitaViewHolder extends RecyclerView.ViewHolder {

        TextView viewFecha, viewEsp, viewMedico;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);

            viewFecha = itemView.findViewById(R.id.citaHora);
            viewEsp = itemView.findViewById(R.id.citaEsp);
            viewMedico = itemView.findViewById(R.id.citaMed);

        }
    }
}
