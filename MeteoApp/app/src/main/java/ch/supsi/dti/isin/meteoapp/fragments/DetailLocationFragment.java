package ch.supsi.dti.isin.meteoapp.fragments;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mIdTextView;
    private ImageView mIdImageView;

    public static DetailLocationFragment newInstance(UUID locationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID, locationId);

        DetailLocationFragment fragment = new DetailLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID locationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationsHolder.get(getActivity()).getLocation(locationId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_location, container, false);

        mIdTextView = v.findViewById(R.id.id_cityName);
        mIdTextView.setText(mLocation.getId().toString());
        setInformationsOnView(v);

        return v;
    }

    private void setInformationsOnView(View v) {
        mIdTextView = v.findViewById(R.id.id_cityName);
        if (mLocation.getWeather().getName() != null)
            mIdTextView.setText(mLocation.getWeather().getName());
        else
            mIdTextView.setText(mLocation.getName());

        mIdTextView = v.findViewById(R.id.id_temp);
        mIdTextView.setText(mLocation.getWeather().getTemp() + "");
        mIdTextView = v.findViewById(R.id.degreeMin);
        mIdTextView.setText(mLocation.getWeather().gettMin() + "");
        mIdTextView = v.findViewById(R.id.degreeMax);
        mIdTextView.setText(mLocation.getWeather().gettMax() + "");
        mIdTextView = v.findViewById(R.id.description);
        mIdTextView.setText(mLocation.getWeather().getDescription());

        mIdImageView=v.findViewById(R.id.icon);

        mIdImageView.setImageBitmap(mLocation.getWeather().getImage());


    }
}

