package be.ehb.dt_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.StudentenlijstAdapter;
import be.ehb.dt_app.model.Subscription;


public class StudentenlijstFragment extends Fragment {

    public StudentenlijstFragment studentenlijstFragment = null;
    public ArrayList<Subscription> studentenlijstArray = new ArrayList<Subscription>();
    ListView studentenlijstLV;
    StudentenlijstAdapter slAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_studentenlijst, container, false);

        Subscription testStudent = new Subscription("Raf", "Cannaerts", "anke.cannaerts@gmail.com", "Baron De Celleslaan", "75", "2650", "Edegem");
        Subscription testStudent2 = new Subscription("Joke", "Cannaerts", "anke.cannaerts@gmail.com", "Baron De Celleslaan", "75", "2650", "Edegem");
        studentenlijstArray.add(testStudent);
        studentenlijstArray.add(testStudent2);

        slAdapter = new StudentenlijstAdapter(getActivity(), studentenlijstArray);

        studentenlijstLV = (ListView) v.findViewById(R.id.lv_studentenlijst);
        studentenlijstLV.setAdapter(slAdapter);

        return v;
    }


}
