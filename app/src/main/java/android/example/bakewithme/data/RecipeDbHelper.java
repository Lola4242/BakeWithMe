/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.example.bakewithme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Manages a local database for weather data.
 */
public class RecipeDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "recipe.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.
     */
    private static final int DATABASE_VERSION = 6;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_RECIPE_TABLE =

                "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +

                        /*
                         * WeatherEntry did not explicitly declare a column called "_ID". However,
                         * WeatherEntry implements the interface, "BaseColumns", which does have a field
                         * named "_ID". We use that here to designate our table's primary key.
                         */
                        RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RecipeContract.RecipeEntry.COLUMN_ID + " INTEGER NOT NULL, " +

                        RecipeContract.RecipeEntry.COLUMN_NAME + " STRING NOT NULL," +

                        RecipeContract.RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_IMAGE + " STRING NOT NULL);";

        final String SQL_CREATE_STEP_TABLE =

                "CREATE TABLE " + RecipeContract.StepEntry.TABLE_NAME_STEP + " (" +

                        /*
                         * WeatherEntry did not explicitly declare a column called "_ID". However,
                         * WeatherEntry implements the interface, "BaseColumns", which does have a field
                         * named "_ID". We use that here to designate our table's primary key.
                         */
                        RecipeContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RecipeContract.StepEntry.COLUMN_ID + " INTEGER NOT NULL, " +

                        RecipeContract.StepEntry.COLUMN_DESCRIPTION + " STRING NOT NULL," +
                        RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL + " STRING NOT NULL," +
                        RecipeContract.StepEntry.COLUMN_VIDEO_URL + " STRING NOT NULL," +
                        RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION + " STRING NOT NULL, " +
                        RecipeContract.StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL);";

        final String SQL_CREATE_INGREDIENT_TABLE =

                "CREATE TABLE " + RecipeContract.IngredientEntry.TABLE_NAME + " (" +

                        /*
                         * WeatherEntry did not explicitly declare a column called "_ID". However,
                         * WeatherEntry implements the interface, "BaseColumns", which does have a field
                         * named "_ID". We use that here to designate our table's primary key.
                         */
                        RecipeContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RecipeContract.IngredientEntry.COLUMN_INGREDIENT + " STRING NOT NULL, " +

                        RecipeContract.IngredientEntry.COLUMN_MEASURE + " STRING NOT NULL," +
                        RecipeContract.IngredientEntry.COLUMN_QUANTITY + " DOUBLE NOT NULL," +
                        RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL);";
        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);



    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.StepEntry.TABLE_NAME_STEP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientEntry.TABLE_NAME);


        onCreate(sqLiteDatabase);
    }
}