package com.hit.gamespotlight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import proto.Game;
import proto.Genre;

public class NavigationFragment extends Fragment {
    private RecyclerView gamesRecyclerView;
    private LinearLayoutManager layoutManager;
    private IGDBController controller;

    private GameAdapter gameAdapter;

    private ArrayList<GameInfo> gamesList = new ArrayList<>();
    private ArrayList<GameInfo> filteredGamesList = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<String> genresNames = new ArrayList<>();

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

        genresNames.add("All");

        // String[] genres = getResources().getStringArray(R.array.genre_array);
        String[] ratings = getResources().getStringArray(R.array.rating_array);
        String[] years = getResources().getStringArray(R.array.publisher_array);

        ArrayAdapter<String> spinnerGenreAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, genresNames) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        ArrayAdapter<String> spinnerRatingsAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,ratings){
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        ArrayAdapter<String> spinnerPublishersAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,years){
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
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

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filteredGamesList.clear();
                filteredGamesList.addAll(gamesList);
                if (i == 0) {
                    // do nothing
                } else {
                    String name = genresNames.get(i);
                    filteredGamesList.removeIf((gameInfo) -> !gameInfo.hasGenre(name));
                }
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filteredGamesList.clear();
                filteredGamesList.addAll(gamesList);
                if (i == 0) {
                    // do nothing
                } else {
                    int minRate = Integer.parseInt(ratings[i]);
                    filteredGamesList.removeIf((gameInfo) -> gameInfo.getRating() < minRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filteredGamesList.clear();
                filteredGamesList.addAll(gamesList);
                if (i == 0) {
                    // do nothing
                } else {
                    String yearsString = years[i];
                    int fromYear = Integer.parseInt(yearsString.split("-")[0]);
                    int toYear = Integer.parseInt(yearsString.split("-")[1]);
                    filteredGamesList.removeIf((gameInfo) -> gameInfo.getReleaseYear() < fromYear || gameInfo.getReleaseYear() > toYear);
                }
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gamesRecyclerView = view.findViewById(R.id.gamelist);
        layoutManager = new LinearLayoutManager(view.getContext());
        gamesRecyclerView.setLayoutManager(layoutManager);
        gamesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        gamesRecyclerView.addOnItemTouchListener(new GameItemClickListener(view.getContext(), gamesRecyclerView, new GameItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        GameInfo gameInfo = filteredGamesList.get(position);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragmentContainerView, MainFragment.newInstance(gameInfo));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // TODO?
                    }
                }));

        gameAdapter = new GameAdapter(view.getContext(), filteredGamesList);
        gamesRecyclerView.setAdapter(gameAdapter);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Toolbar toolbar = view.findViewById(R.id.toolbar);

//        Menu menu = toolbar.getMenu();//requireActivity();
//        toolbar.addMenuProvider();

        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu, menu);

                MenuItem menuItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.setQueryHint("Search games by name...");
                searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        if(!focused){
                            filteredGamesList.clear();
                            filteredGamesList.addAll(gamesList);
                            gameAdapter.notifyDataSetChanged();
                        }
                    }
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        filteredGamesList.clear();
                       // List<Game> games = controller.getGameList(query);
                        for (GameInfo gameInfo : gamesList) {
                            if (gameInfo.getName().toLowerCase().contains(query.toLowerCase())
                            ){
                                filteredGamesList.add(gameInfo);
                            }

                        }
                      //  Collections.sort(gamesPlaceholder);
                        gameAdapter.notifyDataSetChanged();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
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

        controller = ((MainActivity)getActivity()).getController();
        List<Game> games = controller.getGameList();
        Log.i("game count", String.valueOf(games.size()));
         genres = controller.getGenreList();
        for (Genre g: genres) {
            genresNames.add(g.getName());
            Log.i("GENRE: ", g.getName());
        }
        filteredGamesList.addAll(gamesList);

        gamesList.clear();
        for (Game game : games) {
            GameInfo gameInfo = new GameInfo(
                    game.getName(),
                    game.getSummary(),
                    game.getCover().getUrl(),
                    game.getFirstReleaseDate(),
                    game.getGenresList(),
                    (int) game.getTotalRating(),
                    game.getPlatformsList()
            );
            gamesList.add(gameInfo);
        }
        filteredGamesList.addAll(gamesList);

        //Collections.sort(gamesPlaceholder);
        gameAdapter.notifyDataSetChanged();
    }
}