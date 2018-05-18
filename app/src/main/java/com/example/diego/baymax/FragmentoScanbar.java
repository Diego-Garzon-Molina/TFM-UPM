package com.example.diego.baymax;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Diego on 21/02/2018.
 */

public class FragmentoScanbar extends Fragment implements ScanResultReceiver {
    public String codeContent,codeFormat;
    public Button bScan;
    public Fragment thisFragment;
    public FragmentoScanbar() {
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_scanbar, container, false);
        bScan = view.findViewById(R.id.bScan);
        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(thisFragment);
                // use forSupportFragment or forFragment method to use fragments instead of activity
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt(getString(R.string.scan));
                integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.thisFragment = this;

    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
       // ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();
       // Toast.makeText(this.getActivity(),"activity Result",Toast.LENGTH_LONG).show();
        if (scanningResult != null) {
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();
            scanResultData(codeFormat,codeContent);
           // parentActivity.scanResultData(codeFormat,codeContent);

        }else{
            String noResultErrorMsg = "No se ha escaneado correctamente, pruebe de nuevo";
            scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }

    @Override
    public void scanResultData(String codeFormat, String codeContent) {
        String codNacional = codeContent.substring(codeContent.length()-7,codeContent.length()-1);

        Toast toast = Toast.makeText(getActivity(),"Code Format" + codeFormat +"\nCodeContent" + codNacional, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(getActivity(),noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}
