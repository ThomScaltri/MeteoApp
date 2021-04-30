package ch.supsi.dti.isin.meteoapp.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import ch.supsi.dti.isin.meteoapp.Service.Http;
import ch.supsi.dti.isin.meteoapp.Service.NotifyWorker;
import ch.supsi.dti.isin.meteoapp.db.CursorWrapper;
import ch.supsi.dti.isin.meteoapp.db.DBContentValues;
import ch.supsi.dti.isin.meteoapp.db.DatabaseHelper;
import ch.supsi.dti.isin.meteoapp.db.MeteoDbSchema;
import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;

public class MainActivity extends SingleFragmentActivity{

    //riferimento a fragment
    //fragment.aggiorna

    private static final String TAG = "GPS";
    ListFragment listFragment=new ListFragment();
    private static SQLiteDatabase mDatabase;

    @Override
    protected Fragment createFragment() {
        return listFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        mDatabase = new DatabaseHelper(context).getWritableDatabase();
        readData();
        findGps();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "notificaBella", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Ciao gatto");

            mNotificationManager.createNotificationChannel(channel);
        }

        PeriodicWorkRequest periodicRequest = new PeriodicWorkRequest.Builder(NotifyWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("POLL WORK", ExistingPeriodicWorkPolicy.KEEP, periodicRequest);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission not granted");
            requestPermissions();
        } else {
            Log.i(TAG, "Permission granted");
            listFragment.startLocationListener();
        }

        //mDatabase.close();

    }

    public static void insertData(Location entry) {
        ContentValues values = DBContentValues.getContentValues(entry);
        mDatabase.insert(MeteoDbSchema.TestTable.NAME, null, values);
    }

    public static void deleteData()
    {
        mDatabase.execSQL("delete from " + MeteoDbSchema.TestTable.NAME);
    }

    public static boolean checkDB()
    {
        Cursor c =mDatabase.rawQuery("select * from " + MeteoDbSchema.TestTable.NAME,null);
        if(c.getCount()==0)
            return true;
        else
            return false;
    }

    public Location findGps()
    {
        CursorWrapper cursor = queryData(null, null);
        try {
            cursor.moveToFirst();
            //LocationsHolder.get(this).setGps(cursor.getLocation());
            return cursor.getLocation();

        } finally {
            cursor.close();
        }
    }

    public void readData() {

        CursorWrapper cursor = queryData(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Location entry = cursor.getLocation();
                Http.doRequest(entry);
                LocationsHolder.get(this).addLocation(entry);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        //Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }

    private CursorWrapper queryData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MeteoDbSchema.TestTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null,  // having
                null  // orderBy
        );
        return new CursorWrapper(cursor);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            listFragment.startLocationListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    listFragment.startLocationListener();
                return;
            }
        }
    }

}
