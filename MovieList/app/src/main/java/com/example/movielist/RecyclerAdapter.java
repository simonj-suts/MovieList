package com.example.movielist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.vHolder> {

    private JSONArray jsonArray;
    private ArrayList<Movie> watchList;
    private Context context;

    public DbInteractionListener dbInteractionListener;

    public interface DbInteractionListener{
        boolean checkWatchList(int movieID);
        ArrayList<Movie> getWatchList();
    }

    public RecyclerAdapter(JSONArray jsonArray, DbInteractionListener dbInteractionListener) {
        if (jsonArray==null)
            this.watchList = dbInteractionListener.getWatchList();
        else
            this.jsonArray = jsonArray;
        this.dbInteractionListener = dbInteractionListener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.re_layout,parent,false);
        context = view.getContext();
        return new vHolder(view, dbInteractionListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.vHolder holder, int position) {

        String title = "", genreList = "", imageURL = "";
        double popularity = 0.0;

        try{
            JSONObject movie = jsonArray == null ? null : jsonArray.getJSONObject(position);
            title = movie == null ? watchList.get(position).getTitle() : movie.getString("title");
            genreList = movie == null ? watchList.get(position).getGenre() :getGenres(movie.getJSONArray("genre_ids"));
            popularity = movie == null ? watchList.get(position).getPopularity() : movie.getDouble("popularity");
            imageURL = "https://image.tmdb.org/t/p/w300" + (movie == null ? watchList.get(position).getPoster_path() : movie.getString("poster_path"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        holder.itemView.setTag(position);
        holder.textView_title.setText(title);
        holder.textView_genre.setText("Genre: " + genreList);
        holder.textView_popularity.setText("Popularity: " + popularity);

        ImageLoader imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        imageLoader.get(imageURL,
                ImageLoader.getImageListener(holder.networkImageView,
                        android.R.drawable.ic_lock_lock,
                        android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(imageURL,imageLoader);
    }

    @Override
    public int getItemCount() {
        return jsonArray == null ? watchList.size() : jsonArray.length();
    }

    public class vHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final NetworkImageView networkImageView;
        private final TextView textView_title,textView_genre,textView_popularity;
        private final DbInteractionListener dbInteractionListener;

        public vHolder(@NonNull View itemView, DbInteractionListener dbInteractionListener) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.networkImageView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_genre = itemView.findViewById(R.id.textView_genre);
            textView_popularity = itemView.findViewById(R.id.textView_popularity);
            this.dbInteractionListener = dbInteractionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MainActivityDisplay.class);
            Movie movie = null;

            if (jsonArray != null){
                try {
                    JSONObject selectedMovie = jsonArray.getJSONObject((int) v.getTag());

                    movie = new Movie(selectedMovie.getInt("id"),
                            selectedMovie.getString("title"),
                            getGenres(selectedMovie.getJSONArray("genre_ids")),
                            selectedMovie.getString("poster_path"),
                            selectedMovie.getString("overview"),
                            selectedMovie.getString("backdrop_path"),
                            selectedMovie.getDouble("popularity"));

                    intent.putExtra(MainActivityDisplay.NON_CLICKABLE_KEY,
                            dbInteractionListener.checkWatchList(movie.getId()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                watchList = dbInteractionListener.getWatchList();
                movie = watchList.get((int) v.getTag());
                intent.putExtra(MainActivityDisplay.NON_CLICKABLE_KEY, true);
            }
            intent.putExtra(MainActivityDisplay.DATA_KEY, movie);
            ((MainActivity) context).startActivityForResult(intent, MainActivity.ADD_REQUEST);
        }
    }

    private String getGenres(JSONArray jsonArray){
        Genre genre = new Genre();
        return genre.getGenre(jsonArray);
    }
}
