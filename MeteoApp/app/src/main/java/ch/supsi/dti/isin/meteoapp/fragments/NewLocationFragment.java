package ch.supsi.dti.isin.meteoapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.GregorianCalendar;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.model.Location;

public class NewLocationFragment extends DialogFragment {


    Button bSearch;
    EditText eTxt;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.add_location,null);
        eTxt=v.findViewById(R.id.editText);
        bSearch=v.findViewById(R.id.buttonAddLocation);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=eTxt.getText().toString();
                Location mLocation=new Location();
                mLocation.setName(name);


            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

}


