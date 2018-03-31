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
 * Created by Diego on 9/11/16.
 */
public class MiAdaptador extends ArrayAdapter<Medicina> {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre, pActivo;
    private MiBaseDatos MDB;
    private Button bBorrarMedicina;
    private  ArrayList arrayList;
    public MiAdaptador(Context contexto, ArrayList<Medicina> c) {
        super(contexto, 0, c);
        MDB = new MiBaseDatos(contexto);
        this.arrayList = c;
    }
    public View getView(final int position, View vista, ViewGroup parent) {
        if (vista == null) {
            vista = LayoutInflater.from(getContext()).inflate(R.layout.elementolista, parent, false);
        }
        Medicina c = getItem(position);
        final int id = c.getId();
        nombre = (TextView) vista.findViewById(R.id.nombreMedicina);
        pActivo = (TextView) vista.findViewById(R.id.principioActivo);
        String nombreT = c.getNombreMedicina();
        nombre.setText(nombreT);
        pActivo.setText( c.getPrincipioActivo());
        bBorrarMedicina = vista.findViewById(R.id.bBorrarMedicina);
        bBorrarMedicina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MDB.borrarMedicina(id);
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
        return vista;
    }
}


