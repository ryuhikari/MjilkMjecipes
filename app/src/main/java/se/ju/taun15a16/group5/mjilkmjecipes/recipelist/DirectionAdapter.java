package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.R;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Direction;

/**
 * Created by kevin on 04.01.2017.
 */

public class DirectionAdapter extends ArrayAdapter<Direction> {

    private final Context context;
    private final ArrayList<Direction> listItems;
    private final int resource;

    public DirectionAdapter(Context context, int resource, ArrayList<Direction> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listItems = objects;
    }

    private static class ViewHolder {

        TextView directionTitle;
        TextView directionDescription;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        ViewHolder holder;

        if( convertView == null ){
            view = inflater.inflate(resource, null);

            holder = new ViewHolder();
            holder.directionTitle = (TextView) view.findViewById( R.id.textViewDirectionTitle );
            holder.directionDescription = (TextView) view.findViewById( R.id.textViewDirectionDescription );
            holder.directionTitle.setText(context.getString(R.string.recipe_step_title) + " " + listItems.get(position).getOrder());
            holder.directionDescription.setText(listItems.get(position).getDescription());

            /************  Set holder with LayoutInflater ************/
            view.setTag( holder );
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
