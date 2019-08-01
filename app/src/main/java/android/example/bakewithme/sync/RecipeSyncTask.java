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
package android.example.bakewithme.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.example.bakewithme.data.RecipeContract;
import android.example.bakewithme.model.Recipe;
import android.example.bakewithme.utilities.NetworkUtils;
import android.example.bakewithme.utilities.RecipeUtils;

import java.net.URL;
import java.util.List;

public class RecipeSyncTask {


    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncRecipe(Context context) {


        try {
            /*
             * The getUrl method will return the URL that we need to get the forecast JSON for the
             * weather. It will decide whether to create a URL based off of the latitude and
             * longitude or off of a simple location as a String.
             */
            URL recipeRequestUrl = NetworkUtils.getUrl(context);

            /* Use the URL to retrieve the JSON */
            String jsonRecipeResponse = NetworkUtils.getResponseFromHttpUrl(recipeRequestUrl);

            List<Recipe> recipes = RecipeUtils.getObjectFromJson(jsonRecipeResponse);

            /* Parse the JSON into a list of weather values */
            ContentValues[] recipeValues = RecipeUtils
                    .recipesToContentValues(recipes);

            ContentValues[] stepValues = RecipeUtils
                    .recipesToStepContentValues(recipes);

            ContentValues[] ingredientValues = RecipeUtils
                    .recipesToIngredientContentValues(recipes);

            /*
             * In cases where our JSON contained an error code, getWeatherContentValuesFromJson
             * would have returned null. We need to check for those cases here to prevent any
             * NullPointerExceptions being thrown. We also have no reason to insert fresh data if
             * there isn't any to insert.
             */
            if (recipeValues != null && recipeValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver bakingContentResolver = context.getContentResolver();

                /* Delete old weather data because we don't need to keep multiple days' data */
                bakingContentResolver.delete(
                        RecipeContract.RecipeEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new weather data into Sunshine's ContentProvider */
                bakingContentResolver.bulkInsert(
                        RecipeContract.RecipeEntry.CONTENT_URI,
                        recipeValues);

                if(stepValues!= null && stepValues.length != 0){

                    /* Delete old weather data because we don't need to keep multiple days' data */
                    bakingContentResolver.delete(
                            RecipeContract.StepEntry.CONTENT_URI,
                            null,
                            null);

                    /* Insert our new weather data into Sunshine's ContentProvider */
                    bakingContentResolver.bulkInsert(
                            RecipeContract.StepEntry.CONTENT_URI,
                            stepValues);
                }

                if(ingredientValues!= null && ingredientValues.length != 0){

                    /* Delete old weather data because we don't need to keep multiple days' data */
                    bakingContentResolver.delete(
                            RecipeContract.IngredientEntry.CONTENT_URI,
                            null,
                            null);

                    /* Insert our new weather data into Sunshine's ContentProvider */
                    bakingContentResolver.bulkInsert(
                            RecipeContract.IngredientEntry.CONTENT_URI,
                            ingredientValues);
                }
            }

            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

}