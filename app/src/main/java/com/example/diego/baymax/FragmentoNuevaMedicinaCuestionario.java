package com.example.diego.baymax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Diego on 21/02/2018.
 */

public class FragmentoNuevaMedicinaCuestionario extends Fragment implements TimePickerDialog.OnTimeSetListener {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private FragmentTransaction FT;
    private MiBaseDatos MDB;
    private FragmentoListaMedicinas fragmentoListaMedicinas;
    private Button bHora, bCancelar, bGuardar;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cbTodos;
    private View vista;
    public ListView listView;
    private ArrayList<String> lAlarmas;
    private MiAdaptador2 adaptadorAlarmas;

    public FragmentoNuevaMedicinaCuestionario() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nueva_medicina_cuestionario, container, false);

        this.vista = view;
        listView = vista.findViewById(R.id.lista2);
        MDB = new MiBaseDatos(getActivity().getApplicationContext());
        lAlarmas = new ArrayList<>();
        lAlarmas.add("");
        lAlarmas.remove(0);
        adaptadorAlarmas = new MiAdaptador2(getContext(), lAlarmas);
        listView.setAdapter(adaptadorAlarmas);
        cb1 = vista.findViewById(R.id.checkBox1);
        cb2 = vista.findViewById(R.id.checkBox2);
        cb3 = vista.findViewById(R.id.checkBox3);
        cb4 = vista.findViewById(R.id.checkBox4);
        cb5 = vista.findViewById(R.id.checkBox5);
        cb6 = vista.findViewById(R.id.checkBox6);
        cb7 = vista.findViewById(R.id.checkBox7);
        cbTodos = vista.findViewById(R.id.checkBox8);


        cbTodos.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (cbTodos.isChecked()) {
                    cb1.setChecked(true);
                    cb2.setChecked(true);
                    cb3.setChecked(true);
                    cb4.setChecked(true);
                    cb5.setChecked(true);
                    cb6.setChecked(true);
                    cb7.setChecked(true);
                } else {
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                    cb6.setChecked(false);
                    cb7.setChecked(false);
                }
            }
        });

        bHora = view.findViewById(R.id.hora);
        bHora.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                TimePickerDialog selector = new TimePickerDialog(getContext(), FragmentoNuevaMedicinaCuestionario.this,
                        Calendar.HOUR_OF_DAY, 0, DateFormat.is24HourFormat(getActivity()));
                selector.show();
            }
        });
        bCancelar = view.findViewById(R.id.bCancelar);
        bCancelar.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                fragmentoListaMedicinas = new FragmentoListaMedicinas();
                FT = getActivity().getSupportFragmentManager().beginTransaction();
                FT.replace(R.id.view_fragments, fragmentoListaMedicinas);
                FT.commit();
            }
        });
        bGuardar = view.findViewById(R.id.bGuardar);
        bGuardar.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                int id = MDB.insertarMedicina("medicina insertada", "pa insertado");
                JSONObject jAlarmas = new JSONObject();
                JSONObject jCodigos = new JSONObject();
                boolean[] diasChecked = new boolean[7];
                boolean diaSeleccionado = false;

                int diasSeleccionados = 0;
                if (lAlarmas.isEmpty()) {
                    Toast.makeText(getContext(), "Debe seleccionar al menos una hora", Toast.LENGTH_SHORT).show();
                    return;
                }
                diasChecked[0] = cb1.isChecked();
                diasChecked[1] = cb2.isChecked();
                diasChecked[2] = cb3.isChecked();
                diasChecked[3] = cb4.isChecked();
                diasChecked[4] = cb5.isChecked();
                diasChecked[5] = cb6.isChecked();
                diasChecked[6] = cb7.isChecked();
                for (Boolean dias : diasChecked) {
                    if (dias) {
                        diasSeleccionados++;
                        diaSeleccionado = true;
                    }
                    try {
                        jAlarmas.accumulate("dias", dias);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (!diaSeleccionado) {
                    Toast.makeText(getContext(), "Debe seleccionar al menos un dia", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (String object : lAlarmas) {
                    try {
                        jAlarmas.accumulate("alarma", object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                MDB.insertarAlarmas(id, jAlarmas.toString());
                MDB.insertarCodigos(id, jCodigos.toString());
                declararAlarmas(diasChecked, diasSeleccionados);

                fragmentoListaMedicinas = new FragmentoListaMedicinas();
                FT = getActivity().getSupportFragmentManager().beginTransaction();
                FT.replace(R.id.view_fragments, fragmentoListaMedicinas);
                FT.commit();
            }
        });
        return view;
    }

    public void declararAlarmas(boolean[] diasChecked, int diasSeleccionados) {
        Calendar horaActual = Calendar.getInstance();
        int horasAux[] = new int[lAlarmas.size()];
        int minutesAux[] = new int[lAlarmas.size()];
        for (int i = 0; i < lAlarmas.size(); i++) {
            if (lAlarmas.get(i) == null) break;
            String horaAlarma = lAlarmas.get(i);
            horasAux[i] = (Integer.parseInt(horaAlarma.substring(0, horaAlarma.indexOf(":"))));
            minutesAux[i] = (Integer.parseInt(horaAlarma.substring(horaAlarma.indexOf(":") + 1)));
        }
        Context context = getContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar arrayCalendar[] = new Calendar[diasSeleccionados * lAlarmas.size()];
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction("nueva notificacion");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        int contadorAlarmas = 0;
        for (int j = 0; j < lAlarmas.size(); j++) {
            for (int i = 0; i < diasChecked.length; i++) {
                if (!diasChecked[i]) continue;
                arrayCalendar[contadorAlarmas] = Calendar.getInstance();
                arrayCalendar[contadorAlarmas].set(
                        horaActual.get(Calendar.YEAR),
                        horaActual.get(Calendar.MONTH),
                        (horaActual.get(Calendar.DAY_OF_MONTH) - horaActual.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 1 + i),
                        horasAux[j], minutesAux[j]);
                 Toast.makeText(context, "alarmas declaradas: " + arrayCalendar[contadorAlarmas].getTime().toString(), Toast.LENGTH_SHORT).show();
                if (arrayCalendar[contadorAlarmas].compareTo(Calendar.getInstance()) > 0) {
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, arrayCalendar[contadorAlarmas].getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, pendingIntent);
                } else {
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, arrayCalendar[contadorAlarmas].getTimeInMillis() + 1000 * 60 * 60 * 24 * 7, 1000 * 60 * 60 * 24 * 7, pendingIntent);
                }
                contadorAlarmas++;
            }
      }

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        int hora = hourOfDay;
        int minuto = minute;
        boolean repeticion = false;

        TextView textView = (TextView) vista.findViewById(R.id.HorasInsertadasTexto);
        String newText = (String) textView.getText();
        //LinearLayout layout = (LinearLayout) findViewById(R.id.HorasInsertadas);
        // TextView textView = new TextView(calendario.this);

        if (minute > 10) minuto = 20;
        if (minute > 30) minuto = 40;
        if (minute >= 50) {
            hora += 1;
            minuto = 0;
        }
        if (hora == 24) hora = 0;
        if (minuto < 10) {
            minuto = 0;
            newText += hora + ":0" + minuto;
        } else {
            newText += hora + ":" + minuto;
        }
        for (String object : lAlarmas) {
            if (object.contains(newText)) {
                repeticion = true;
                break;
            }
        }
        if (!repeticion) {
            lAlarmas.add(newText);
            adaptadorAlarmas.notifyDataSetChanged();
        }

    }
}
