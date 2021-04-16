package ch.supsi.dti.isin.meteoapp.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.db.DatabaseHelper;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;

import static ch.supsi.dti.isin.meteoapp.activities.MainActivity.deleteData;
import static ch.supsi.dti.isin.meteoapp.activities.MainActivity.insertData;



public abstract class SingleFragmentActivity extends AppCompatActivity{
    protected abstract Fragment createFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_single_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


    }

    /*@Override
    public void onStart() {
        super.onStart();
        //Log.d("TAG", "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        //Log.d("TAG", "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d("TAG", "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        //deleteData();
        /*for (Location l: LocationsHolder.get(this).getLocations()) {
            insertData(l);
        }

        for(int i=1;i<LocationsHolder.get(this).getLocations().size();i++) {
            insertData(LocationsHolder.get(this).getLocations().get(i));
        }
        //Log.d("TAG", "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }*/

}