package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMyRecipes = (Button) findViewById(R.id.button_my_recipes);
        btnMyRecipes.setOnClickListener(view -> launchMyRecipesActivity(null));

        Button btnFavorites = (Button) findViewById(R.id.button_favorites);
        btnFavorites.setOnClickListener(view -> launchFavoritesActivity(null));

        Button btnSearchRecipes = (Button) findViewById(R.id.button_search);
        btnSearchRecipes.setOnClickListener(view -> launchSearchActivity(null));

        Button btnProfile =(Button) findViewById(R.id.button_profile);
        btnProfile.setOnClickListener(view -> launchProfileActivity(null));

        Button btnLogout = (Button) findViewById(R.id.button_logout);
        btnLogout.setOnClickListener(view -> launchLoginActivity(null));

    }

    public void launchMyRecipesActivity(View view){
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

    public void launchFavoritesActivity(View view){

    }

    public void launchSearchActivity(View view){
        Intent intent = new Intent(this, ShowListActivity.class);
        startActivity(intent);
    }

    public void launchProfileActivity(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void launchLoginActivity(View view){

    }


}
