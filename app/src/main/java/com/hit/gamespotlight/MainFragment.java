package com.hit.gamespotlight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

public class MainFragment extends Fragment {

    private static final String GAME_NAME = "GAME_NAME";
    private GameInfo game;
    private IGDBController controller;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(GameInfo param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(GAME_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = (GameInfo) getArguments().getSerializable(GAME_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TextView gameNameTxt = view.findViewById(R.id.txtGameName);
        TextView gameReleaseDateTxt = view.findViewById(R.id.txtGameReleaseDate);
        TextView gamePlatformTxt = view.findViewById(R.id.txtGamePlatforms);


        TextView gameSummeryTxt = view.findViewById(R.id.txtGameSummary);
        TextView gameRatingTxt = view.findViewById(R.id.txtGameRating);
        ImageView gameImage = view.findViewById(R.id.imgGame);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        toolbar.setTitle("About Game");

        gameNameTxt.setText(game.getName());
        gameSummeryTxt.setText(game.getSummary());
        gameReleaseDateTxt.setText("Released: "+ game.getReleaseYear());
        gamePlatformTxt.setText("Platforms: " + game.getPlatformsNames());
        gameRatingTxt.setText("Rating: "+game.getRating());
        Picasso.get()
                .load(game.getImageUrl())
                .placeholder(R.drawable.igdb)
                .error(R.drawable.igdb)
                .into(gameImage);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // ((MainActivity)getActivity()).getActionBar().setTitle("Keyword Report Detail");

        controller = ((MainActivity)getActivity()).getController();
    }
}