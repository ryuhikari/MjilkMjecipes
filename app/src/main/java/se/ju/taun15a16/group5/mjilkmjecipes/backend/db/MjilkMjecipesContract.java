package se.ju.taun15a16.group5.mjilkmjecipes.backend.db;

import android.provider.BaseColumns;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;

/**
 * Created by kevin on 04.01.2017.
 */

public class MjilkMjecipesContract {


    private static final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE" + RecipeEntry.TABLE_NAME + " (" +
            RecipeEntry.COLUMN_NAME_RECIPEID + " INTEGER PRIMARY KEY," + RecipeEntry.COLUMN_NAME_NAME      + " TEXT," +
            RecipeEntry.COLUMN_NAME_CREATED  + " INTEGER,"             + RecipeEntry.COLUMN_NAME_CREATORID + " TEXT)";

    private static final String SQL_CREATE_ACCOUNTS_TABLE = "CREATE TABLE" + AccountEntry.TABLE_NAME + " (" +
            AccountEntry.COLUMN_NAME_USERID + " TEXT PRIMARY KEY," + AccountEntry.COLUMN_NAME_USERNAME + " TEXT)";

    private MjilkMjecipesContract(){

    }

    public static class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "Recipes";
        public static final String COLUMN_NAME_RECIPEID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CREATED = "created";
        public static final String COLUMN_NAME_CREATORID = "creatorid";
    }

    public static class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "Accounts";
        public static final String COLUMN_NAME_USERID = "userid";
        public static final String COLUMN_NAME_USERNAME = "username";
    }
}
