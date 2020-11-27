package com.example.movielist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class pagerAdapter extends FragmentStatePagerAdapter {

    private final String mNowPlayingURL,mUpComingURL;
    private final String[] tabtiles = {"NOW SHOWING","UPCOMING MOVIES","WATCH LIST"};
    private final RecyclerAdapter.DbInteractionListener dbInteractionListener;

    public pagerAdapter(@NonNull FragmentManager fm,
                        int behavior,
                        String apiKey,
                        RecyclerAdapter.DbInteractionListener dbInteractionListener) {
        super(fm, behavior);
        mNowPlayingURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + apiKey;
        mUpComingURL = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + apiKey;
        this.dbInteractionListener = dbInteractionListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        MovieFragment movieFragment = null;
        switch (position){
            case 0:
                movieFragment = MovieFragment.newInstance(mNowPlayingURL);
                break;
            case 1:
                movieFragment = MovieFragment.newInstance(mUpComingURL);
                break;
            case 2:
                movieFragment = MovieFragment.newInstance("null");
                break;
        }
        if (movieFragment != null){
            movieFragment.SetDbInteractionListener(dbInteractionListener);
        }
        return movieFragment;
    }

    @Override
    public int getCount() {
        return tabtiles.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtiles[position];
    }
}
