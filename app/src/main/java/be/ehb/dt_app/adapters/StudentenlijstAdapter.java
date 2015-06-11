package be.ehb.dt_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.model.Subscription;

/**
 * Created by mobapp003 on 11/06/15.
 */
public class StudentenlijstAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Subscription tempValues = null;
    private Activity activity;
    private ArrayList<Subscription> studentenLijst;


    public StudentenlijstAdapter(Activity activity, ArrayList<Subscription> studentenLijst) {
        this.activity = activity;
        this.studentenLijst = studentenLijst;


        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return studentenLijst.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (convertView == null) {
            v = inflater.inflate(R.layout.ehb_studentenlijst_listview_layout, null);

            holder = new ViewHolder();
            holder.studentnaamTV = (TextView) v.findViewById(R.id.tv_studentnaam);
            holder.huidigeschoolTV = (TextView) v.findViewById(R.id.tv_huidigeschool);
            holder.interessesTV = (TextView) v.findViewById(R.id.tv_interesse_opleiding);

            v.setTag(holder);
        } else
            holder = (ViewHolder) v.getTag();
        if (studentenLijst.size() <= 0) {
            holder.studentnaamTV.setText("Geen studenten toegevoegd.");
        } else {
            tempValues = null;
            tempValues = studentenLijst.get(position);

            holder.studentnaamTV.setText(tempValues.getFirstName() + " " + tempValues.getLastName());
            //holder.huidigeschoolTV.setText((CharSequence) tempValues.getSchool());
            //holder.interessesTV.setText((CharSequence) tempValues.getInterests());
        }

        return v;
    }

    public static class ViewHolder {
        TextView studentnaamTV;
        TextView huidigeschoolTV;
        TextView interessesTV;

    }
}
