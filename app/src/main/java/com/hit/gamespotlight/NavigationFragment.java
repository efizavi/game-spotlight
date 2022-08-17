package com.hit.gamespotlight;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NavigationFragment extends Fragment {
    private RecyclerView gameList;
    private LinearLayoutManager layoutManager;
    // TODO: remove
    private String[] allGamesPlaceholder = {"Apex Legends", "Grand Theft Auto V", "Minecraft", "Fortnite",
            "Red Dead Redemption 2", "Tetris", "Overwatch", "The Elder Scrolls V: Skyrim", "League of Legends",
            "Super Smash Bros. Ultimate", "Mario Kart 8", "Super Mario Odyssey", "World of Warcraft",
            "Grand Theft Auto: San Andreas", "Call of Duty: Modern Warfare 3", "Call of Duty: Black Ops",
            "Borderlands 2", "FIFA 18", "Sonic the Hedgehog", "The Sims 4", "Worms World Party",
            "The Witcher 3: Wild Hunt", "Elden Ring", "Divinity: Original Sin II"};
    private ArrayList<String> gamesPlaceholder = new ArrayList<>();
    private GameAdapter gameAdapter;

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);


        String[] genres = getResources().getStringArray(R.array.genre_array);
        String[] ratings = getResources().getStringArray(R.array.rating_array);
        String[] publishers = getResources().getStringArray(R.array.publisher_array);

        ArrayAdapter<String> spinnerGenreAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, genres) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                if(position == 0) {
                    view.setTextColor(Color.GRAY);
                }
                return view;
            }
        };
        ArrayAdapter<String> spinnerRatingsAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,ratings){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                if(position == 0) {
                    view.setTextColor(Color.GRAY);
                }
                return view;
            }
        };
        ArrayAdapter<String> spinnerPublishersAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,publishers){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                if(position == 0) {
                    view.setTextColor(Color.GRAY);
                }
                return view;
            }
        };

        spinnerGenreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRatingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublishersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1 = view.findViewById(R.id.spinner);
        spinner1.setAdapter(spinnerGenreAdapter);

        Spinner spinner2 = view.findViewById(R.id.spinner2);
        spinner2.setAdapter(spinnerRatingsAdapter);

        Spinner spinner3 = view.findViewById(R.id.spinner3);
        spinner3.setAdapter(spinnerPublishersAdapter);

        for (String game: allGamesPlaceholder) {
            gamesPlaceholder.add(game);
        }

        gameList = (RecyclerView) view.findViewById(R.id.gamelist);
        layoutManager = new LinearLayoutManager(view.getContext());
        gameList.setLayoutManager(layoutManager);
        gameList.setItemAnimator(new DefaultItemAnimator());

        gameList.addOnItemTouchListener(new GameItemClickListener(view.getContext(), gameList, new GameItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String game = ((TextView)((LinearLayout)view).getChildAt(0)).getText().toString();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragmentContainerView, MainFragment.newInstance(game));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // TODO?
                    }
                }));

        gameAdapter = new GameAdapter(view.getContext(), gamesPlaceholder);
        gameList.setAdapter(gameAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu, menu);

                MenuItem menuItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.setQueryHint("Search games by name...");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        gamesPlaceholder.clear();
                        gamesPlaceholder.addAll(Arrays.asList(allGamesPlaceholder));
                        for (String game : allGamesPlaceholder) {
                            if (!game.toLowerCase().contains(newText.toLowerCase())) {
                                gamesPlaceholder.remove(game);
                            }
                        }

                        gameAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());

        super.onViewCreated(view, savedInstanceState);
    }
}