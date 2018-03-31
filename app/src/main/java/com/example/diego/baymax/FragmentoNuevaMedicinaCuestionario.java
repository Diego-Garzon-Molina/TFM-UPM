package com.example.diego.baymax;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Diego on 21/02/2018.
 */

public class FragmentoNuevaMedicinaCuestionario extends Fragment{
    private FragmentTransaction FT;
    private FragmentoListaMedicinas fragmentoListaMedicinas;
    private Button bHora,bCancelar;
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
        bHora = view.findViewById(R.id.hora);
        bHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeDialog selector = new TimeDialog();
                selector.show(getActivity().getSupportFragmentManager(), "Hora de medicación");
            }
        });
        bCancelar = view.findViewById(R.id.bCancelar);
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentoListaMedicinas = new FragmentoListaMedicinas();
                FT = getActivity().getSupportFragmentManager().beginTransaction();
                FT.replace(R.id.view_fragments, fragmentoListaMedicinas);
                FT.commit();
            }
        });
        return view;
    }
}
