package com.example.movielist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.DbInteractionListener {

    public static final int ADD_REQUEST = 5;
    public static final String ADD_INTENT_KEY = "AddMovie";
    public ArrayList<Movie> WatchList;
    private ViewPager viewPager;
    private pagerAdapter pagerAdapter;

    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DbHandler(this,2);
        dbHandler.removeAll();
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new pagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                getString(R.string.movie_api_key), this);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMoviesInDB();
    }

    private void GetMoviesInDB(){
        if (WatchList!=null){
            WatchList.clear();
        }
        WatchList = dbHandler.GetAllMovies();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST){
            if (resultCode == RESULT_OK){
                Movie movie = data.getParcelableExtra(ADD_INTENT_KEY);
                if (movie != null){
                    dbHandler.AddRecord(movie);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setCurrentItem(2);
                }
            }
        }
    }

    @Override
    public boolean checkWatchList(int movieID) {
        for (Movie movie : WatchList){
            if (movie.getId().equals(movieID))
                return true;
        }
        return false;
    }

    @Override
    public ArrayList<Movie> getWatchList() {
        GetMoviesInDB();
        return WatchList;
    }
}