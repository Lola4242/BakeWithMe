package android.example.bakewithme;

import android.content.Context;
import android.example.bakewithme.model.Step;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class DetailFragment extends Fragment {

    private String description;
    private String thumbnail;
    private String video;

    public static final String LIST_INDEX = "list_index";

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private TextView mDescription;

    public static final String INDEX_DESCIPTION = "indexDescription";
    public static final String INDEX_THUMBNAIL = "indexThumbnail";
    public static final String INDEX_VIDEO = "indexVideo";

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            //mCallback = (StepFragment.OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public DetailFragment(){

    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            //mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
            //mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        final View rootView = inflater.inflate(R.layout.details_fragment, container, false);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        //GridView gridView = rootView.findViewById(R.id.step_grid_view);


        if(!video.equals("")){
            initializePlayer(Uri.parse(video));
        } else if (!thumbnail.equals("")){
            initializePlayer(Uri.parse(thumbnail));

        } else {
            mPlayerView.setVisibility(View.GONE);
        }

        mDescription = rootView.findViewById(R.id.description);

        mDescription.setText(description);



        return rootView;
    }

    public void setStep(String description, String thumbnail, String video){
        this.description = description;
        this.thumbnail = thumbnail;
        this.video = video;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

}
