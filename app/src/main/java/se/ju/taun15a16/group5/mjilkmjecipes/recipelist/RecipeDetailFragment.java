package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.R;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Direction;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class RecipeDetailFragment extends Fragment {

    private long recipeId;
    private String recipeName = "";
    private String recipeAuthor = "";
    private float recipeRating = 0.0f;


    private ImageView recipeImage = null;
    private TextView recipeNameTextView = null;
    private TextView recipeAuthorTextView = null;
    private RatingBar recipeRatingTextView = null;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong("recipeId");
            recipeName = savedInstanceState.getString("recipeName");
            recipeAuthor = savedInstanceState.getString("recipeAuthor");
            recipeRating = savedInstanceState.getFloat("recipeRating");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        recipeImage = (ImageView) v.findViewById(R.id.imageView_recipe_detail_recipe_picture);
        recipeNameTextView = (TextView) v.findViewById(R.id.textView_recipe_detail_recipe_name);
        recipeAuthorTextView = (TextView) v.findViewById(R.id.textView_recipe_detail_recipe_author);
        recipeRatingTextView = (RatingBar) v.findViewById(R.id.ratingBar_recipe_detail_recipe_rating);

        recipeNameTextView.setText(recipeName);
        recipeAuthorTextView.setText(recipeAuthor);
        recipeRatingTextView.setRating(recipeRating);

        if(savedInstanceState == null){
            loadRecipeData();
        }

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_edit_recipe){
            //Do whatever you want to do
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void loadRecipeData(){
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected void onPreExecute() {
                View view = getView();
                if (view != null) {
                    getActivity().findViewById(R.id.recipePager).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.layoutShowRecipeBar).setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected JSONObject doInBackground(Void... voids) {
                JSONObject rawData = null;
                try {
                    RESTManager restManager = RESTManager.getInstance();
                    String id = Long.toString(recipeId);
                    rawData = restManager.getRecipe(id);
                    JSONArray rawCommentData = restManager.getAllCommentsFromRecipe(id);
                    Log.d("REST", rawCommentData.toString());
                    double averageRating = calculateAverageRating(rawCommentData);
                    rawData.put("averageRating", averageRating);
                    Log.d("REST", rawData.toString());
                } catch (HTTP404Exception | JSONException e) {
                    Log.e("REST", Log.getStackTraceString(e));
                }
                return rawData;
            }

            @Override
            protected void onPostExecute(JSONObject recipeData) {
                if(recipeData == null){
                    return;
                }

                try {
                    recipeName = recipeData.getString("name");
                    recipeNameTextView.setText(recipeName);
                    recipeAuthor = recipeData.getJSONObject("creator").getString("userName");
                    recipeAuthorTextView.setText(recipeAuthor);
                    recipeRating = (float)recipeData.getDouble("averageRating");
                    recipeRatingTextView.setRating(recipeRating);

                    JSONArray directionArray = recipeData.getJSONArray("directions");
                    ArrayList<Direction> directions = new ArrayList<>();
                    for(int i = 0; i < directionArray.length(); ++i){
                        JSONObject directionRAW = directionArray.getJSONObject(i);
                        Direction d = new Direction(recipeId, directionRAW.getInt("order"), directionRAW.getString("description"));
                        directions.add(d);
                    }
                    DirectionAdapter adapter = new DirectionAdapter(getActivity(), R.layout.direction_item, directions);
                    ListView directionListView = (ListView) getActivity().findViewById(R.id.listView_RecipeSteps);
                    directionListView.setAdapter(adapter);

                } catch (JSONException e) {
                    Log.e("REST", Log.getStackTraceString(e));
                }
                View view = getView();
                if (view != null) {
                    getActivity().findViewById(R.id.layoutShowRecipeBar).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.recipePager).setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    private double calculateAverageRating(JSONArray commentData){
        if(commentData.length() == 0){
            return 0.0;
        }
        double sum = 0;
        for(int i = 0; i < commentData.length(); ++i){
            try {
                sum += (double)commentData.getJSONObject(i).getInt("grade");
            } catch (JSONException e) {
                Log.e("REST", Log.getStackTraceString(e));
            }
        }
        return sum / (double)(commentData.length());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("recipeId", recipeId);
        savedInstanceState.putString("recipeName", recipeName);
        savedInstanceState.putString("recipeAuthor", recipeAuthor);
        savedInstanceState.putFloat("recipeRating", recipeRating);
    }

    public void setRecipe(long id) {
        this.recipeId = id;
    }

}
