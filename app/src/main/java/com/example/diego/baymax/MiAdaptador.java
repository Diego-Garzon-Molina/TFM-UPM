package com.example.diego.baymax;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Diego on 9/11/16.
 */
public class MiAdaptador extends CursorAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre, pActivo;

    public MiAdaptador(Context contexto, Cursor c) {
        super(contexto, c, false);
    }

    /* bindView() es el encargado de poblar la lista con los datos del cursor y
       newView() es quien infla cada view de la lista. Al implementar ambos
       métodos no debemos preocuparnos por iterar el curso, esto es manejado internamente.
      */
    @Override
    /* en newView() accedemos a la instancia del LayoutInflater a través del Context
      y luego invocamos inflate() para inflar nuestra fila.*/
    public View newView(Context contexto, Cursor c, ViewGroup padre) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.elementolista, padre, false);
        return vista;
    }

    @Override
    /* En bindView() simplemente debemos obtener los valores de las columnas
     y emparejarlos en los TextViews del layout.*/
    public void bindView(View vista, Context contexto, Cursor c) {
        nombre = (TextView) vista.findViewById(R.id.nombreMedicina);
        pActivo = (TextView) vista.findViewById(R.id.principioActivo);
        String nombreT = c.getString(c.getColumnIndex("nombreMedicina"));
        nombre.setText(nombreT);
        pActivo.setText(c.getString(c.getColumnIndex("principioActivo")));
    }
}

