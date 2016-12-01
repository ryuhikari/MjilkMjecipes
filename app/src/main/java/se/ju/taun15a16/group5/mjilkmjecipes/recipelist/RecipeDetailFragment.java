package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import se.ju.taun15a16.group5.mjilkmjecipes.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {

    private long recipeId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong("recipeId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ImageView image = (ImageView) view.findViewById(R.id.imageView_recipe_detail_recipe_picture);
            TextView name = (TextView) view.findViewById(R.id.textView_recipe_detail_recipe_name);
            TextView author = (TextView) view.findViewById(R.id.textView_recipe_detail_recipe_author);
            RatingBar rating = (RatingBar) view.findViewById(R.id.ratingBar_recipe_detail_recipe_rating);

            image.setImageResource(R.drawable.tacos);
            name.setText("Recipe "+recipeId);
            author.setText("Author "+recipeId);
            rating.setRating(recipeId/2);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("recipeId", recipeId);
    }

    public void setRecipe(long id) {
        this.recipeId = id;
    }

}
