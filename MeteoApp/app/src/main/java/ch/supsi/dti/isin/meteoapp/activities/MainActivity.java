package ch.supsi.dti.isin.meteoapp.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

import ch.supsi.dti.isin.meteoapp.HttpService.LocationFetcher;
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
        mDatabase.insert(MeteoDbSchema.TestTable.NAME, null, values); //ERRORE DI ACCESSO
    }

    public static void deleteData()
    {
        mDatabase.execSQL("delete from " + MeteoDbSchema.TestTable.NAME );
    }

    public void readData() {

        CursorWrapper cursor = queryData(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Location entry = cursor.getLocation();
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


    /*@Override
    public void onDestroy() {
        super.onDestroy();
        /*Log.d(TAG, "Stampa lista");

        for (Location l:LocationsHolder.get(this).getLocations()) {
            insertData(l);
            Log.d(TAG, LocationsHolder.get(this).getLocations().toString());
        }

        Log.d(TAG, "onDestroy() called");
    }*/
}
