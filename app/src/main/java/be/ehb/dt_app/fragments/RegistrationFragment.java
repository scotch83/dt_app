package be.ehb.dt_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import be.ehb.dt_app.R;
import be.ehb.dt_app.aanvulling.Postcodezoeker;
import be.ehb.dt_app.activities.RegistrationActivity;


public class RegistrationFragment extends Fragment {

    private EditText emailET, postcodeET, voornaamET, achternaalET, straatnaamET, nummerET, stadET;
    private TextView emailTV;
    private LinearLayout llBottom;
    private ScrollView mScrollView;
    private Postcodezoeker postcodezoeker;

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
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        emailET = (EditText) v.findViewById(R.id.et_email);
        emailTV = (TextView) v.findViewById(R.id.tv_email);
        postcodeET = (EditText) v.findViewById(R.id.et_postcode);
        stadET = (EditText) v.findViewById(R.id.et_stad);

        postcodezoeker = new Postcodezoeker(postcodeET, stadET, getActivity());

        llBottom = (LinearLayout) v.findViewById(R.id.ll_registratie1_bottom);
        mScrollView = (ScrollView) v.findViewById(R.id.sv_registratie1);

        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (v.getId() == emailET.getId()) {
                    if (hasFocus) {
                        int[] loc = new int[2];
                        v.getLocationOnScreen(loc);
                        mScrollView.smoothScrollTo(loc[0], llBottom.getTop());
                        ((RegistrationActivity) getActivity()).scrollIndicator();
                    }
                }

            }
        });

        return v;
    }


}