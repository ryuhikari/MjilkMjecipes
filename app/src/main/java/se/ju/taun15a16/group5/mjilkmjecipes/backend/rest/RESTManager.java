package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountInfo;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;

//import com.google.gson.Gson;

public class RESTManager
{
	
	private static final String BASE_PATH = "http://52.211.99.140/api/v1/";

	private static final String BASE_PATH_ACCOUNTS = "accounts/";

	private static final String PATH_PASSWORD = "password";

	private static final String PATH_RECIPES = "recipes/";

	private static final String PATH_COMMENTS = "comments/";

	private static final String PATH_FAVORITES = "favorites/";

	private static final String BASE_PATH_TOKEN = "tokens/password";

	private static final String PATH_RECIPES_PAGE = "?page=";

	private static final String PATH_IMAGE = "image/";

	private static final String PATH_RECIPES_SEARCH = "search?term=";

	private static int TIMEOUT = 10000;


	// Singleton Variable
	private static RESTManager managerInstance = new RESTManager();

	// Private constructor
	private RESTManager(){
	}

	// Get RESTManager instance
	public static RESTManager getInstance() {
		return managerInstance;
	}
	
	public JSONObject createAccount(String username, String password, double longitude, double latitude) throws HTTP400Exception {

		JSONObject returnData = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + PATH_PASSWORD);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);


			JSONObject data = new JSONObject();
			data.put("userName", username);
			data.put("password", password);
			data.put("longitude", longitude);
			data.put("latitude", latitude);
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write(data.toString());
			osw.flush();
			osw.close();

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					returnData = new JSONObject(jsonData);
					Log.d("REST",returnData.toString());
					break;
				case 400:
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();

					JSONObject obj = new JSONObject(jsonData);
					JSONArray jsonArray = obj.getJSONArray("errors");

					RESTErrorCodes[] errorCodes = new RESTErrorCodes[jsonArray.length()];
					for(int i = 0; i < errorCodes.length; ++i){
						errorCodes[i] = RESTErrorCodes.fromString(jsonArray.getString(i));
					}

					throw new HTTP400Exception("ERROR: HTTP 400 Error", errorCodes);
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return returnData;
	}

	public AccountInfo createAccountFacebook(String username, String token, double longitude, double latitude) {

		AccountInfo info = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + PATH_PASSWORD);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);


			JSONObject data = new JSONObject();
			data.put("userName", username);
			data.put("token", token);
			data.put("longitude", longitude);
			data.put("latitude", latitude);
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write(data.toString());
			osw.flush();
			osw.close();

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					JSONObject returnData = new JSONObject(jsonData);
					info = new AccountInfo(returnData.getString("id"), returnData.getString("userName"), returnData.getDouble("longitude"), returnData.getDouble("latitude"));
					Log.d("REST",info.toString());
					break;

			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return info;
	}
	
	public JSONObject getAccountInfo(String userID) throws HTTP404Exception {
		JSONObject data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoOutput(false);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					data = new JSONObject(jsonData);
					break;
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");

			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}

		return data;
	}
	
	public boolean updateAccountInfo(Context context, String userID, double longitude, double latitude) throws HTTP401Exception, HTTP404Exception {

		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			JSONObject inputData = new JSONObject();
			inputData.put("longitude", longitude);
			inputData.put("latitude", latitude);

			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write(inputData.toString());
			osw.flush();
			osw.close();

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
				case 204:
					break;
				case 401:
					throw new HTTP401Exception("ERROR: HTTP 401 Error");
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");

			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
			return false;
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
			return false;
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return true;
	}
	
	public boolean deleteAccount(Context context, String userID) throws HTTP401Exception, HTTP404Exception {
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
			con.setUseCaches(false);
			con.setDoInput(false);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
				case 204:
					break;
				case 401:
					throw new HTTP401Exception("ERROR: HTTP 401 Error");
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");

			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
			return false;
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return true;
	}
	
	public JSONArray getAllCreatedRecipesByAccount(String userID) throws HTTP404Exception {
		// TODO check

        JSONArray data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID + "/" + PATH_RECIPES);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST", status + " " + con.getResponseMessage());

			switch(status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
						sb.append(line).append("\n");
                    }
                    br.close();
                    String jsonData = sb.toString();
                    data = new JSONArray(jsonData);

                    break;
                case 404:
                    Log.e("REST-getAllRecipes", "Error 404 Not Found");
                    throw new HTTP404Exception("ERROR: HTTP 404 Error");
            }
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
        } catch (JSONException e) {
            Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}

        return data;
	}
	
	public void getAllCommentsMadeByAccount(String userID) {
		// TODO check

        JSONArray data = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID + "/" + PATH_COMMENTS);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept","application/json");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setAllowUserInteraction(false); //TODO: Check
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            con.connect();
            int status = con.getResponseCode();
            Log.d("REST",status + " " + con.getResponseMessage());

            switch(status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
						sb.append(line).append("\n");
                    }
                    br.close();
                    String jsonData = sb.toString();
                    data = new JSONArray(jsonData);
                    break;
                case 404:
                    Log.e("REST-getAllRecipes", "Error 404 Not Found");
                    break;
            }
        } catch (IOException e) {
            Log.e("REST", Log.getStackTraceString(e));
        } catch (JSONException e) {
            Log.e("REST-JSON", Log.getStackTraceString(e));
        } finally {
            if(con != null){
                con.disconnect();
            }
        }

	}
	
	public boolean updateAllFavoriteRecipesByAccount(Context context, String userID, List<String> recipeIDs) throws HTTP401Exception, HTTP404Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID + "/" + PATH_FAVORITES);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type","application/json");
            con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
            con.setRequestProperty("Accept","application/json");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false); //TODO: Check
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            // Send Favorite recipes to the server
            JSONArray jsonArray = new JSONArray();
            for (String recipeID : recipeIDs ) {
                JSONObject recipe = new JSONObject();
                try {
                    recipe.put("id", Integer.parseInt(recipeID));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(recipe);
            }

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(jsonArray.toString());
            osw.flush();
            osw.close();

            con.connect();
            int status = con.getResponseCode();
            Log.d("REST",status + " " + con.getResponseMessage());

            switch(status){
                case 200:
                case 201:
                case 204:
                    break;
                case 400:
                    break;
                case 401:
                    throw new HTTP401Exception("ERROR: HTTP 401 Error");
                case 404:
                    throw new HTTP404Exception("ERROR: HTTP 404 Error");
            }
        } catch (IOException e) {
            Log.e("REST-recipe", Log.getStackTraceString(e));
            return false;
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return true;
	}
	
	public JSONArray getAllFavoriteRecipesByAccount(Context context, String userID) throws HTTP404Exception, HTTP401Exception {
		// TODO check

		JSONArray data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_ACCOUNTS + userID + "/" + PATH_FAVORITES);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
            con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
            con.setUseCaches(false);
			con.setDoInput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST", status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					data = new JSONArray(jsonData);

					break;
				case 401:
                    throw new HTTP401Exception("ERROR: HTTP 401 Error");
				case 404:
					Log.e("REST-getAllRecipes", "Error 404 Not Found");
					throw new HTTP404Exception("ERROR: HTTP 404 Error");
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}

		return data;
	}
	
	public JSONObject createLoginToken(String username, String password) throws HTTP400Exception {
		// TODO implement me
		Log.d("REST","Creating Login-Token...");

		HttpURLConnection con = null;
		JSONObject returnData = null;
		try {
			URL url = new URL(BASE_PATH + BASE_PATH_TOKEN);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);


			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write("grant_type=password&username=" + username + "&password=" +  password);
			osw.flush();
			osw.close();


			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());



			BufferedReader br;
			StringBuilder sb;
			String line;
			String jsonData;

			switch(status){
				case 200:
				case 201:

					br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();
					returnData = new JSONObject(jsonData);
					returnData.put("timestamp", timestamp);
					break;

				case 400:
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();

					JSONObject obj = new JSONObject(jsonData);
					String errorCode = obj.getString("error");
					RESTErrorCodes code = RESTErrorCodes.fromString(errorCode);
				throw new HTTP400Exception("ERROR: 400", code);
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return returnData;
	}

	public JSONObject createLoginTokenFacebook(String fbToken) {
		// TODO implement me
		Log.d("REST","Creating Facebook Login-Token...");
		HttpURLConnection con = null;
		JSONObject returnData = null;
		try {
			URL url = new URL(BASE_PATH + "tokens/facebook");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write("grant_type=mjecipes.com/grants/facebook&token=" + fbToken);
			osw.flush();
			osw.close();


			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			BufferedReader br;
			StringBuilder sb;
			String line;
			String jsonData;

			switch(status){
				case 200:
				case 201:

					br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();
					returnData = new JSONObject(jsonData);
					break;

				case 400:
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();
					String errorCode = new JSONObject(jsonData).getString("error");
					Log.e("REST",errorCode);
					break;

				case -1:
					break;

			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return returnData;
	}
	
	public boolean createRecipe(Recipe recipeData, Context context) throws HTTP401Exception, HTTP400Exception {

		Recipe recipe = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept","application/json");

			// Authorization TODO: Check
			String header = "Bearer " + AccountManager.getInstance().getLoginToken(context);
			con.addRequestProperty("Authorization", header);

			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			Gson gson = new Gson();
            String jsonString = gson.toJson(recipeData);
			JSONObject data = new JSONObject(jsonString);

			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write(data.toString());
			osw.flush();
			osw.close();

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			BufferedReader br;
			StringBuilder sb;
			String line;
			String jsonData;

			switch(status){
				case 200:
				case 201:
                    Log.e("REST-recipe", "Created recipe");
					break;
				case 400:
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();

					JSONObject obj = new JSONObject(jsonData);
					JSONArray jsonArray = obj.getJSONArray("errors");

					RESTErrorCodes[] errorCodes = new RESTErrorCodes[jsonArray.length()];
					for(int i = 0; i < errorCodes.length; ++i){
						errorCodes[i] = RESTErrorCodes.fromString(jsonArray.getString(i));
					}

					throw new HTTP400Exception("ERROR: 400", errorCodes);
				case 401:
					throw new HTTP401Exception("401 Unauthorized");
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return true;
	}
	
	public JSONArray getMostRecentRecipes(int page) {
		JSONArray data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES.substring(0,PATH_RECIPES.length()-1) + PATH_RECIPES_PAGE + page);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:

					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					data = new JSONArray(jsonData);
					break;

			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}

		return data;
	}
	
	public JSONObject getRecipe(String recipeID) throws HTTP404Exception {
		JSONObject data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES + recipeID);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					data = new JSONObject(jsonData);
					break;
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return data;
	}
	
	public boolean deleteRecipe(Context context, String recipeID) throws HTTP401Exception, HTTP404Exception {
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES + recipeID);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
			con.setUseCaches(false);
			con.setDoInput(false);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
				case 204:
					break;
				case 401:
					throw new HTTP401Exception("ERROR: HTTP 401 Error");
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");

			}
		} catch (IOException e) {
			Log.e("REST-recipe", Log.getStackTraceString(e));
			return false;
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return true;
	}
	
	public boolean updateRecipe(Context context, long recipeID, Recipe recipeData) throws HTTP400Exception, HTTP401Exception, HTTP404Exception {

        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_PATH + PATH_RECIPES + recipeID);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
            con.setRequestProperty("Accept","application/json");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false); //TODO: Check
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            // Send new Recipe content
            Gson gson = new Gson();
            String jsonString = gson.toJson(recipeData);
            JSONObject data = new JSONObject(jsonString);

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(data.toString());
            osw.flush();
            osw.close();

            con.connect();
            int status = con.getResponseCode();
            Log.d("REST",status + " " + con.getResponseMessage());

            switch(status){
                case 200:
                case 201:
                case 204:
                    break;
                case 400:
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while((line = br.readLine()) != null){
                        sb.append(line).append("\n");
                    }
                    br.close();
                    String jsonData = "";
                    jsonData = sb.toString();

                    JSONObject obj = new JSONObject(jsonData);
                    JSONArray jsonArray = obj.getJSONArray("errors");

                    RESTErrorCodes[] errorCodes = new RESTErrorCodes[jsonArray.length()];
                    for(int i = 0; i < errorCodes.length; ++i){
                        errorCodes[i] = RESTErrorCodes.fromString(jsonArray.getString(i));
                    }
                    throw new HTTP400Exception("ERROR: 400", errorCodes);
                case 401:
                    throw new HTTP401Exception("ERROR: HTTP 401 Error");
                case 404:
                    throw new HTTP404Exception("ERROR: HTTP 404 Error");

            }
        } catch (IOException e) {
            Log.e("REST-recipe", Log.getStackTraceString(e));
            return false;
        } catch (JSONException e) {
            Log.e("REST-JSON", Log.getStackTraceString(e));
            return false;
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return true;
	}
	
	public boolean addImageToRecipe(Context context , String recipeID, Drawable imageDrawable) throws HTTP401Exception, HTTP404Exception {

        HttpURLConnection con = null;
        String boundary = Long.toHexString(System.currentTimeMillis());

        try {
            URL url = new URL(BASE_PATH + PATH_RECIPES + recipeID + "/" + PATH_IMAGE);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
            con.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false); //TODO: Check
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            String charset = "UTF-8";
            String CRLF = "\r\n";
            OutputStream output = con.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data;name=\"image\";filename=\"image.jpeg\"").append(CRLF);
            writer.append("Content-Type: image/jpeg").append(CRLF);
            writer.append(CRLF).flush();
            Bitmap bitmap = ((BitmapDrawable)imageDrawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            con.connect();
            int status = con.getResponseCode();
            Log.d("REST",status + " " + con.getResponseMessage());

            switch(status){
                case 204:
                    break;
                case 401:
                    throw new HTTP401Exception("ERROR: HTTP 401 Error");
                case 404:
                    throw new HTTP404Exception("ERROR: HTTP 404 Error");
            }
        } catch (IOException e) {
            Log.e("REST-recipe", Log.getStackTraceString(e));
            return false;
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return true;
	}
	
	public boolean addCommentToRecipe(Context context, String recipeID, String commentText, int grade, String commenterID) throws HTTP400Exception, HTTP401Exception, HTTP404Exception {
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES + recipeID + "/" +  PATH_COMMENTS);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept","application/json");

			// Authorization TODO: Check
			String header = "Bearer " + AccountManager.getInstance().getLoginToken(context);
			con.addRequestProperty("Authorization", header);

			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			JSONObject data = new JSONObject();
			data.put("text", commentText);
			data.put("grade", grade);
			data.put("commenterId", commenterID);

			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
			osw.write(data.toString());
			osw.flush();
			osw.close();

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			BufferedReader br;
			StringBuilder sb;
			String line;
			String jsonData;

			switch(status){
				case 200:
				case 201:
					break;
				case 400:
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();

					JSONObject obj = new JSONObject(jsonData);
					JSONArray jsonArray = obj.getJSONArray("errors");

					RESTErrorCodes[] errorCodes = new RESTErrorCodes[jsonArray.length()];
					for(int i = 0; i < errorCodes.length; ++i){
						errorCodes[i] = RESTErrorCodes.fromString(jsonArray.getString(i));
					}
					throw new HTTP400Exception("ERROR: 400", errorCodes);
				case 401:
					throw new HTTP401Exception("401 Unauthorized");
				case 404:
					throw new HTTP404Exception("404 Not Found");
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return true;
	}
	
	public JSONArray getAllCommentsFromRecipe(String recipeID) throws HTTP404Exception {
		JSONArray data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES + recipeID + "/" +  PATH_COMMENTS);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoOutput(false);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					data = new JSONArray(jsonData);
					break;
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return data;
	}
	
	public JSONArray searchRecipes(String searchTerm) throws HTTP400Exception {
		JSONArray data = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_RECIPES + PATH_RECIPES_SEARCH + searchTerm);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept","application/json");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					String jsonData = sb.toString();
					data = new JSONArray(jsonData);
					break;
				case 400:
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					sb = new StringBuilder();
					while((line = br.readLine()) != null){
						sb.append(line).append("\n");
					}
					br.close();
					jsonData = sb.toString();

					JSONObject obj = new JSONObject(jsonData);
					JSONArray jsonArray = obj.getJSONArray("errors");

					RESTErrorCodes[] errorCodes = new RESTErrorCodes[jsonArray.length()];
					for(int i = 0; i < errorCodes.length; ++i){
						errorCodes[i] = RESTErrorCodes.fromString(jsonArray.getString(i));
					}

					throw new HTTP400Exception("ERROR: 400", errorCodes);
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} catch (JSONException e) {
			Log.e("REST-JSON", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return data;
	}
	
	public boolean updateComment(Context context, String commentID, String commentText, int grade) throws HTTP400Exception, HTTP401Exception, HTTP404Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_PATH + PATH_COMMENTS + commentID);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PATCH");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
            con.setRequestProperty("Accept","application/json");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false); //TODO: Check
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            JSONObject data = new JSONObject();
            data.put("text", commentText);
            data.put("grade", grade);

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(data.toString());
            osw.flush();
            osw.close();

            con.connect();
            int status = con.getResponseCode();
            Log.d("REST",status + " " + con.getResponseMessage());

            switch(status){
                case 200:
                case 201:
                case 204:
                    break;
                case 400:
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while((line = br.readLine()) != null){
                        sb.append(line).append("\n");
                    }
                    br.close();
                    String jsonData = "";
                    jsonData = sb.toString();

                    JSONObject obj = new JSONObject(jsonData);
                    JSONArray jsonArray = obj.getJSONArray("errors");

                    RESTErrorCodes[] errorCodes = new RESTErrorCodes[jsonArray.length()];
                    for(int i = 0; i < errorCodes.length; ++i){
                        errorCodes[i] = RESTErrorCodes.fromString(jsonArray.getString(i));
                    }
                    throw new HTTP400Exception("ERROR: 400", errorCodes);
                case 401:
                    throw new HTTP401Exception("ERROR: HTTP 401 Error");
                case 404:
                    throw new HTTP404Exception("ERROR: HTTP 404 Error");

            }
        } catch (IOException e) {
            Log.e("REST-recipe", Log.getStackTraceString(e));
            return false;
        } catch (JSONException e) {
            Log.e("REST-JSON", Log.getStackTraceString(e));
            return false;
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return true;
	}
	
	public boolean deleteComment(Context context, String commentID) throws HTTP404Exception, HTTP401Exception {
		HttpURLConnection con = null;
		try {
			URL url = new URL(BASE_PATH + PATH_COMMENTS + commentID);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
			con.setUseCaches(false);
			con.setDoOutput(false);
			con.setAllowUserInteraction(false); //TODO: Check
			con.setConnectTimeout(TIMEOUT);
			con.setReadTimeout(TIMEOUT);

			con.connect();
			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 204:
				case 401:
					throw  new HTTP401Exception("ERROR: HTTP 401 Error");
				case 404:
					throw new HTTP404Exception("ERROR: HTTP 404 Error");
			}
		} catch (IOException e) {
			Log.e("REST", Log.getStackTraceString(e));
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return true;
	}
	
	public boolean addImageToComment(Context context, String commentID, Drawable imageDrawable) throws HTTP401Exception, HTTP404Exception {
        HttpURLConnection con = null;
        String boundary = Long.toHexString(System.currentTimeMillis());

        try {
            URL url = new URL(BASE_PATH + PATH_COMMENTS + commentID + "/" + PATH_IMAGE);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.addRequestProperty("Authorization", "Bearer " + AccountManager.getInstance().getLoginToken(context));
            con.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false); //TODO: Check
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            String charset = "UTF-8";
            String CRLF = "\r\n";
            OutputStream output = con.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data;name=\"image\";filename=\"image.jpeg\"").append(CRLF);
            writer.append("Content-Type: image/jpeg").append(CRLF);
            writer.append(CRLF).flush();
			Bitmap bitmap = ((BitmapDrawable)imageDrawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            con.connect();
            int status = con.getResponseCode();
            Log.d("REST",status + " " + con.getResponseMessage());

            switch(status){
                case 204:
                    break;
                case 401:
                    throw new HTTP401Exception("ERROR: HTTP 401 Error");
                case 404:
                    throw new HTTP404Exception("ERROR: HTTP 404 Error");
            }
        } catch (IOException e) {
            Log.e("REST-recipe", Log.getStackTraceString(e));
            return false;
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return true;
	}

}

