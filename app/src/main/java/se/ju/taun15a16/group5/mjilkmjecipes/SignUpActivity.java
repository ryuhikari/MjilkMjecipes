package se.ju.taun15a16.group5.mjilkmjecipes;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Up navigation arrow on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
