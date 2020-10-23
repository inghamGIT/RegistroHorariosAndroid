package com.example.apphorarios.Controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PagerController extends FragmentPagerAdapter {

    int numTabs;
    ArrayList<JSONObject> weekDataArray;
    String fecha, horaEntrada, horaSalida, descripcion;

    public PagerController(@NonNull FragmentManager fm, int behavior, ArrayList<JSONObject> weekDataArray) {
        super(fm, behavior);
        this.numTabs = behavior;
        this.weekDataArray = weekDataArray;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                try {
                    fecha = weekDataArray.get(0).getString("fecha");
                    horaEntrada = weekDataArray.get(0).getString("horaEntrada");
                    horaSalida = weekDataArray.get(0).getString("horaSalida");
                    descripcion = weekDataArray.get(0).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            case 1:
                try {
                    fecha = weekDataArray.get(1).getString("fecha");
                    horaEntrada = weekDataArray.get(1).getString("horaEntrada");
                    horaSalida = weekDataArray.get(1).getString("horaSalida");
                    descripcion = weekDataArray.get(1).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            case 2:
                try {
                    fecha = weekDataArray.get(2).getString("fecha");
                    horaEntrada = weekDataArray.get(2).getString("horaEntrada");
                    horaSalida = weekDataArray.get(2).getString("horaSalida");
                    descripcion = weekDataArray.get(2).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            case 3:
                try {
                    fecha = weekDataArray.get(3).getString("fecha");
                    horaEntrada = weekDataArray.get(3).getString("horaEntrada");
                    horaSalida = weekDataArray.get(3).getString("horaSalida");
                    descripcion = weekDataArray.get(3).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            case 4:
                try {
                    fecha = weekDataArray.get(4).getString("fecha");
                    horaEntrada = weekDataArray.get(4).getString("horaEntrada");
                    horaSalida = weekDataArray.get(4).getString("horaSalida");
                    descripcion = weekDataArray.get(4).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            case 5:
                try {
                    fecha = weekDataArray.get(5).getString("fecha");
                    horaEntrada = weekDataArray.get(5).getString("horaEntrada");
                    horaSalida = weekDataArray.get(5).getString("horaSalida");
                    descripcion = weekDataArray.get(5).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            case 6:
                try {
                    fecha = weekDataArray.get(6).getString("fecha");
                    horaEntrada = weekDataArray.get(6).getString("horaEntrada");
                    horaSalida = weekDataArray.get(6).getString("horaSalida");
                    descripcion = weekDataArray.get(6).getString("descripcion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new TabDia(fecha, horaEntrada, horaSalida, descripcion);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
