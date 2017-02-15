package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.ju.taun15a16.group5.mjilkmjecipes.recipelist.ShowRecipeListActivity;

public class MainActivity extends MainMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRecentRecipes = (Button) findViewById(R.id.button_main_recent_recipes);
        btnRecentRecipes.setOnClickListener(view -> launchRecentActivity(null));

        Button btnMyRecipes = (Button) findViewById(R.id.button_main_my_recipes);
        btnMyRecipes.setOnClickListener(view -> launchMyRecipesActivity(null));

        Button btnFavorites = (Button) findViewById(R.id.button_main_favorites);
        btnFavorites.setOnClickListener(view -> launchFavoritesActivity(null));

        /*
        Button btnCamera = new Button(this);
        btnCamera.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnCamera.setText("Camera");
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadImageActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.activity_main);
        layout1.addView(btnCamera);
        */

        /*
        Button btnDebug = new Button(this);
        btnDebug.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnDebug.setText("Debug Menu");
        btnDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true){
                    Intent intent = new Intent(this, DebugActivity.class);
                    startActivity(intent);
                }
            }
        });
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main);
        layout.addView(btnDebug);
        */
    }

    public void launchRecentActivity(View view){
        Intent intent = new Intent(this, ShowRecipeListActivity.class);
        intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, ShowRecipeListActivity.EXTRA_RECENT);
        intent.putExtra(ShowRecipeListActivity.EXTRA_PAGE, 1);
        startActivity(intent);
    }

    public void launchMyRecipesActivity(View view){
        Intent intent = new Intent(this, ShowRecipeListActivity.class);
        intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, ShowRecipeListActivity.EXTRA_MY);
        startActivity(intent);
    }

    public void launchFavoritesActivity(View view){
        Intent intent = new Intent(this, ShowRecipeListActivity.class);
        intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, ShowRecipeListActivity.EXTRA_FAVORITES);
        startActivity(intent);
    }
}
