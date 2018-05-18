package com.example.diego.baymax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Diego on 9/11/16.
 */
public class MiAdaptador extends ArrayAdapter<Medicina> {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre, pActivo;
    private MiBaseDatos MDB;
    private Button bBorrarMedicina;
    private  ArrayList arrayList;
    private ArrayList<String> lAlarmas;
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
                cancelarAlarmas(id);
                MDB.borrarAlarmas(id);
                MDB.borrarCodigos(id);
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
        return vista;
    }
    public void cancelarAlarmas(int id) {
        Context context = getContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction("nueva notificacion");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);
        manager.cancel(pendingIntent);

    }

}


