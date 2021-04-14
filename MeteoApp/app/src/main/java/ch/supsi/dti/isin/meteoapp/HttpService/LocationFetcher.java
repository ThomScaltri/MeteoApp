package ch.supsi.dti.isin.meteoapp.HttpService;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.supsi.dti.isin.meteoapp.model.Location;

interface OnTaskCompleted {
    void onTaskCompleted(List<Location> location);
}

public class LocationFetcher extends AsyncTask<Location, Void, Void> {

    public static final String TAG = "METEOAPP";
    private static final String API_KEY = "9c7bc41f67eb76922b7785d7a39546ec"; //Nostro API KEY

    private OnTaskCompleted listener;

    public LocationFetcher(OnTaskCompleted listener) {
        this.listener = listener;
    }

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

    //ESEMPIO
    private Weather parseItems(JSONObject jsonBody) throws  JSONException {

        JSONObject list = jsonBody.getJSONArray("list").getJSONObject(0);
        JSONObject main = list.getJSONObject("main");
        Double temp = main.getDouble("temp");
        Double temp_min = main.getDouble("temp_min");
        Double temp_max = main.getDouble("temp_max");
        String cityName="";

        String name = list.getJSONArray("weather").getJSONObject(0).getString("main");
        String description = list.getJSONArray("weather").getJSONObject(0).getString("description");
        //System.out.println("info: " + temp + "," + temp_max +"," + temp_min +"," + descr);
        if(list.has("name"))
            return new Weather(name,description,temp,temp_max,temp_min,list.get("name").toString());
        else
            return new Weather(name,description,temp,temp_max,temp_min);
    }

    private Loca parseItems(List<Location> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            Location item = new Location();
            //item.setId(photoJsonObject.getString("id"));
            //item.setCaption(photoJsonObject.getString("title"));

            if (!photoJsonObject.has("url_s"))
                continue;

            //item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }

    @Override
    protected Void doInBackground(Location... locations) {
        List<Location> location=new ArrayList<>();
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
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);
                parseItems(location, jsonBody);

            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch items", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }

        }
        return null;
    }

    public static void doRequest(Location mLocation) {
        LocationFetcher t = new LocationFetcher();
        try {

            t.execute(mLocation).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(List<Location> items) {
        listener.onTaskCompleted(items);
    }
}
