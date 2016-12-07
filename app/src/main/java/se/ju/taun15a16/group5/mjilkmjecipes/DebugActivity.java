package se.ju.taun15a16.group5.mjilkmjecipes;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountInfo;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Direction;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP400Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP401Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
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
                            AccountInfo info = null;
                            try {
                                info = debugRestManager.createAccount("AdminMjilkRecipes","Admin!1",0.0,0.0);
                                callToast(info.toString());
                            } catch (HTTP400Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                            }

                            break;
                        case "createAccountFacebook":
                            break;
                        case "createLoginToken":
                            break;
                        case "createLoginTokenFacebook":
                            break;
                        case "createRecipe":
                            Recipe recipe = new Recipe();
                            recipe.setName("Test recipe");
                            recipe.setCreatorId("69c23d21-f103-466f-9687-985c22f47964");
                            recipe.setDescription("Recipe Description");
                            ArrayList<Direction> directions = new ArrayList<Direction>();
                            directions.add(new Direction(recipe.getId(),1,"Direction 1"));
                            recipe.setDirections(directions);
                            try {
                                RESTManager.getInstance().createRecipe(recipe, getApplicationContext());
                            } catch (HTTP401Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                                callToast(e.getMessage());
                            } catch (HTTP400Exception e) {
                                Log.e("DEBUG-REST", e.errorCodesToString());
                                for(RESTErrorCodes code : e.getErrorCodes()){
                                    callToast(code.getDescription());
                                    switch (code){
                                        case DIRECTION_DESCRIPTION_MISSING:
                                            break;
                                    }
                                }
                            }
                            break;
                        case "deleteAccount":
                            break;
                        case "deleteComment":
                            break;
                        case "deleteRecipe":
                            break;
                        case "getAccountInfo":
                            try {
                                JSONObject data = RESTManager.getInstance().getAccountInfo(AccountManager.getInstance().getUserID(getApplicationContext()));
                                Log.d("DEBUG-REST", data.toString());
                                callToast(data.toString());
                            } catch (HTTP404Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                            }
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
                            callToast(recipes.toString());
                            break;
                        case "getRecipe":
                            break;
                        case "searchRecipes":
                            //TODO: Replace search term
                            try {
                                JSONArray data = RESTManager.getInstance().searchRecipes("TEST");
                                Log.d("DEBUG-REST", data.toString());
                                callToast(data.toString());
                            } catch (HTTP400Exception e) {
                                Log.e("DEBUG-REST", e.errorCodesToString());
                                for(RESTErrorCodes code : e.getErrorCodes()){
                                    callToast(code.getDescription());
                                    switch (code){
                                        case DIRECTION_DESCRIPTION_MISSING:
                                            break;
                                    }
                                }
                            }
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

    private void callToast(String message){
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show());

    }
}
