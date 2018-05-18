package com.example.diego.baymax;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Diego on 21/02/2018.
 */

public class FragmentoListaMedicinas extends Fragment implements ScanResultReceiver {
    private FragmentTransaction FT;
    private FragmentoNuevaMedicinaCuestionario fNMCuestionario;
    private MiBaseDatos MDB;
    private int[] ident;
    private int iDAct = 0;
    public MiAdaptador adaptador;
    public ListView listView;
    public Button bNuevaMedicina;
    public String codeContent, codeFormat;

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
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(FragmentoListaMedicinas.this);
                // use forSupportFragment or forFragment method to use fragments instead of activity
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt(getActivity().getString(R.string.scan));
                integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();

                //Esto debe ir una vez leido la medicina EN EL ON RESULT ACTIVITY

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
        // ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();
        // Toast.makeText(this.getActivity(),"activity Result",Toast.LENGTH_LONG).show();
        if (scanningResult != null) {
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();
            scanResultData(codeFormat, codeContent);
            // parentActivity.scanResultData(codeFormat,codeContent);

        } else {
            String noResultErrorMsg = "No se ha escaneado correctamente, pruebe de nuevo";
            scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }

    @Override
    public void scanResultData(String codeFormat, String codeContent) {
        String codNacional = codeContent.substring(codeContent.length() - 7, codeContent.length() - 1);
        Toast toast = Toast.makeText(getActivity(), "Code Format" + codeFormat + "\nCode Content" + codNacional, Toast.LENGTH_SHORT);
        toast.show();
        fNMCuestionario = new FragmentoNuevaMedicinaCuestionario();
        Resources res = getResources();
        XmlResourceParser xpp = res.getXml(R.xml.prueba2);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parseamos el documento obteniendolo de los recursos y lo almacenamos
            // en un objeto Document
            Document doc = builder.parse(getResources()
                    .openRawResource(R.raw.prueba2));

            // Obtenemos el elemento raiz del documento, libros
            Element raiz = doc.getDocumentElement();

            // Obtenemos todos los elementos llamados libro, que cuelgan
            // de la raiz

            NodeList items = raiz.getElementsByTagName("prescription");
            Log.d("CodNacional", codNacional);

            consultaListaNodos(items, codNacional, false);


        } catch (Exception e) {

        }

    }

    public void consultaListaNodos(NodeList list, String codNacional, Boolean nodoEncontrado) {
        String cod_nacion = "";
        String nombre = "";
        String contenido = "";
        String[] principioActivo = new String[100];
        if (nodoEncontrado) {
            for (int x = 0; x < list.getLength(); x++) {
                Node nodoActual = list.item(x);
                if (nodoActual.getNodeName().equalsIgnoreCase("des_nomco")) {
                    nombre = nodoActual.getChildNodes().item(0)
                            .getNodeValue();
                }
                if (nodoActual.getNodeName().equalsIgnoreCase("contenido")) {
                    contenido = nodoActual.getChildNodes().item(0)
                            .getNodeValue();
                }
                if (nodoActual.getNodeName().equalsIgnoreCase("formasfarmaceuticas")) {
                    for (int z = 0; z < nodoActual.getChildNodes().getLength(); z++) {
                        principioActivo[z] = nodoActual.getChildNodes().item(0).getNodeValue();
                    }
                }
                Bundle args = new Bundle();
                args.putString("NombreMedicina", nombre);
                args.putString("cod_nacion", cod_nacion);
                args.putString("contenido", contenido);
                args.putString("principio_activo", principioActivo[0]);
                fNMCuestionario.setArguments(args);
                FT = getActivity().getSupportFragmentManager().beginTransaction();
                FT.replace(R.id.view_fragments, fNMCuestionario, "fNMCuestionario");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                FT.addToBackStack(null);
                FT.commit();
            }
        } else {
            for (int i = 0; i < list.getLength(); i++) {
                Node nodoActual = list.item(i);

                if(nodoActual.getNodeName().equals("cod_nacion")) {
                    cod_nacion = nodoActual.getFirstChild().getNodeValue();
                    Log.d("Valor Nodo", "" + cod_nacion);
                }

                if (cod_nacion.equals(codNacional)) {
                    Log.d("Nodo Actual", "" + nodoActual.getNodeName().toString());
                    Log.d("Valor Nodo", "" + cod_nacion);
                    consultaListaNodos(list, codNacional, true);

                    break;
                }else
                    consultaListaNodos(nodoActual.getChildNodes(), codNacional, false);


            }
        }
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(getActivity(), noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}



