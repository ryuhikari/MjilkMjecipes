package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.R;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class ShowListActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "recipeType";


    ListView list;
    CustomAdapter adapter;
    public ShowListActivity CustomListView = null;
    public ArrayList<Recipe> CustomListViewValuesArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);


        Intent intent = getIntent();
        String recipeType = intent.getStringExtra(EXTRA_TYPE);

        getSupportActionBar().setTitle(recipeType);


        CustomListView = this;
        list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData();
    }

    /****** Function to set data in ArrayList *************/
    public void setListData() {
        new AsyncTask<Void, Void, RESTErrorCodes[]>() {

            @Override
            protected RESTErrorCodes[] doInBackground(Void... params) {

                RESTErrorCodes[] result = {};
                try {
                    RESTManager restManager = RESTManager.getInstance();
                    AccountManager accManager = AccountManager.getInstance();

                    JSONArray sched = restManager.getAllCreatedRecipesByAccount(accManager.getUserID(getApplicationContext()));

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
                    CustomListViewValuesArray = gson.fromJson(sched.toString(), type);

                } catch (HTTP404Exception e) {
                    Log.getStackTraceString(e);

                    Context context = getApplicationContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                return result;
            }

            @Override
            protected void onPostExecute(RESTErrorCodes[] result) {

                if (result.length == 0) {
                    Resources resources = getResources();
                    //list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

                    /**************** Create Custom Adapter *********/
                    adapter = new CustomAdapter( CustomListView, CustomListViewValuesArray, resources );
                    list.setAdapter( adapter );

                } else {

                    // TODO: Finish coding all the error messages
                    for(int i = 0; i < result.length; ++i){
                        switch (result[i]){
                            case INVALID_USERNAME:
                                //TODO: Use textedit.setError("") for marking a textedit as incorrect!
                                break;
                            //TODO: Add all possible error codes here except for longitude and latitude
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Error getting recipes!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
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
