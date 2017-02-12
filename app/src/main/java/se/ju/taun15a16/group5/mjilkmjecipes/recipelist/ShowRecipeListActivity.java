package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.app.SearchManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.CommentsFragment;
import se.ju.taun15a16.group5.mjilkmjecipes.R;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP400Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP401Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class ShowRecipeListActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "recipeType";
    public static final String EXTRA_SHOW = "recipeId";
    public static final String EXTRA_PAGE = "recipePage";

    public static final String EXTRA_SEARCH = "Search Recipes";
    public static final String EXTRA_RECENT = "Recent Recipes";
    public static final String EXTRA_MY = "My Recipes";
    public static final String EXTRA_FAVORITES = "Favorite Recipes";

    String recipeType = null;
    int recipePage = 1;
    ListView list;
    CustomAdapter adapter;
    public ShowRecipeListActivity CustomListView = null;
    public ArrayList<Recipe> CustomListViewValuesArray = new ArrayList<>();

    private View pageButtons;
    private Button nextPageButton;
    private Button previousPageButton;
    private TextView pageIndicator;

    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        recipeType = intent.getStringExtra(EXTRA_TYPE);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            recipeType = EXTRA_SEARCH;
        }

        long showRecipe;
        showRecipe = intent.getLongExtra(EXTRA_SHOW, -1);

        if ( showRecipe != -1 ) {
            itemClicked(showRecipe);
        }

        recipePage = intent.getIntExtra(EXTRA_PAGE, 0);

        if ( recipePage != 0 ) {
            pageButtons = findViewById(R.id.layout_page_buttons);
            pageButtons.setVisibility(View.VISIBLE);

            previousPageButton = (Button) findViewById(R.id.button_show_list_previous_page);
            nextPageButton = (Button) findViewById(R.id.button_show_list_next_page);
            pageIndicator = (TextView) findViewById(R.id.textView_show_list_page_number);

            if ( recipePage == 1 ) {
                previousPageButton.setEnabled(false);
            } else {
                previousPageButton.setEnabled(true);
            }

            pageIndicator.setText(Integer.toString(recipePage));
            previousPageButton.setOnClickListener(view -> changeToNextPage(false));
            nextPageButton.setOnClickListener(view -> changeToNextPage(true));
        }


        getSupportActionBar().setTitle(recipeType);


        CustomListView = this;
        list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        // TODO: CustomListViewValuesArray get all the recipes from server but they cannot be passed to the adapter bellow
        setListData();
    }

    private void changeToNextPage(Boolean nextPage) {
        if ( nextPage ) {
            Intent intent = new Intent(this, ShowRecipeListActivity.class);
            intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, ShowRecipeListActivity.EXTRA_RECENT);
            intent.putExtra(ShowRecipeListActivity.EXTRA_PAGE, recipePage+1);
            startActivity(intent);
        } else {
            if ( recipePage > 1 ) {
                Intent intent = new Intent(this, ShowRecipeListActivity.class);
                intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, ShowRecipeListActivity.EXTRA_RECENT);
                intent.putExtra(ShowRecipeListActivity.EXTRA_PAGE, recipePage-1);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onRestart();
        setListData();
    }

    /****** Function to set data in ArrayList *************/
    public void setListData() {

        new AsyncTask<Void, Void, RESTErrorCodes[]>() {

            @Override
            protected void onPreExecute() {
                findViewById(R.id.activity_show_list).setVisibility(View.GONE);
                findViewById(R.id.layoutShowBar).setVisibility(View.VISIBLE);
            }

            @Override
            protected RESTErrorCodes[] doInBackground(Void... params) {

                RESTErrorCodes[] result = {};
                try {
                    RESTManager restManager = RESTManager.getInstance();
                    AccountManager accManager = AccountManager.getInstance();

                    JSONArray sched = null;

                    switch( recipeType ) {
                        case EXTRA_SEARCH:
                            sched = restManager.searchRecipes(searchQuery);
                            break;
                        case EXTRA_RECENT:
                            sched = restManager.getMostRecentRecipes(recipePage);
                            break;
                        case EXTRA_MY:
                            sched = restManager.getAllCreatedRecipesByAccount(accManager.getUserID(getApplicationContext()));
                            break;
                        case EXTRA_FAVORITES:
                            sched = restManager.getAllFavoriteRecipesByAccount(getApplicationContext(), accManager.getUserID(getApplicationContext()));
                            break;
                        default:
                            finish();
                            break;
                    }
                    Log.d("REST", sched.toString());
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
                    CustomListViewValuesArray = gson.fromJson(sched.toString(), type);
                    for(Recipe r : CustomListViewValuesArray){
                        r.setCreatorId(AccountManager.getInstance().getUserID(getApplicationContext()));
                    }

                } catch (HTTP404Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getApplicationContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (HTTP401Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getApplicationContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (HTTP400Exception e) {
                    Log.e("REST", e.errorCodesToString());
                    for(RESTErrorCodes code : e.getErrorCodes()){
                        switch (code){
                            case TERM_MISSING:
                                Toast.makeText(getApplicationContext(), "Please, fill in the search.", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                        }
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(RESTErrorCodes[] result) {

                Log.v("important", CustomListViewValuesArray.toString());

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
                findViewById(R.id.layoutShowBar).setVisibility(View.GONE);
                findViewById(R.id.activity_show_list).setVisibility(View.VISIBLE);
            }
        }.execute();
    }
    /*===================================================================*/

    /*@Override*/
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragment_container_detail);
        if (fragmentContainer != null) {
            RecipeDetailFragment details = new RecipeDetailFragment();
            CommentsFragment comments = CommentsFragment.newInstance(id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            details.setRecipe(id);
            ft.replace(R.id.fragment_container_detail, details);
            ft.replace(R.id.fragment_container_comments, comments);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, id);
            startActivity(intent);
        }
    }

}
