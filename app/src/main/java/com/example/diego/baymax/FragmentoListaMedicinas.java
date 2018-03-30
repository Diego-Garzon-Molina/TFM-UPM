package com.example.diego.baymax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Diego on 21/02/2018.
 */

public class FragmentoListaMedicinas extends Fragment {

    private MiBaseDatos MDB;
    private int[] ident;
    private int iDAct = 0;
    public MiAdaptador adaptador;
    public ListView listView;

    //  private MiAdaptadorBusqueda adaptadorBusqueda;
    public FragmentoListaMedicinas() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_medicinas, container, false);
        MDB = new MiBaseDatos(getActivity().getApplicationContext());
       // MDB.insertarMedicina("Medicina1","PrincipioActivo1");
        ident = MDB.recuperaIds();
        listView = view.findViewById(R.id.lista);
        rellenaLista();
        return view;
    }

    public void rellenaLista() {

        int numFilas;

        //Comprobamos el numero de filas almacenadas en la BBDD
        numFilas = MDB.numerodeFilas();
        Toast.makeText(getActivity().getApplicationContext(),""+numFilas,Toast.LENGTH_LONG).show();
        if (numFilas > 0) {
            //rellenamos las ids de la lista en nuestro array identificador
            ident = MDB.recuperaIds();
            //creamos y rellenamos el adaotador de la lista
            adaptador = new MiAdaptador(getContext(), MDB.recuperarMedicinaCursor());

            listView.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    iDAct = ident[position];
                }
            });

        }

    }


}

