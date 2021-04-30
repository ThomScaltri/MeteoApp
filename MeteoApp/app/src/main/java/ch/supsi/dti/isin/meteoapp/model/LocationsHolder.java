package ch.supsi.dti.isin.meteoapp.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.Service.Http;
import ch.supsi.dti.isin.meteoapp.activities.MainActivity;

public class LocationsHolder {

    private static LocationsHolder sLocationsHolder; //Singleton
    private List<Location> mLocations = new ArrayList<>();

    private MainActivity ma=new MainActivity();


    public static LocationsHolder get(Context context) {
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder(context);

        return sLocationsHolder;
    }

    //metodo per aggiungere location alla lista
    public void insertData(String string){
        Location l = new Location();
        l.setName(string);
        mLocations.add(l);

    }

    public void addLocation(Location location){
        mLocations.add(location);
    }

    //Lettura di location da DB
    private LocationsHolder(Context context) {

        //Log.i("READ","GPS" + ma.readGPS().getName());

        if(MainActivity.checkDB()){
            Location location = new Location();
            location.setName("GPS"); //"Location # " + i + " "+ location.getLati() +"°  "+ location.getLongi()+ "°"
            mLocations.add(location);
            MainActivity.insertData(location);
            Http.doRequest(location);
        }

    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public Location getLocation(UUID id) {
        for (Location location : mLocations) {
            if (location.getId().equals(id))
                return location;
        }

        return null;
    }

}
