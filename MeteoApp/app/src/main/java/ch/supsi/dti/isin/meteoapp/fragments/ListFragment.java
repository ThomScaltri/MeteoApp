package ch.supsi.dti.isin.meteoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import java.util.List;

import ch.supsi.dti.isin.meteoapp.Service.Http;
import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.activities.DetailActivity;
import ch.supsi.dti.isin.meteoapp.activities.MainActivity;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class ListFragment extends Fragment {
    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter; //madapter.notify per informare che il modello è cambiato

    private static final String TAG = "GPS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //setContentView(R.layout.fragment_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        startLocationListener();
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mLocationRecyclerView = view.findViewById(R.id.recycler_view);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Location> locations = LocationsHolder.get(getActivity()).getLocations();
        mAdapter = new LocationAdapter(locations);
        mLocationRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    public void startLocationListener(){

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.HIGH)
                .setDistance(0)
                .setInterval(5000);
        // 5 sec
        SmartLocation.with(getContext())
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(android.location.Location location) {
                        //Log.i(TAG, "GPS" + location);
                        Location gpsLoc= LocationsHolder.get(getContext()).getLocations().get(0);
                        gpsLoc.setLati(location.getLatitude());
                        gpsLoc.setLongi(location.getLongitude());
                        /*gpsLoc.setName("GPS"); // + " "+ gpsLoc.getLati() +"°  "+ gpsLoc.getLongi()+ "°"
                        //Log.i(TAG, "Lati" + location.getLongitude());
                        //Log.i(TAG, "Longi" + location.getLatitude());
                        //LocationsHolder.get(getActivity()).addLocation(gpsLoc);*/
                        Http.doRequest(gpsLoc);
                        mAdapter.notifyDataSetChanged();
                    }
                });


    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:

                final EditText editText = new EditText(getContext());
                editText.setText("");

                new AlertDialog.Builder(getContext())
                        .setTitle("Insert New Location")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //add location ad holder
                                String m_Text = editText.getText().toString();
                                Location location=new Location();
                                location.setName(m_Text);
                                Http.doRequest(location);

                                boolean flag=false;
                                for (Location l: LocationsHolder.get(getActivity()).getLocations()) {
                                    if(l.getWeather()==null)
                                        Log.i("ERRORE", "ERRORE");

                                    if(l.getWeather().getName().equals(location.getWeather().getName())) {
                                        flag=false;
                                        break;
                                    }
                                    else {
                                        flag=true;
                                    }
                                }

                                Toast toast;
                                if(location.getWeather()==null) {
                                    toast = Toast.makeText(getActivity(),
                                            "location not found",
                                            Toast.LENGTH_SHORT);
                                }else {
                                    if (flag) {
                                        location.setName(location.getWeather().getName());
                                        LocationsHolder.get(getActivity()).addLocation(location);
                                        MainActivity.insertData(location);
                                        mAdapter.notifyDataSetChanged();
                                        toast = Toast.makeText(getActivity(),
                                                "location added",
                                                Toast.LENGTH_SHORT);
                                    } else {
                                        mAdapter.notifyDataSetChanged();
                                        toast = Toast.makeText(getActivity(),
                                                "location already added",
                                                Toast.LENGTH_SHORT);
                                    }
                                }
                                toast.show();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setView(editText)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Holder

    private class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private Location mLocation;

        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);
        }

        public void bind(Location location) {
            mLocation = location;
            mNameTextView.setText(mLocation.getName());
        }
    }

    // Adapter

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private List<Location> mLocations;

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @Override
        public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LocationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, int position) {
            Location location = mLocations.get(position);
            holder.bind(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }
    }
}
