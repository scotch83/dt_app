package be.ehb.dt_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ehb.dt_app.R;


public class RegistrationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setUpDesign(getActivity());
    }

    /*private void setUpDesign(Activity activity) {


        View lay = getActivity().findViewById(R.id.rl_registratiefragment2);
        lay.setBackgroundResource(R.drawable.achtergrond2);

        int pic = R.drawable.achtergrond2;
        lay.setBackgroundResource(pic);

        ImageView logo = (ImageView) getActivity().findViewById(R.id.iv_ehbletter);
        logo.setAlpha(1f);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }


}
