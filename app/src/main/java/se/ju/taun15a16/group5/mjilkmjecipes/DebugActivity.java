package se.ju.taun15a16.group5.mjilkmjecipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.lang.reflect.Method;
import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountInfo;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class DebugActivity extends AppCompatActivity {


    private RESTManager debugRestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debugRestManager = RESTManager.getInstance();
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
            new Thread(){
                @Override
                public void run() {
                    String selection = (String) dropdown.getSelectedItem();
                    switch(selection){
                        case "addCommentToRecipe":
                            break;
                        case "addImageToComment":
                            break;
                        case "addImageToRecipe":
                            break;
                        case "createAccount":
                            AccountInfo info = debugRestManager.createAccount("AdminMjilkRecipes","Admin!1",0.0,0.0);
                            //Toast.makeText(getApplicationContext(), info.toString(), Toast.LENGTH_SHORT).show();
                            break;
                        case "createAccountFacebook":
                            break;
                        case "createLoginToken":
                            break;
                        case "createLoginTokenFacebook":
                            break;
                        case "createRecipe":
                            break;
                        case "deleteAccount":
                            break;
                        case "deleteComment":
                            break;
                        case "deleteRecipe":
                            break;
                        case "getAccountInfo":
                            break;
                        case "getAllCommentsFromRecipe":
                            break;
                        case "getAllCommentsMadeByAccount":
                            break;
                        case "getAllCreatedRecipesByAccount":
                            break;
                        case "getAllFavoriteRecipesByAccount":
                            break;
                        case "getMostRecentRecipes":
                            JSONArray recipes = debugRestManager.getMostRecentRecipes(1);
                            //Toast.makeText(getApplicationContext(), recipes.toString(), Toast.LENGTH_LONG).show();
                            break;
                        case "getRecipe":
                            break;
                        case "searchRecipes":
                            break;
                        case "updateAccountInfo":
                            break;
                        case "updateAllFavoriteRecipesByAccount":
                            break;
                        case "updateComment":
                            break;
                        case "updateRecipe":
                            break;
                    }
                }
            }.start();

        });
    }
}
