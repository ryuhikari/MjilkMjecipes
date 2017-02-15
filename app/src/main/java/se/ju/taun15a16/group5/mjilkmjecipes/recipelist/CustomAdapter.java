package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import se.ju.taun15a16.group5.mjilkmjecipes.R;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;

public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<Recipe> data = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private Resources resources;
    private Recipe tempValues = null;



    /*************  CustomAdapter Constructor *****************/
    CustomAdapter(Activity activity, ArrayList<Recipe> data, Resources resources) {

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
        TextView recipeCreated;

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
            holder.recipeCreated = (TextView) view.findViewById( R.id.textView_tabitem_recipe_created );

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
            tempValues = data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.recipeName.setText( tempValues.getName() );

            Date tempDate = new Date(tempValues.getCreated() * 1000L);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(tempDate);
            holder.recipeCreated.setText(date);

            final String imgURL  = tempValues.getImage();
            holder.recipeImage.setImageResource(R.drawable.no_image_available);
            new DownLoadImageTask(holder.recipeImage).execute(imgURL);

            /******** Set Item Click Listener for LayoutInflater for each row *******/

            view.setOnClickListener(new OnItemClickListener( position ));
        }
        return view;
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Log.v("Image URL", urlOfImage+" "+tempValues.getName());
            if (urlOfImage == null) {
                return null;
            }
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            if (result == null) {
                imageView.setImageResource(R.drawable.no_image_available);
            } else {
                imageView.setImageBitmap(result);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            Recipe recipeInPosition = new Recipe();
            recipeInPosition = data.get( position );
            mPosition = (int) recipeInPosition.getId();
            //mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            ShowRecipeListActivity sct = (ShowRecipeListActivity) activity;

            /****  Call  onItemClick Method inside ShowRecipeListActivity Class ( See Below )****/

            sct.itemClicked( mPosition );
        }
    }
}
