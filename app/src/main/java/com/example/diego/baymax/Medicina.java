package com.example.diego.baymax;

/**
 * Created by Diego on 30/03/2018.
 */

public class Medicina {
    private int id;
    private String nombreMedicina;
    private String principioActivo;

    public Medicina (int id, String nombreMedicina, String principioActivo){
        this.id = id;
        this.nombreMedicina = nombreMedicina;
        this.principioActivo = principioActivo;
    }
    public Medicina (String nombreMedicina, String principioActivo){
        this.nombreMedicina = nombreMedicina;
        this.principioActivo = principioActivo;
    }
    public int getId(){return this.id;}
    public String getNombreMedicina(){return this.nombreMedicina;}
    public String getPrincipioActivo(){return this.principioActivo;}
}
