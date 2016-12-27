package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.recipelist.ShowListActivity;

public class MainMenu extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.item_new_recipe:
                launchNewRecipeActivity(null);
                return true;

            case R.id.item_search:
                return true;

            case R.id.item_settings:
                return true;
            case R.id.item_log_out:
                AccountManager.getInstance().logout(getApplicationContext());
                Toast.makeText(getApplicationContext(), R.string.facebook_logout_successful_notify, Toast.LENGTH_SHORT).show();
                launchLogInActivity(null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void launchNewRecipeActivity(View view){
        Intent intent = new Intent(this, NewRecipeActivity.class);
        startActivity(intent);
    }

    public void launchLogInActivity(View view){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
