package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;


import android.media.Image;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountInfo;

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

	private static int TIMEOUT = 1000;

	public RESTManager(){
	}
	
	public AccountInfo createAccount(String username, String password, double longitude, double latitude) {
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

			int status = con.getResponseCode();
			Log.d("REST",status + " " + con.getResponseMessage());

			switch(status){
				case 200:
				case 201:
					/*BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line + "\n");
					}
					br.close();
					String jsonData = sb.toString();*/
					break;

			}
		} catch (IOException e) {
			Log.e("REST", e.getStackTrace().toString());
		} catch (JSONException e) {
			Log.e("REST-JSON", e.getStackTrace().toString());
		} finally {
			if(con != null){
				con.disconnect();
			}
		}
		return null;
	}

	public boolean createAccountFacebook(String username, String token, double longitude, double latitude) {
		// TODO implement me
		return false;
	}
	
	public AccountInfo getAccountInfo(String userID) {
		// TODO implement me
		return null;
	}
	
	public boolean updateAccountInfo(String userID, double longitude, double latitude) {
		// TODO implement me
		return false;
	}
	
	public boolean deleteAccount(String userID) {
		// TODO implement me
		return false;
	}
	
	public void getAllCreatedRecipesByAccount(String userID) {
		// TODO implement me
	}
	
	public void getAllCommentsMadeByAccount(String userID) {
		// TODO implement me
	}
	
	public boolean updateAllFavorityRecipesByAccount(String userID, String[] recipeIDs) {
		// TODO implement me
		return false;
	}
	
	public void getAllFavoriteRecipesByAccount(String userID) {
		// TODO implement me
	}
	
	public String createLoginToken(String username, String password) {
		// TODO implement me
		return null;
	}

	public String createLoginTokenFacebook(String fbToken) {
		// TODO implement me
		return null;
	}
	
	public boolean createRecipe(Object[] recipeData) {
		// TODO implement me
		return false;
	}
	
	public void getMostRecentRecipes(int page) {
		// TODO implement me
	}
	
	public void getRecipe(String recipeID) {
		// TODO implement me
	}
	
	public boolean deleteRecipe(String recipeID) {
		// TODO implement me
		return false;
	}
	
	public boolean updateRecipe(String recipeID, Object[] recipeData) {
		// TODO implement me
		return false;
	}
	
	public boolean addImageToRecipe(String recipeID, Image image) {
		// TODO implement me
		return false;
	}
	
	public boolean addCommentToRecipe(String recipeID, Object[] commentData) {
		// TODO implement me
		return false;
	}
	
	public void getAllCommentsFromRecipe(String recipeID, Object[] comments) {
		// TODO implement me
	}
	
	public void searchRecipes(String searchTerm) {
		// TODO implement me
	}
	
	public boolean updateComment(String commentID, Object[] commentData) {
		// TODO implement me
		return false;
	}
	
	public boolean deleteComment(String commentID) {
		// TODO implement me
		return false;
	}
	
	public boolean addImageToComment(String commentID, Image image) {
		// TODO implement me
		return false;
	}

}

