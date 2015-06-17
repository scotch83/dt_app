package be.ehb.dt_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import be.ehb.dt_app.R;

public class RegistrationFragment2 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration_fragment2, container, false);

        EditText schoolStad = (EditText) v.findViewById(R.id.et_stad_secundaireschool);
        EditText schoolNaam = (EditText) v.findViewById(R.id.sp_secundaire_school);


        return v;
    }
}
