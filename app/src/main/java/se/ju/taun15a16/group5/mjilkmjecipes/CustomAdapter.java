package se.ju.taun15a16.group5.mjilkmjecipes;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    private Resources resources;
    private ListModel tempValues = null;
    private int i = 0;

    /*************  CustomAdapter Constructor *****************/
    CustomAdapter(Activity activity, ArrayList data, Resources resources) {

        /********** Take passed values **********/
        this.activity = activity;
        this.data = data;
        this.resources = resources;

        /***********  Layout inflater to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if( data.size() <= 0 )
            return 1;
        return data.size();

    }

    public Object getItem( int position ) {
        return position;
    }

    public long getItemId( int position ) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    private static class ViewHolder {

        ImageView recipeImage;
        TextView recipeName;
        TextView recipeAuthor;
        RatingBar recipeRating;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if( convertView == null ){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.recipeImage = (ImageView) view.findViewById( R.id.imageView_tabitem_recipe_picture );
            holder.recipeName = (TextView) view.findViewById( R.id.textView_tabitem_recipe_name );
            holder.recipeAuthor = (TextView) view.findViewById( R.id.textView_tabitem_recipe_author );
            holder.recipeRating = (RatingBar) view.findViewById( R.id.ratingBar_tabitem_recipe_rating );

            /************  Set holder with LayoutInflater ************/
            view.setTag( holder );
        }
        else
            holder=(ViewHolder)view.getTag();

        if( data.size() <=0 )
        {
            holder.recipeName.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues = (ListModel) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.recipeName.setText( tempValues.getRecipeName() );
            holder.recipeAuthor.setText( tempValues.getRecipeAuthor() );

            int resID = resources.getIdentifier( tempValues.getRecipeImage(), "drawable", "se.ju.taun15a16.group5.mjilkmjecipes");
            holder.recipeImage.setImageResource(resID);

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            view.setOnClickListener(new OnItemClickListener( position ));
        }
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            ShowListActivity sct = (ShowListActivity) activity;

            /****  Call  onItemClick Method inside ShowListActivity Class ( See Below )****/

            sct.onItemClick( mPosition );
        }
    }
}
