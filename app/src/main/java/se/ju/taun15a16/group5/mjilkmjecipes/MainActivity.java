package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import se.ju.taun15a16.group5.mjilkmjecipes.recipelist.ShowListActivity;

public class MainActivity extends MainMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMyRecipes = (Button) findViewById(R.id.button_main_my_recipes);
        btnMyRecipes.setOnClickListener(view -> launchMyRecipesActivity(null));

        Button btnFavorites = (Button) findViewById(R.id.button_main_favorites);
        btnFavorites.setOnClickListener(view -> launchFavoritesActivity(null));

        /*Button btnSearchRecipes = (Button) findViewById(R.id.button_search);
        btnSearchRecipes.setOnClickListener(view -> launchSearchActivity(null));*/

        Button btnProfile =(Button) findViewById(R.id.button_main_profile);
        btnProfile.setOnClickListener(view -> launchProfileActivity(null));

        /*Button btnLogout = (Button) findViewById(R.id.button_logout);
        btnLogout.setOnClickListener(view -> launchLoginActivity(null));*/


        Button btnDebug = new Button(this);
        btnDebug.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnDebug.setText("Debug Menu");
        btnDebug.setOnClickListener(view -> launchDebugActivity(null));
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main);
        layout.addView(btnDebug);
    }



    public void launchMyRecipesActivity(View view){
        Intent intent = new Intent(this, ShowListActivity.class);
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

    private void launchDebugActivity(View view){
        if(true){
            Intent intent = new Intent(this, DebugActivity.class);
            startActivity(intent);
        }
    }


}
