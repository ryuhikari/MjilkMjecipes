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

public class ShowListActivity extends AppCompatActivity implements RecipeListFragment.RecipeListListener {

    public static final String EXTRA_TYPE = "recipeType";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        Intent intent = getIntent();
        String recipeType = intent.getStringExtra(EXTRA_TYPE);

        getSupportActionBar().setTitle(recipeType);
    }

    @Override
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            RecipeDetailFragment details = new RecipeDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            details.setWorkout(id);
            ft.replace(R.id.fragment_container, details);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_WORKOUT_ID, (int)id);
            startActivity(intent);
        }
    }

}
