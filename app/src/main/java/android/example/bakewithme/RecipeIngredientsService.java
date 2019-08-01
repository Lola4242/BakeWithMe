package android.example.bakewithme;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.example.bakewithme.data.RecipeContract;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsService extends IntentService {

    public static final String ACTION_UPDADE_INGREDIENT_WIDGETS = "android.example.bakewithme.action.update_ingredient_widgets";

    public RecipeIngredientsService() {
        super("RecipeIngredientsService");
    }

    public static void startActionUpdate(Context context){
        Intent intent = new Intent(context, RecipeIngredientsService.class);
        intent.setAction(ACTION_UPDADE_INGREDIENT_WIDGETS);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDADE_INGREDIENT_WIDGETS.equals(action)){
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        Uri recipeQueryUri = RecipeContract.IngredientEntry.CONTENT_URI;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int lastViewed = prefs.getInt(MainActivity.LAST_VIEWED_RECIPE, 0);

        Cursor cursor = getContentResolver().query(
                recipeQueryUri,
                null,
                RecipeContract.StepEntry.COLUMN_RECIPE_ID + "=" + lastViewed,
                null,
                null);

        List<String> ingredients = new ArrayList<>();

        int quantity = cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUANTITY);
        int measure = cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE);
        int ingredient = cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT);



        while (cursor.moveToNext()){
            ingredients.add(cursor.getDouble(quantity) + " " + cursor.getString(measure) + " "
            + cursor.getString(ingredient));
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        RecipeWidgetProvider.updateAppWidget(this, appWidgetManager, ingredients, appWidgetIds);
    }

}
