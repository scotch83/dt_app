package be.ehb.dt_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.ehb.dt_app.R;
import be.ehb.dt_app.model.Subscription;

/**
 * Created by mobapp003 on 11/06/15.
 */
public class StudentenlijstAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater inflater = null;
    private Subscription tempValues = null;
    private Activity activity;
    private ArrayList<Subscription> studentenLijst;
    private ArrayList<Subscription> orig;


    public StudentenlijstAdapter(Activity activity, ArrayList<Subscription> studentenLijst) {
        this.activity = activity;
        this.studentenLijst = studentenLijst;


        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Subscription> results = new ArrayList<>();
                if (orig == null)
                    orig = studentenLijst;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Subscription item : orig) {
                            if (item.getFirstName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(item);
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                studentenLijst = (ArrayList<Subscription>) results.values;
                notifyDataSetChanged();
            }
        };
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
            holder.interessesTV = (TextView) v.findViewById(R.id.tv_interesses);

            v.setTag(holder);
        } else
            holder = (ViewHolder) v.getTag();
        if (studentenLijst.size() <= 0) {
            holder.studentnaamTV.setText("Geen studenten toegevoegd.");
        } else {
            tempValues = null;
            tempValues = studentenLijst.get(position);

            holder.studentnaamTV.setText(tempValues.getFirstName() + " " + tempValues.getLastName());
            holder.huidigeschoolTV.setText(tempValues.getSchool().getName());
            String interestsConc = "";
            HashMap<String, String> interests = tempValues.getInterests();
            Iterator it = interests.entrySet().iterator();

            ArrayList<String> intList = new ArrayList<>();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                if (pair.getValue().equals("true")) {
                    intList.add(String.valueOf(pair.getKey()));
                }


            }
            for (String interest : intList)
                interestsConc += "- " + interest.toUpperCase() + "\n";
            holder.interessesTV.setText(interestsConc);


        }

        return v;
    }

    public static class ViewHolder {
        TextView studentnaamTV;
        TextView huidigeschoolTV;
        TextView interessesTV;

    }
}
