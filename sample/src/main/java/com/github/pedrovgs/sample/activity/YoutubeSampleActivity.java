/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pedrovgs.sample.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.github.pedrovgs.sample.R;
import com.github.pedrovgs.sample.fragment.MoviePosterFragment;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


/**
 * Sample activity created to show a video from YouTube using a YouTubePlayer.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class YoutubeSampleActivity extends FragmentActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyC1rMU-mkhoyTvBIdTnYU0dss0tU9vtK48";
    private static final String[] VIDEO_KEYS = new String[]{
            "XGik29RF4cg",
            "Ll8j-hjGkIw",
            "1hiB780Su6U",
            "Ivhhw0NoQgE",
            "GSoh3xunjhM",
            "l3rGxaoTiNI"};
    private static final String[] VIDEO_LIST = new String[]{
            "Armin van Buuren - Save My Night (Mark Sixma Remix)",
            "Paul Oakenfold - Touch Me (Rodg Chill Mix)",
            "Andrew Rayel - Find Your Harmony 2015",
            "Karol XVII & MB Valence - Sweet Honey Drops (Radio Edit)",
            "Andre Sobota - Survivor (Radio Edit)",
            "Dash Berlin & 3LAU feat. Bright Lights - Somehow (Club Mix)"};

    private static final String VIDEO_POSTER_THUMBNAIL = "http://4.bp.blogspot.com/-BT6IshdVsoA/UjfnTo_TkBI/AAAAAAAAMWk/JvDCYCoFRlQ/s1600/xmenDOFP.wobbly.1.jpg";
    private static final String VIDEO_POSTER_TITLE = "X-Men: Days of Future Past";

    private DraggablePanel draggablePanel;
    private YouTubePlayer youtubePlayer;
    private YouTubePlayerSupportFragment youtubeFragment;

    /**
     * Initialize the Activity with some injected data.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_sample);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);

        ListView listView = (ListView) findViewById(R.id.lv_videos);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, VIDEO_LIST));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                youtubePlayer.loadVideo(VIDEO_KEYS[position]);
                draggablePanel.maximize();
            }
        });

        initializeYoutubeFragment();
        initializeDraggablePanel();
        hookDraggablePanelListeners();
    }


    /**
     * Initialize the YouTubeSupportFrament attached as top fragment to the DraggablePanel widget and reproduce the
     * YouTube video represented with a YouTube url.
     */
    private void initializeYoutubeFragment() {
        youtubeFragment = new YouTubePlayerSupportFragment();
        youtubeFragment.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                        if (!wasRestored) {
                            youtubePlayer = player;
                            youtubePlayer.loadVideo(VIDEO_KEYS[0]);
                            youtubePlayer.setShowFullscreenButton(true);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                    }

                }
        );
    }

    /**
     * Initialize and configure the DraggablePanel widget with two fragments and some attributes.
     */

    private void initializeDraggablePanel() {
        draggablePanel.setFragmentManager(getSupportFragmentManager());
        draggablePanel.setTopFragment(youtubeFragment);
        MoviePosterFragment moviePosterFragment = new MoviePosterFragment();
        moviePosterFragment.setPoster(VIDEO_POSTER_THUMBNAIL);
        moviePosterFragment.setPosterTitle(VIDEO_POSTER_TITLE);
        draggablePanel.setBottomFragment(moviePosterFragment);
        draggablePanel.initializeView();
        //Picasso.with(this).load(SECOND_VIDEO_POSTER_THUMBNAIL).placeholder(R.drawable.xmen_placeholder).into(thumbnailImageView);
    }

    /**
     * Hook the DraggableListener to DraggablePanel to pause or resume the video when the DragglabePanel is maximized
     * or closed.
     */
    private void hookDraggablePanelListeners() {
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                playVideo();
            }

            @Override
            public void onMinimized() {
                //Empty
            }

            @Override
            public void onClosedToLeft() {
                pauseVideo();
            }

            @Override
            public void onClosedToRight() {
                pauseVideo();
            }
        });
    }

    /**
     * Pause the video reproduced in the YouTubePlayer.
     */
    private void pauseVideo() {
        if (youtubePlayer.isPlaying()) {
            youtubePlayer.pause();
        }
    }

    /**
     * Resume the video reproduced in the YouTubePlayer.
     */
    private void playVideo() {
        if (!youtubePlayer.isPlaying()) {
            youtubePlayer.play();
        }
    }

}
