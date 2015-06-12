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

import be.ehb.dt_app.R;
import be.ehb.dt_app.model.Pdf;

/**
 * Created by mobapp003 on 12/06/15.
 */
public class PdflijstAdapter extends BaseAdapter implements Filterable {

    private static LayoutInflater inflater = null;
    Pdf tempValues = null;
    private Activity activity;
    private ArrayList<Pdf> pdfLijst;
    private ArrayList<Pdf> orig;

    public PdflijstAdapter(Activity activity, ArrayList<Pdf> pdfLijst) {
        this.activity = activity;
        this.pdfLijst = pdfLijst;


        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Pdf> results = new ArrayList<Pdf>();
                if (orig == null)
                    orig = pdfLijst;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Pdf g : orig) {
                            if (g.getNaam().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pdfLijst = (ArrayList<Pdf>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pdfLijst.size();
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
            v = inflater.inflate(R.layout.ehb_pdflijst_listview, null);

            holder = new ViewHolder();
            holder.pdfnaamTV = (TextView) v.findViewById(R.id.tv_pdfnaam);
            holder.datumtoegevoegdTV = (TextView) v.findViewById(R.id.tv_toegevoegd_datum);


            v.setTag(holder);
        } else
            holder = (ViewHolder) v.getTag();
        if (pdfLijst.size() <= 0) {
            holder.pdfnaamTV.setText("Geen studenten toegevoegd.");
        } else {
            tempValues = null;
            tempValues = pdfLijst.get(position);

            holder.pdfnaamTV.setText(pdfLijst.get(position).getNaam());
            holder.datumtoegevoegdTV.setText(pdfLijst.get(position).getDatum());

        }

        return v;
    }

    public static class ViewHolder {
        TextView pdfnaamTV;
        TextView datumtoegevoegdTV;
    }


}
