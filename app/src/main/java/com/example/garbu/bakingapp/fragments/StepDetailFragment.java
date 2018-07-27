package com.example.garbu.bakingapp.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This fragment shows the detailed instructions for the selected step and the video or image.
 */
public class StepDetailFragment extends Fragment {

    Unbinder unbinder;
    SimpleExoPlayer mPlayer;
    DataSource.Factory dataSourceFactory;
    @BindView(R.id.step_image)
    ImageView stepImage;
    @BindView(R.id.step_details)
    TextView stepDetails;
    @BindView(R.id.recipe_video)
    PlayerView playerView;
    private Step step;
    private long mPlayerPosition;
    private boolean mPlayWhenReady;
    private Context mContext;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Step step, Context context) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(context.getString(R.string.step_key), step);
        stepDetailFragment.setArguments(args);
        return stepDetailFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mContext = getActivity().getApplicationContext();
        unbinder = ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(mContext.getString(R.string.step_key));

            mPlayerPosition = savedInstanceState.getLong(getString(R.string.video_position));
            mPlayWhenReady = savedInstanceState.getBoolean(getString(R.string.player_state));
        } else {
            step = getArguments().getParcelable(mContext.getString(R.string.step_key));
            mPlayWhenReady = true;

        }

        stepDetails.setText(step.getDescription());

        if (step.getVideoURL().equals("")) {
            stepImage.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            if (!step.getThumbnailURL().equals("")) {
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .placeholder(R.drawable.ic_landscape_grey_24dp)
                        .error(R.drawable.content_error)
                        .into(stepImage);
            } else {
                stepImage.setImageResource(R.drawable.no_content);
            }


        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayerPosition = mPlayer.getCurrentPosition();
            releasePlayer();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupPlayer();
        loadVideo();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //release video mPlayer
        releasePlayer();

        //unbind the view
        unbinder.unbind();
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void setupPlayer() {

        if (mPlayer == null) {
            //Referenced: http://google.github.io/ExoPlayer/guide.html for ExoPlayer
            // 1. Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the mPlayer
            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            playerView.setPlayer(mPlayer);
            // Produces DataSource instances through which media data is loaded.
            dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()).toString()));
        }
    }

    private void loadVideo() {

        //set video source
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(step.getVideoURL()));
        // Prepare the mPlayer with the source.
        mPlayer.prepare(videoSource);
        //check for saved position and set it
        mPlayer.seekTo(mPlayerPosition);
        mPlayer.setPlayWhenReady(mPlayWhenReady);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.step_key), step);
        outState.putBoolean(getString(R.string.player_state), mPlayWhenReady);
        outState.putLong(getString(R.string.video_position), mPlayerPosition);

    }

}
