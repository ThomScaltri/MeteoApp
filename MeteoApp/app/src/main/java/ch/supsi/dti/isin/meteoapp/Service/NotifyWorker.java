package ch.supsi.dti.isin.meteoapp.Service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ch.supsi.dti.isin.meteoapp.activities.MainActivity;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;


public class NotifyWorker extends Worker {

    private Context mContext;
    MainActivity ma;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext=context;
    }

    @Override
    public Result doWork() {
        Http.doRequest(ma.findGps());
        notify(ma.findGps());

        return Result.success();

    }

    private void notify(Location gps) {

        /*if(gps.getWeather().getTemp()> 15) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "default")
                    .setSmallIcon(0)
                    .setContentTitle("Notifica cambio temperatura")
                    .setContentText("Temperatura GPS cambiata")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
            manager.notify(0, mBuilder.build());
        }*/
    }






}
