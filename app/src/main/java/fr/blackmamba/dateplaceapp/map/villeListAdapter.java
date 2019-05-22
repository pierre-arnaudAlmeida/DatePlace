package fr.blackmamba.dateplaceapp.map;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.blackmamba.dateplaceapp.R;

public class villeListAdapter extends ArrayAdapter<ville>{
    private static final String TAG = "villeListAdapter";

    private Context mContext;
    int mResource;

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */




    public villeListAdapter(Context context, int resource, ArrayList<ville> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }


    @NonNull
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String distance = getItem(position).getDistance();
        String like = getItem(position).getLike();

        //Create the person object with the information
        ville ville = new ville(name,distance,like);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvDistance = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvLike = (TextView) convertView.findViewById(R.id.textView3);

        tvName.setText(name);
        tvDistance.setText(distance);
        tvLike.setText(like);

        return convertView;






    }
}

