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
                                info = debugRestManager.createAccount("AdminMjilkRecipes2","Admin!1",0.0,0.0);
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
                                if ( RESTManager.getInstance().createRecipe(recipe, getApplicationContext()) ){
                                    callToast("Recipe created successfully!");
                                }
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
                            try {
                                boolean result = RESTManager.getInstance().deleteAccount(getApplicationContext(), AccountManager.getInstance().getUserID(getApplicationContext()));
                                callToast("Deleting Account was: " + result);
                            } catch (HTTP401Exception e) {
                                e.printStackTrace();
                            } catch (HTTP404Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "deleteComment":
                            break;
                        case "deleteRecipe":
                            try {
                                if ( RESTManager.getInstance().deleteRecipe(getApplicationContext(), "230") ) {
                                    callToast("Recipe deleted successfully!");
                                }
                            } catch (HTTP401Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                                callToast(e.getMessage());
                            } catch (HTTP404Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                                callToast(e.getMessage());
                            }
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
                            try {
                                JSONObject data = RESTManager.getInstance().getRecipe("230");
                                callToast(data.toString());
                            } catch (HTTP404Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                            }
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
                            try {
                                boolean result = RESTManager.getInstance().updateAccountInfo(getApplicationContext(),AccountManager.getInstance().getUserID(getApplicationContext()),1.0,1.0);
                                callToast("Updating Account was: " + result);
                            } catch (HTTP401Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                            } catch (HTTP404Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                            }
                            break;
                        case "updateAllFavoriteRecipesByAccount":
                            break;
                        case "updateComment":
                            break;
                        case "updateRecipe":
                            Recipe recipe2 = new Recipe();
                            recipe2.setId(231);
                            recipe2.setName("Test recipe modified");
                            recipe2.setCreatorId("69c23d21-f103-466f-9687-985c22f47964");
                            recipe2.setDescription("Recipe Description modified");
                            ArrayList<Direction> directions2 = new ArrayList<Direction>();
                            directions2.add(new Direction(recipe2.getId(),1,"Direction 1 modified"));
                            recipe2.setDirections(directions2);
                            try {
                                if ( RESTManager.getInstance().updateRecipe(getApplicationContext(), recipe2.getId(), recipe2) ){
                                    callToast("Recipe modified successfully!");
                                }
                            } catch (HTTP401Exception e) {
                                Log.e("DEBUG-REST", Log.getStackTraceString(e));
                                callToast(e.getMessage());
                            } catch (HTTP404Exception e) {
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
                    }
                }
            }.start();

        });
    }

    private void callToast(String message){
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show());

    }
}
