package be.ehb.dt_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import be.ehb.dt_app.R;


public class RegistrationFragment extends Fragment {

    EditText emailET;
    TextView emailTV;
    LinearLayout llBottom;
    ScrollView mScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
                    }
                }

            }
        });

        return v;
    }


}