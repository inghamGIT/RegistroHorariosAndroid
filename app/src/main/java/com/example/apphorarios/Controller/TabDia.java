package com.example.apphorarios.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.apphorarios.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabDia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabDia extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView txt_fecha, txt_horaEntrada, txt_horaSalida, txt_descripcion;
    String fecha, horaEntrada, horaSalida, descripcion;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabDia() {
        // Required empty public constructor
    }

    public TabDia(String fecha, String horaEntrada, String horaSalida, String descripcion) {
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.descripcion = descripcion;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Lunes.
     */
    // TODO: Rename and change types and number of parameters
    public static TabDia newInstance(String param1, String param2) {
        TabDia fragment = new TabDia();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_dia, container, false);

        txt_fecha = view.findViewById(R.id.txt_dia_fecha);
        txt_horaEntrada = view.findViewById(R.id.txt_dia_horaEntrada);
        txt_horaSalida = view.findViewById(R.id.txt_dia_horaSalida);
        txt_descripcion = view.findViewById(R.id.txt_dia_descripcion);

        txt_fecha.setText(fecha);
        txt_horaEntrada.setText(horaEntrada);
        txt_horaSalida.setText(horaSalida);
        txt_descripcion.setText(descripcion);

        return view;
    }
}