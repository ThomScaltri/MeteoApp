package ch.supsi.dti.isin.meteoapp.model;

import android.icu.text.IDNA;

import java.util.UUID;

public class Location {
    private UUID Id;
    private String mName;
    private double lati;
    private double longi;
    private Information weather;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public Information getWeather() {
        return weather;
    }

    public void setWeather(Information weather) {
        this.weather = weather;
    }

    public Location() {
        Id = UUID.randomUUID();
        lati=0.0;
        longi=0.0;
        new Information("",0,0,0,"","");
    }

    public Location(String mName) {
        this.mName = mName;
    }

    public Location(UUID id, String mName, double lati, double longi) {
        Id = id;
        this.mName = mName;
        this.lati = lati;
        this.longi = longi;
        new Information("",0,0,0,"","");
    }

    @Override
    public String toString() {
        return "Location{" +
                "Id=" + Id +
                ", mName='" + mName + '\'' +
                ", lati=" + lati +
                ", longi=" + longi +
                //", weather=" + weather.toString() +
                '}';
    }
}