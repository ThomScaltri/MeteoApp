package ch.supsi.dti.isin.meteoapp.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.Arrays;

public class Information {

    private String name;
    private double tMax;
    private double tMin;
    private double temp;
    private String description;
    private String icon;
    private Bitmap image;

    public Information(String name,double tMax, double tMin, double temp, String description, String icon) {
        this.name=name;
        this.tMax = tMax;
        this.tMin = tMin;
        this.temp = temp;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double gettMax() {
        return tMax;
    }

    public void settMax(double tMax) {
        this.tMax = tMax;
    }

    public double gettMin() {
        return tMin;
    }

    public void settMin(double tMin) {
        this.tMin = tMin;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;//Arrays.copyOf(image, image.length);
    }

    @Override
    public String toString() {
        return "Information{" +
                "tMax=" + tMax +
                ", tMin=" + tMin +
                ", temp=" + temp +
                ", description='" + description + '\'' +
                ", name='" + icon + '\'' +
                '}';
    }
}
