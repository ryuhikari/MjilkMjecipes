package se.ju.taun15a16.group5.mjilkmjecipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Method;
import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class DebugActivity extends AppCompatActivity {


    private RESTManager debugRestManager = new RESTManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Spinner dropdown = (Spinner)findViewById(R.id.spinnerDebug);
        ArrayList<String> names = new ArrayList<>();
        Method[] methods = debugRestManager.getClass().getDeclaredMethods();
        for(Method m : methods){
            names.add(m.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
        dropdown.setAdapter(adapter);

        Button btnExecute = (Button) findViewById(R.id.btnExecute);
        btnExecute.setOnClickListener(view -> {
            String selection = (String) dropdown.getSelectedItem();
            Log.d("DEBUG-Menu", selection);
        });
    }
}
