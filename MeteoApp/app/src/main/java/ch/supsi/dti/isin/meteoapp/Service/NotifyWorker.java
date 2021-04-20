package ch.supsi.dti.isin.meteoapp.Service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;


public class NotifyWorker extends Worker {

    public static final String TAG = "METEOAPP";
    private static final String API_KEY = "9c7bc41f67eb76922b7785d7a39546ec"; //Nostro API KEY

    Http service;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // codice da eseguire


        //service.execute();
        return Result.success(); // con failure() lo scheduling viene interrotto! }

    }




}
