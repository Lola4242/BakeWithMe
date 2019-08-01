package android.example.bakewithme.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.example.bakewithme.data.RecipeContract;
import android.example.bakewithme.model.Ingredient;
import android.example.bakewithme.model.Recipe;
import android.example.bakewithme.model.Step;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public final class RecipeUtils {

    public static ContentValues[] getRecipeContentValuesFromJson(Context context, String json) {

        Gson gson = new Gson();

        Recipe[] recipesArray = gson.fromJson(json, Recipe[].class);

        ContentValues[] recipeContentValues = new ContentValues[recipesArray.length];


        for(int i = 0; i<recipesArray.length; i++){
            ContentValues recipeValues = new ContentValues();
            Recipe recipe = recipesArray[i];

            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_ID, recipe.getId());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());


            recipeContentValues[i] = recipeValues;
        }

        return recipeContentValues;
    }

    public static ContentValues[] recipesToContentValues(List<Recipe> recipesArray){
        ContentValues[] recipeContentValues = new ContentValues[recipesArray.size()];


        for(int i = 0; i<recipesArray.size(); i++){
            ContentValues recipeValues = new ContentValues();
            Recipe recipe = recipesArray.get(i);

            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_ID, recipe.getId());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());


            recipeContentValues[i] = recipeValues;
        }

        return recipeContentValues;

    }

    public static ContentValues[] recipesToStepContentValues(List<Recipe> recipeList){

        List<ContentValues> stepContentValues = new ArrayList<>();

        for(int i = 0; i<recipeList.size(); i++){
            Recipe recipe = recipeList.get(i);

            for(int j = 0; j<recipe.getSteps().size(); j++){
                ContentValues stepValues = new ContentValues();
                Step step = recipe.getSteps().get(j);

                stepValues.put(RecipeContract.StepEntry.COLUMN_ID, step.getId());
                stepValues.put(RecipeContract.StepEntry.COLUMN_DESCRIPTION, step.getDescription());
                stepValues.put(RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
                stepValues.put(RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL, step.getThumbnailURL());
                stepValues.put(RecipeContract.StepEntry.COLUMN_VIDEO_URL, step.getVideoURL());
                stepValues.put(RecipeContract.StepEntry.COLUMN_RECIPE_ID, recipe.getId());

                stepContentValues.add(stepValues);

            }

        }

        return stepContentValues.toArray(new ContentValues[stepContentValues.size()]);
    }

    public static ContentValues[] recipesToIngredientContentValues(List<Recipe> recipeList){

        List<ContentValues> stepContentValues = new ArrayList<>();

        for(int i = 0; i<recipeList.size(); i++){
            Recipe recipe = recipeList.get(i);

            for(int j = 0; j<recipe.getIngredients().size(); j++){
                ContentValues ingredientsValues = new ContentValues();
                Ingredient ingredient = recipe.getIngredients().get(j);

                ingredientsValues.put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
                ingredientsValues.put(RecipeContract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
                ingredientsValues.put(RecipeContract.IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
                ingredientsValues.put(RecipeContract.IngredientEntry.COLUMN_RECIPE_ID, recipe.getId());

                stepContentValues.add(ingredientsValues);

            }

        }

        return stepContentValues.toArray(new ContentValues[stepContentValues.size()]);
    }

    public static List<Recipe> getObjectFromJson(String json){

        List<Recipe> recipes = new ArrayList<>();

            Gson gson = new Gson();

            Recipe[] recipesArray = gson.fromJson(json, Recipe[].class);

            for(int i = 0; i<recipesArray.length; i++){
                recipes.add(recipesArray[i]);
            }

        return recipes;
    }
}
