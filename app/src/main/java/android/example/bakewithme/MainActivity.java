package android.example.bakewithme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.example.bakewithme.data.RecipeContract;
import android.example.bakewithme.sync.RecipeSyncUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private final String TAG = MainActivity.class.getSimpleName();
    public static final int INDEX_TITLE = 2;

    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecyclerView;

    private static final int ID_STEP_LOADER = 44;

    public static final String LAST_VIEWED_RECIPE = "last_viewed_recipe";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecyclerView = findViewById(R.id.item_list);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this, this);

        mRecyclerView.setAdapter(mRecipeAdapter);

        getSupportLoaderManager().initLoader(ID_STEP_LOADER, null, this);

        RecipeSyncUtils.initialize(this);

        // Init on 1
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_VIEWED_RECIPE, 1);
        editor.commit();



    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {
            case ID_STEP_LOADER:
                Uri recipeQueryUri = RecipeContract.RecipeEntry.CONTENT_URI;

                return new CursorLoader(this,
                        recipeQueryUri,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);


        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mRecipeAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mRecipeAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int position) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_VIEWED_RECIPE, position + 1);
        editor.commit();
        RecipeIngredientsService.startActionUpdate(this);


        Log.d(TAG, "onClick: " + position);
        Intent stepIntent = new Intent(MainActivity.this, StepListActivity.class);
        stepIntent.putExtra(StepListActivity.POSITION, position);
        startActivity(stepIntent);


    }


}
