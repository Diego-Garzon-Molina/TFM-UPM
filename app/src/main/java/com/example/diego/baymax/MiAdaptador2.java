package com.example.diego.baymax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Diego on 10/04/18.
 */
public class MiAdaptador2 extends ArrayAdapter<String> {
    TextView nombre, pActivo;
    private Button bBorrarHora;
    private  ArrayList arrayList;
    public MiAdaptador2(Context contexto, ArrayList<String> c) {
        super(contexto, 0, c);
        this.arrayList = c;
    }
    public View getView(final int position, View vista, ViewGroup parent) {
        if (vista == null) {
            vista = LayoutInflater.from(getContext()).inflate(R.layout.elementolista2, parent, false);
        }
        String alarma = getItem(position);
        nombre = (TextView) vista.findViewById(R.id.alarma);
        nombre.setText("Hora seleccionada: " + alarma);
        bBorrarHora = vista.findViewById(R.id.bBorrarAlarma);
        bBorrarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.remove(position);

                notifyDataSetChanged();
            }
        });
        return vista;
    }
}


