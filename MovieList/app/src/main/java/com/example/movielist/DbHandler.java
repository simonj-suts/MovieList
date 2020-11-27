package com.example.movielist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MovieDB";
    private static final String TABLE_NAME = "MovieTable";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "posterpath";
    private static final String KEY_BACKDROP_PATH = "backdroppath";
    private static final String KEY_OVERVIEW = "overview";

    public DbHandler(@Nullable Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("+
                KEY_ID + " INTEGER, " +
                KEY_TITLE + " TEXT, " +
                KEY_GENRE + " TEXT, " +
                KEY_POPULARITY + " DOUBLE, " +
                KEY_BACKDROP_PATH + " TEXT, " +
                KEY_OVERVIEW + " TEXT, " +
                KEY_POSTER_PATH + " TEXT)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long AddRecord(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, movie.getId());
        contentValues.put(KEY_TITLE, movie.getTitle());
        contentValues.put(KEY_GENRE, movie.getGenre());
        contentValues.put(KEY_POPULARITY, movie.getPopularity());
        contentValues.put(KEY_POSTER_PATH, movie.getPoster_path());
        contentValues.put(KEY_BACKDROP_PATH, movie.getBackdrop_path());
        contentValues.put(KEY_OVERVIEW, movie.getOverview());

        return db.insert(TABLE_NAME,null,contentValues);
    }

    public ArrayList<Movie> GetAllMovies(){
        ArrayList<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                Double popularity = cursor.getDouble(3);
                String backdrop_path = cursor.getString(4);
                String overview = cursor.getString(5);
                String poster_path = cursor.getString(6);
                movies.add(new Movie(id,title,genre,poster_path,overview,backdrop_path,popularity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movies;
    }

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }
}
