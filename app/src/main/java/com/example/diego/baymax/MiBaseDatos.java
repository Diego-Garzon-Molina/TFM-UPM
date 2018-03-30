package com.example.diego.baymax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Diego on 09/11/2017.
 */

public class MiBaseDatos extends SQLiteOpenHelper {
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "MiBDdeContactos2017.db";
    private static final String TABLA_CONTACTOS =
            "CREATE TABLE medicinas " +
                    "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " nombreMedicina VARCHAR(100) , principioActivo VARCHAR(50) )";


    public MiBaseDatos(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         /* El método onCreate() será ejecutado automáticamente por nuestra clase
    MiBaseDatos cuando sea necesaria la creación de la base de datos, es decir, cuando aún no exista
    Las tareas típicas que deben hacerse en este método serán la creación de todas las tablas necesarias
    y la inserción de los datos iniciales si son necesarios,Para la creación de la tabla utilizaremos
    la sentencia SQL ya definida y la ejecutaremos contra la base de datos utilizando el método más
    sencillo de los disponibles en la API de SQLite proporcionada por Android, llamado execSQL().
    Este método se limita a ejecutar directamente el código SQL que le pasemos como parámetro
    */
        db.execSQL(TABLA_CONTACTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*se lanzará automáticamente cuando sea necesaria una actualización de la
      estructura de la base de datos o una conversión de los datos cuando se produzca
      un cambio de versión en nuestra BBDD optamos por la opción más sencilla:
       borrar la tabla actual y volver a crearla con la nueva estructura, */

        db.execSQL("DROP TABLE IF EXISTS" + TABLA_CONTACTOS);
        onCreate(db);
    }

    public void insertarMedicina(String nombreMedicina, String principioActivo) {
        /*EL método insert recibe tres parámetros, el primero de ellos será el nombre de la tabla,
         el tercero serán los valores del registro a insertar, y el segundo
         se hace necesario en casos muy puntuales (por ejemplo para poder insertar registros completamente vacíos)
         */
        SQLiteDatabase db = getWritableDatabase();
        int pp;
        if (db != null) {
            //Los valores a insertar los pasaremos como elementos de una colección de tipo ContentValues.
            // creamos el registro a insertar como objeto ContentValues
            ContentValues valores = new ContentValues();
            valores.put("nombreMedicina", nombreMedicina);
            valores.put("principioActivo", principioActivo);
            //insertamos el registro en la Base de Datos
            db.insert("medicinas", null, valores);
        }
       //Toast.makeText(this,"medicina insertada",Toast.LENGTH_LONG).show();

        db.close();
    }

    public void modificarMedicina(int id, String nombreMedicina, String principioActivo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreMedicina", nombreMedicina);
        valores.put("principioActivo", principioActivo);
        //insertamos el registro en la Base de Datos
        db.update("medicinas", valores, "_id=" + id, null);
        db.close();

    }

    public void borrarMedicina(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("medicinas", "_id=" + id, null);
        db.close();
    }

    public Medicina recuperarMedicina(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombreMedicina", "principioActivo"};
        Cursor c = db.query("contactos", valores_recuperar, "_id=" + id, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        Medicina medicina = new Medicina(c.getInt(0), c.getString(1), c.getString(2));
        db.close();
        c.close();
        return medicina;
    }



    public ArrayList recuperarMedicinaArrray(String nombre) {
        ArrayList<Medicina> arrayList = new ArrayList<>();
        Medicina medicina;

        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombreMedicina", "principioActivo"};


        Cursor c = db.query("medicinas", valores_recuperar, null, null, null, null, null, null);
        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex("nombre")).toLowerCase().contains(nombre.toLowerCase())) {
                medicina = new Medicina(c.getInt(0), c.getString(1), c.getString(2));
                arrayList.add(medicina);
            }
        }
        return arrayList;

    }

    public Cursor recuperarMedicinaCursor() {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombreMedicina", "principioActivo"};
        Cursor c = db.query("medicinas", valores_recuperar, null, null, null, null, null, null);
        return c;

    }

    public int numerodeFilas() {
        int dato = (int) DatabaseUtils.queryNumEntries(this.getWritableDatabase(), "medicinas");
        return dato;
    }

    public int[] recuperaIds() {
        int[] datosId;
        int i;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM medicinas", null);
        if (cursor.getCount() > 0) {
            datosId = new int[cursor.getCount()];
            i = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                datosId[i] = cursor.getInt(0);
                i++;
                cursor.moveToNext();
            }
        } else datosId = new int[0];
        cursor.close();
        return datosId;
    }
}
