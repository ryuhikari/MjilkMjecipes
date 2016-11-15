package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends MainMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_profile =(Button) findViewById(R.id.button_main_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchProfileActivity(null);
            }
        });

        Button btn_my_recipes =(Button) findViewById(R.id.button_main_my_recipes);
        btn_my_recipes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchShowListActivity(null);
            }
        });
    }

    public void launchProfileActivity(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void launchShowListActivity(View view){
        Intent intent = new Intent(this, ShowListActivity.class);
        startActivity(intent);
    }
}
