package com.example.myapplication.models;

public class Cita {
    private String fecha;
    private String especialidad;
    private String medico;

    public Cita (String fecha, String especialidad, String medico) {
        this.medico = medico;
        this.especialidad = especialidad;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }
}
