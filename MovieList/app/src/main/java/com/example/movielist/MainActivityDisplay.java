package com.example.movielist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class MainActivityDisplay extends AppCompatActivity implements View.OnClickListener {

    public static final String DATA_KEY = "json";
    public static final String NON_CLICKABLE_KEY = "clickable";

    private TextView textView_title, textView_popularity, textView_overview;
    private ImageView imageView;
    private Button button;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

        button = findViewById(R.id.button_add);
        textView_title = findViewById(R.id.textView_title_d);
        textView_popularity = findViewById(R.id.textView_popularity_d);
        textView_overview = findViewById(R.id.textView_overview_d);
        imageView = findViewById(R.id.imageView);

        InitValues();
    }

    private void InitValues(){
        if(getIntent().hasExtra(DATA_KEY)) {
            movie = getIntent().getParcelableExtra(DATA_KEY);
            textView_title.setText(movie.getTitle());
            textView_popularity.setText(String.valueOf(movie.getPopularity()));
            textView_overview.setText(movie.getOverview());
            LoadingImages("https://image.tmdb.org/t/p/w1280"+movie.getBackdrop_path());
        }

        if (getIntent().getBooleanExtra(NON_CLICKABLE_KEY,false))
            button.setVisibility(View.GONE);
        else
            button.setOnClickListener(this);
    }

    private void LoadingImages(String url) {
        ImageLoader imageLoader = ImageSingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bmp = response.getBitmap();
                imageView.setImageBitmap(bmp);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ",error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle agrs = new Bundle();
        agrs.putParcelable(MainActivity.ADD_INTENT_KEY,movie);
        intent.putExtras(agrs);
        setResult(RESULT_OK,intent);
        finish();
    }
}