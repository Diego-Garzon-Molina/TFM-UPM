package com.example.diego.baymax;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.diego.baymax.FragmentoScanbar;
import com.example.diego.baymax.MainActivity;
import com.example.diego.baymax.R;

import java.util.ArrayList;

/**
 * Created by Diego on 14/03/2017.
 */

public class AlarmReceiver extends android.content.BroadcastReceiver {
    int notificaciones=0;

    @Override
    public void onReceive(Context context, Intent intent)  {
        Toast.makeText( context, "recibiendo peticion", Toast.LENGTH_SHORT ).show();
        if(!intent.getAction().contains("patata")) notification(notificaciones, R.drawable.baymax,"Hora de la medicacion","Debe tomar:\nNolotil 575 mg cáps duras.\nPrednisona Kern Pharma 30 mg",context);
        else alarmas(context);
        notificaciones++;
    }
    public void alarmas(Context context){
        Intent intent = new Intent(context,FragmentoScanbar.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public void notification(int id, int iconId, String titulo, String contenido, Context context ) {

        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, FragmentoScanbar.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        // Estructurar la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconId)
                .setTicker(titulo)
                // .setContentTitle(titulo)
                .setContentText(contenido)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.baymax));

        // Construir la notificación y emitirla
        notifyMgr.notify(id, builder.build());
        AlarmManager manager_notificacion = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction("patata");
        // alarmIntent.setFlags(PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,0);

        manager_notificacion.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000*60*10, pendingIntent);

    }
}