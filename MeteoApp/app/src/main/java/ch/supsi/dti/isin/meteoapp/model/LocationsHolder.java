package ch.supsi.dti.isin.meteoapp.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.HttpService.Http;

public class LocationsHolder {

    private static LocationsHolder sLocationsHolder; //Singleton
    private List<Location> mLocations;



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
        mLocations = new ArrayList<>();
        Location location = new Location();
        location.setName("GPS"); //"Location # " + i + " "+ location.getLati() +"°  "+ location.getLongi()+ "°"
        mLocations.add(location);
        Http.doRequest(location);

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
