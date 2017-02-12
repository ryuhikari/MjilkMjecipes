package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import se.ju.taun15a16.group5.mjilkmjecipes.NewRecipeActivity;
import se.ju.taun15a16.group5.mjilkmjecipes.R;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Direction;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP401Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

import static se.ju.taun15a16.group5.mjilkmjecipes.NewRecipeActivity.EXTRA_DIRECTIONS;

public class RecipeDetailFragment extends Fragment {

    private Boolean showEditButton = false;
    private Boolean markedAsFavorite = false;

    private ArrayList<Recipe> favoriteRecipes = new ArrayList<>();

    private long recipeId;
    private String recipeName = "";
    private String recipeAuthor = "";
    private float recipeRating = 0.0f;
    private String recipeDescription= "";
    private String recipeImageURL;

    LinearLayout rootLayout = null;

    ArrayList<Direction> directions = new ArrayList<>();

    private ImageView recipeImageImageView = null;
    private TextView recipeNameTextView = null;
    private TextView recipeAuthorTextView = null;
    private RatingBar recipeRatingTextView = null;
    private TextView recipeDescriptionTextView = null;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong("recipeId");
            recipeName = savedInstanceState.getString("recipeName");
            recipeAuthor = savedInstanceState.getString("recipeAuthor");
            recipeRating = savedInstanceState.getFloat("recipeRating");
            recipeDescription = savedInstanceState.getString("recipeDescription");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        rootLayout = (LinearLayout) v.findViewById(R.id.fragment_recipe_linearview);

        //TODO: Add Loading Bar
        recipeImageImageView = (ImageView) v.findViewById(R.id.imageView_recipe_detail_recipe_picture);
        recipeNameTextView = (TextView) v.findViewById(R.id.textView_recipe_detail_recipe_name);
        recipeAuthorTextView = (TextView) v.findViewById(R.id.textView_recipe_detail_recipe_author);
        recipeRatingTextView = (RatingBar) v.findViewById(R.id.ratingBar_recipe_detail_recipe_rating);
        recipeDescriptionTextView = (TextView) v.findViewById(R.id.recipeDescriptionText);

        recipeNameTextView.setText(recipeName);
        recipeAuthorTextView.setText(recipeAuthor);
        recipeRatingTextView.setRating(recipeRating);
        recipeDescriptionTextView.setText(recipeDescription);

        if(savedInstanceState == null){
            loadRecipeData();
            checkFavorite();
        }

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe, menu);

        if (showEditButton) {
            MenuItem editButton = menu.findItem(R.id.item_edit_recipe);
            editButton.setVisible(true);
        }

        if (markedAsFavorite) {
            MenuItem favoriteButton = menu.findItem(R.id.item_favorite_recipe);
            favoriteButton.setVisible(true);

            MenuItem notFavoriteButton = menu.findItem(R.id.item_not_favorite_recipe);
            notFavoriteButton.setVisible(false);
        } else {
            MenuItem favoriteButton = menu.findItem(R.id.item_favorite_recipe);
            favoriteButton.setVisible(false);

            MenuItem notFavoriteButton = menu.findItem(R.id.item_not_favorite_recipe);
            notFavoriteButton.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item_edit_recipe:
                Intent intent = new Intent(getContext(), NewRecipeActivity.class);

                ArrayList<String> directionDescriptions = new ArrayList<String>();

                for (Direction d : directions) {
                    directionDescriptions.add(d.getDescription());
                }

                intent.putExtra(NewRecipeActivity.EXTRA_ID, recipeId);
                intent.putExtra(NewRecipeActivity.EXTRA_NAME, recipeName);
                intent.putExtra(NewRecipeActivity.EXTRA_DESCRIPTION, recipeDescription);
                intent.putStringArrayListExtra(EXTRA_DIRECTIONS, directionDescriptions);
                startActivity(intent);
                return true;

            case R.id.item_favorite_recipe:
                updateFavoriteRecipes(false);
                break;

            case R.id.item_not_favorite_recipe:
                updateFavoriteRecipes(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void loadRecipeData(){
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected void onPreExecute() {
                View view = getView();
                if (view != null) {
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
                    recipeDescription = recipeData.getString("description");
                    recipeDescriptionTextView.setText(recipeDescription);

                    recipeImageURL  = recipeData.getString("image");
                    Log.v("Recipe Image URL JSON", recipeImageURL);
                    new DownLoadImageTask(recipeImageImageView).execute(recipeImageURL);

                    if (recipeAuthor.equals(AccountManager.getInstance().getUserName(getContext()))) {
                        showEditButton = true;
                        ActivityCompat.invalidateOptionsMenu(getActivity());
                    }

                    JSONArray directionArray = recipeData.getJSONArray("directions");
                    for(int i = 0; i < directionArray.length(); ++i){
                        JSONObject directionRAW = directionArray.getJSONObject(i);
                        Direction d = new Direction(recipeId, directionRAW.getInt("order"), directionRAW.getString("description"));

                        View child = getActivity().getLayoutInflater().inflate(R.layout.direction_item, null);
                        TextView title = (TextView) child.findViewById(R.id.textViewDirectionTitle);
                        title.setText(getActivity().getString(R.string.recipe_step_title) + " " + d.getOrder());
                        TextView description = (TextView) child.findViewById(R.id.textViewDirectionDescription);
                        description.setText(d.getDescription());
                        final float scale = getResources().getDisplayMetrics().density;
                        int padding = (int) (20 * scale + 0.5f);
                        child.setPadding(padding,0,0,0);
                        rootLayout.addView(child);
                        directions.add(d);
                    }

                } catch (JSONException e) {
                    Log.e("REST", Log.getStackTraceString(e));
                }
            }
        }.execute();
    }

    private void checkFavorite() {
        new AsyncTask<Void, Void, RESTErrorCodes[]>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected RESTErrorCodes[] doInBackground(Void... params) {

                RESTErrorCodes[] result = {};
                try {
                    RESTManager restManager = RESTManager.getInstance();
                    AccountManager accManager = AccountManager.getInstance();

                    JSONArray sched = null;
                    sched = restManager.getAllFavoriteRecipesByAccount(getContext(), accManager.getUserID(getContext()));

                    Log.d("REST", sched.toString());
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
                    favoriteRecipes = gson.fromJson(sched.toString(), type);

                    for(Recipe recipe : favoriteRecipes){
                        if ( recipe.getId() == recipeId ) {
                            markedAsFavorite = true;
                            ActivityCompat.invalidateOptionsMenu(getActivity());
                        }
                    }

                } catch (HTTP404Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (HTTP401Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getContext();
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
                    Toast.makeText(getContext(), "Error checking favorite recipes!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void updateFavoriteRecipes(Boolean addToFavorites) {

        List<String> newFavoriteRecipes = new ArrayList<>();

        for(Recipe recipe : favoriteRecipes){
            newFavoriteRecipes.add(Long.toString(recipe.getId()));
        }

        if (addToFavorites) {
            newFavoriteRecipes.add(Long.toString(recipeId));
        } else {
            newFavoriteRecipes.remove(Long.toString(recipeId));
        }

        new AsyncTask<Void, Void, RESTErrorCodes[]>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected RESTErrorCodes[] doInBackground(Void... params) {

                RESTErrorCodes[] result = {};
                try {
                    Boolean correct = RESTManager.getInstance().updateAllFavoriteRecipesByAccount( getContext(), AccountManager.getInstance().getUserID(getContext()), newFavoriteRecipes );
                } catch (HTTP401Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;
                } catch (HTTP404Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;
                }

                return result;
            }

            @Override
            protected void onPostExecute(RESTErrorCodes[] result) {

                if (result.length == 0) {
                    markedAsFavorite = addToFavorites;
                    ActivityCompat.invalidateOptionsMenu(getActivity());
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
                    Toast.makeText(getContext(), "Error checking favorite recipes!", Toast.LENGTH_LONG).show();
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

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Log.v("Image URL", urlOfImage+" "+recipeName);
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
                return;
            }
            imageView.setImageBitmap(result);
            Log.v("ImageView", imageView.getDrawable().toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("recipeId", recipeId);
        savedInstanceState.putString("recipeName", recipeName);
        savedInstanceState.putString("recipeAuthor", recipeAuthor);
        savedInstanceState.putFloat("recipeRating", recipeRating);
        savedInstanceState.putString("recipeDescription", recipeDescription);
    }

    public void setRecipe(long id) {
        this.recipeId = id;
    }

}
