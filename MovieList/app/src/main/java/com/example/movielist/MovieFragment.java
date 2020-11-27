package com.example.movielist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MovieFragment extends Fragment {

    private static final String ARG_URL = "url";
    private RecyclerView recyclerView;
    private String mUrl;
    private RecyclerAdapter.DbInteractionListener dbInteractionListener;

    public MovieFragment() {}

    public static MovieFragment newInstance(String url) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
            if (!mUrl.equals("null")) RetrieveMovieList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        if (mUrl.equals("null")){
            RecyclerAdapter reAdapter = new RecyclerAdapter(null,dbInteractionListener);
            recyclerView.setAdapter(reAdapter);
        }
        return view;
    }

    public void SetDbInteractionListener(RecyclerAdapter.DbInteractionListener dbInteractionListener){
        this.dbInteractionListener = dbInteractionListener;
    }

    private void RetrieveMovieList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,mUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    RecyclerAdapter reAdapter = new RecyclerAdapter(response.getJSONArray("results"),dbInteractionListener);
                    recyclerView.setAdapter(reAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }
}