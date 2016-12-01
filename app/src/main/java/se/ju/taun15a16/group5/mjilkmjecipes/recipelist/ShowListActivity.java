package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.R;

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

    /*===================================================================*/

    /*@Override*/
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            RecipeDetailFragment details = new RecipeDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            details.setRecipe(id);
            ft.replace(R.id.fragment_container, details);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, (int)id);
            startActivity(intent);
        }
    }

}
