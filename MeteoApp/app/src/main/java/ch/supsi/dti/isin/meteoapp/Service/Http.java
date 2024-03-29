package ch.supsi.dti.isin.meteoapp.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.supsi.dti.isin.meteoapp.model.Information;
import ch.supsi.dti.isin.meteoapp.model.Location;

interface OnTaskCompleted {
    void onTaskCompleted(List<Location> location);
}

public class Http extends AsyncTask<Location, Void, Void> {

    public static final String TAG = "METEOAPP";
    private static final String API_KEY = "9c7bc41f67eb76922b7785d7a39546ec"; //Nostro API KEY

    private OnTaskCompleted listener;

    /*public LocationFetcher(OnTaskCompleted listener) {
        this.listener = listener;
    }*/

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();

            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private Information parseItems(JSONObject jsonBody) throws JSONException {

        JSONObject list = jsonBody.getJSONArray("list").getJSONObject(0);

        JSONObject main = list.getJSONObject("main");
        JSONObject weather = list.getJSONArray("weather").getJSONObject(0);

        Double tmp=main.getDouble("temp");
        Double temp_min = main.getDouble("temp_min");
        Double temp_max = main.getDouble("temp_max");

        String name=list.getString("name");
        String desc=weather.getString("description");
        String icon= weather.getString("icon");

        return new Information(name,temp_max,temp_min,tmp,desc,icon);
    }

    @Override
    protected Void doInBackground(Location... locations) {
        String url;

        for(int i=0;i<locations.length;i++) {
            try {
                if(locations[i].getName().equals("GPS")) {
                    url = Uri.parse("https://api.openweathermap.org/data/2.5/find")
                            .buildUpon()
                            .appendQueryParameter("lat", locations[i].getLati() + "")
                            .appendQueryParameter("lon", locations[i].getLongi() + "")
                            .appendQueryParameter("units", "metric")
                            .appendQueryParameter("appid", API_KEY)
                            .build().toString();
                }else {
                    url = Uri.parse("https://api.openweathermap.org/data/2.5/find")
                            .buildUpon()
                            .appendQueryParameter("q", locations[0].getName())
                            .appendQueryParameter("units", "metric")
                            .appendQueryParameter("appid", API_KEY)
                            .build().toString();
                }
                String jsonString = getUrlString(url);
                JSONObject jsonBody = new JSONObject(jsonString);

                int count=jsonBody.getInt("count");
                if(count>0){
                    locations[0].setWeather(parseItems(jsonBody));
                    //URL per icona
                    String urlIcon = "https://openweathermap.org/img/wn/" + locations[0].getWeather().getIcon() + "@2x.png";

                    try {
                        InputStream input = new java.net.URL(urlIcon).openStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        locations[0].getWeather().setImage(bitmap);
                    } catch (Exception e) { e.printStackTrace(); }
                }else{
                    locations[0].setWeather(null);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch items", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
        }
        return null;
    }

    public static void doRequest(Location mLocation) {
        Http t = new Http();
        try {

            t.execute(mLocation).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    protected void onPostExecute(List<Location> items) {
        listener.onTaskCompleted(items);
    }*/
}
