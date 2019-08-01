package android.example.bakewithme;

import android.content.Intent;
import android.database.Cursor;
import android.example.bakewithme.data.RecipeContract;
import android.example.bakewithme.sync.RecipeSyncUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StepListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        StepAdapter.StepAdapterOnClickHandler {

    private final String TAG = StepListActivity.class.getSimpleName();

    public static final int INDEX_SHORT = 5;
    public static final int INDEX_DESCIRPTION = 2;


    private static final int ID_STEP_LOADER = 44;
    private static final int ID_INGREDIENT_LOADER = 45;

    public static final String POSITION = "position";

    private StepAdapter mStepAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition;

    private boolean mTwoPane;

    private Cursor mCursor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        } else {
            mTwoPane = false;

        }

        mRecyclerView = findViewById(R.id.item_list);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mStepAdapter = new StepAdapter(this, this);

        mRecyclerView.setAdapter(mStepAdapter);

        getSupportLoaderManager().initLoader(ID_STEP_LOADER, null, this);
        getSupportLoaderManager().initLoader(ID_INGREDIENT_LOADER, null, this);


        RecipeSyncUtils.initialize(this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {
            case ID_STEP_LOADER:
                Uri recipeQueryUri = RecipeContract.StepEntry.CONTENT_URI;

                Bundle b = getIntent().getExtras();
                mPosition = b.getInt(POSITION) + 1;

                return new CursorLoader(this,
                        recipeQueryUri,
                        null,
                        RecipeContract.StepEntry.COLUMN_RECIPE_ID + "=" + mPosition,
                        null,
                        null);
            case ID_INGREDIENT_LOADER:
                Uri IngredientQueryUri = RecipeContract.IngredientEntry.CONTENT_URI;

                Bundle b2 = getIntent().getExtras();
                mPosition = b2.getInt(POSITION) + 1;

                return new CursorLoader(this,
                        IngredientQueryUri,
                        null,
                        RecipeContract.StepEntry.COLUMN_RECIPE_ID + "=" + mPosition,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);


        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {


        switch (loader.getId()){
            case ID_STEP_LOADER:
                mCursor = data;
                mStepAdapter.swapCursor(data);
                if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
                //mRecyclerView.smoothScrollToPosition(mPosition);
                break;
            case ID_INGREDIENT_LOADER:
                List<String> ingredients = new ArrayList<>();

                int quantity = data.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUANTITY);
                int measure = data.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE);
                int ingredient = data.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT);



                while (data.moveToNext()){
                    try{
                        ingredients.add(data.getDouble(quantity) + " " + data.getString(measure) + " "
                                + data.getString(ingredient));
                    } catch (Exception e){

                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, ingredients);

                ListView listView = findViewById(R.id.items_listview);
                listView.setAdapter(adapter);
                break;

        }

        if(mTwoPane){
            if(loader.getId() == ID_STEP_LOADER){
                mCursor.moveToPosition(0);

                String description = mCursor.getString(2);
                String thumbNail = mCursor.getString(3);
                String video = mCursor.getString(4);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setStep(description, thumbNail, video);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.item_detail_container, detailFragment)
                        .commit();
            }

        }



    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mStepAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int position) {
        Log.d(TAG, "onClick: " + position);

        mCursor.moveToPosition(position);

        if(!mTwoPane){
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(DetailFragment.INDEX_DESCIPTION, mCursor.getString(2));
            intent.putExtra(DetailFragment.INDEX_THUMBNAIL, mCursor.getString(3));
            intent.putExtra(DetailFragment.INDEX_VIDEO, mCursor.getString(4));

            startActivity(intent);
        } else {
            mCursor.moveToPosition(position);

            String description = mCursor.getString(2);
            String thumbNail = mCursor.getString(3);
            String video = mCursor.getString(4);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setStep(description, thumbNail, video);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, detailFragment)
                    .commit();

        }
    }
}
