package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import se.ju.taun15a16.group5.mjilkmjecipes.R;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment)
                getFragmentManager().findFragmentById(R.id.detail_frag);
        int recipeId = (int) getIntent().getExtras().get(EXTRA_RECIPE_ID);
        recipeDetailFragment.setRecipe(recipeId);
    }
}
