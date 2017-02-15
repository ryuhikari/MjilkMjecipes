package se.ju.taun15a16.group5.mjilkmjecipes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        // Up navigation arrow on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountManager accManager = AccountManager.getInstance();

        String username = accManager.getUserName(getApplicationContext());

        EditText editText_username = (EditText) findViewById(R.id.editText_profile_username);
        editText_username.setText(username);

        EditText editText_longitude = (EditText) findViewById(R.id.editText_profile_longitude);
        EditText editText_latitude = (EditText) findViewById(R.id.editText_profile_latitude);

    }
}
