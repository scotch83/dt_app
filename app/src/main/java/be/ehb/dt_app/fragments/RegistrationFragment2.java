package be.ehb.dt_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import be.ehb.dt_app.R;
import be.ehb.dt_app.aanvulling.Postcodezoeker;
import be.ehb.dt_app.aanvulling.Schoolzoeker;

public class RegistrationFragment2 extends Fragment {

    EditText schoolStad, schoolNaam;
    private Postcodezoeker postcodezoeker;
    private Schoolzoeker schoolzoeker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration_fragment2, container, false);

        schoolStad = (EditText) v.findViewById(R.id.et_stad_secundaireschool);
        schoolNaam = (EditText) v.findViewById(R.id.sp_secundaire_school);

        postcodezoeker = new Postcodezoeker(null, null, getActivity());
        schoolzoeker = new Schoolzoeker(schoolStad, schoolNaam, getActivity(), postcodezoeker);

        return v;
    }

    public Postcodezoeker getPostcodezoeker() {
        return postcodezoeker;
    }

    public void setPostcodezoeker(Postcodezoeker postcodezoeker) {
        this.postcodezoeker = postcodezoeker;
    }

    public Schoolzoeker getSchoolzoeker() {
        return schoolzoeker;
    }

    public void setSchoolzoeker(Schoolzoeker schoolzoeker) {
        this.schoolzoeker = schoolzoeker;
    }
}
