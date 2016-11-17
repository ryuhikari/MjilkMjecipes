package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "recipeType";


    ListView list;
    CustomAdapter adapter;
    public ShowListActivity CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArray = new ArrayList<ListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);


        Intent intent = getIntent();
        String recipeType = intent.getStringExtra(EXTRA_TYPE);

        getSupportActionBar().setTitle(recipeType);


        CustomListView = this;

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData();

        Resources resources = getResources();
        list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArray,resources );
        list.setAdapter( adapter );

    }

    /****** Function to set data in ArrayList *************/
    public void setListData()
    {

        for (int i = 0; i < 11; i++) {

            final ListModel sched = new ListModel();

            /******* Firstly take data in model object ******/
            sched.setRecipeImage( "tacos" );
            sched.setRecipeName( "Name "+i );
            sched.setRecipeAuthor( "Author"+i );
            sched.setRecipeRating( i );

            /******** Take Model Object in ArrayList **********/
            CustomListViewValuesArray.add( sched );
        }

    }


    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArray.get(mPosition);


        // SHOW ALERT

        Toast.makeText(CustomListView,
                ""+tempValues.getRecipeName() +" Image:"+tempValues.getRecipeImage() +" Url:"+tempValues.getRecipeAuthor() +"Rating:"+tempValues.getRecipeRating(),
                Toast.LENGTH_LONG).show();
    }
}