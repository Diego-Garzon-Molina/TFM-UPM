package com.example.diego.baymax;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Diego on 21/02/2018.
 */

public class FragmentoListaMedicinas extends Fragment {
    private FragmentTransaction FT;
    private FragmentoNuevaMedicinaCuestionario fNMCuestionario;
    private MiBaseDatos MDB;
    private int[] ident;
    private int iDAct = 0;
    public MiAdaptador adaptador;
    public ListView listView;
    public Button bNuevaMedicina;
    public String codeContent,codeFormat;
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
        ident = MDB.recuperaIds();
        listView = view.findViewById(R.id.lista);
        rellenaLista();
        bNuevaMedicina = view.findViewById(R.id.bNuevaMedicina);
        bNuevaMedicina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  IntentIntegrator integrator = IntentIntegrator.forSupportFragment(FragmentoListaMedicinas.this);
                // use forSupportFragment or forFragment method to use fragments instead of activity
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt(getActivity().getString(R.string.scan));
                integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();*/

              //Esto debe ir una vez leido la medicina EN EL ON RESULT ACTIVITY
                fNMCuestionario = new FragmentoNuevaMedicinaCuestionario();
                FT = getActivity().getSupportFragmentManager().beginTransaction();
                FT.replace(R.id.view_fragments, fNMCuestionario,"fNMCuestionario");
                getActivity().getSupportFragmentManager().executePendingTransactions();

                FT.addToBackStack(null);
                FT.commit();
            }
        });
        return view;
    }

    public void rellenaLista() {
        int numFilas;
        //Comprobamos el numero de filas almacenadas en la BBDD
        numFilas = MDB.numerodeFilas();
        if (numFilas > 0) {
            //rellenamos las ids de la lista en nuestro array identificador
            ident = MDB.recuperaIds();
            //creamos y rellenamos el adaptador de la lista
            adaptador = new MiAdaptador(getContext(), MDB.recuperarMedicinaArrray(""));
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();
        Toast.makeText(this.getActivity(),"activity Result",Toast.LENGTH_SHORT).show();
        if (scanningResult != null) {
            //we have a result
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();
            // send received data
            parentActivity.scanResultData(codeFormat,codeContent);

        }else{
            // send exception
            //  parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }


}

