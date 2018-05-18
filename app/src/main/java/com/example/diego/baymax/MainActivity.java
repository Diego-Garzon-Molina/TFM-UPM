package com.example.diego.baymax;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage, scan_format, scan_content;
    // private Button scanner;
    private FragmentTransaction FT;
    private FragmentoScanbar scannerFragment;
    private FragmentoListaMedicinas listaFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_scanbar:

                    if (permisoCamara()) {
                        //   int i = xpp.getAttributeIntValue("cod_nacion","",1);
                        //   Toast.makeText(MainActivity.this,""+ xpp.getAttributeName(2),Toast.LENGTH_LONG).show();
                        scannerFragment = new FragmentoScanbar();
                        FT = getSupportFragmentManager().beginTransaction();
                        FT.replace(R.id.view_fragments, scannerFragment);
                        FT.addToBackStack(null);
                        FT.commit();
                    }
                    return true;
                case R.id.navigation_medicinas:
                    listaFragment = new FragmentoListaMedicinas();
                    FT = getSupportFragmentManager().beginTransaction();
                    FT.replace(R.id.view_fragments, listaFragment);
                    FT.addToBackStack(null);
                    FT.commit();
                    return true;
                case R.id.navigation_avisos_entrantes:
                  /*  scannerFragment = new FragmentoScanbar();
                    FT = getSupportFragmentManager().beginTransaction();
                    FT.replace(R.id.view_fragments, scannerFragment);
                    FT.addToBackStack(null);
                    FT.commit();*/
                    return true;
                case R.id.navigation_avisos_salientes:
                  /* scannerFragment = new FragmentoScanbar();
                    FT = getSupportFragmentManager().beginTransaction();
                    FT.replace(R.id.view_fragments, scannerFragment);
                    FT.addToBackStack(null);
                    FT.commit();*/
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan_content = (TextView) findViewById(R.id.scan_content);
        scan_format = (TextView) findViewById(R.id.scan_format);

        permisoCamara();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId( R.id.navigation_medicinas);

    }


    public boolean permisoCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "La camara necesita permiso", Toast.LENGTH_SHORT).show();
                    return false;
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;

        }
    }


}
