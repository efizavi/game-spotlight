package com.hit.gamespotlight;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import proto.Game;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder>{
    private LayoutInflater inflater;
    private List<GameInfo> gameList;

    public GameAdapter(Context context, List<GameInfo> gameList) {
        inflater = LayoutInflater.from(context);
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameInfo game = gameList.get(position);
        holder.gameName.setText(game.getName() + " ("+game.getReleaseYear()+")");
        holder.gameInfo.setText(""+game.getGenresNames());
        String imageURL = game.getImageUrl();


        Picasso.get()
                    .load(imageURL)
                    .placeholder(R.drawable.igdb)
                    .error(R.drawable.igdb)
                    .into(holder.gameImage);



    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}

class GameViewHolder extends RecyclerView.ViewHolder
{
    ImageView gameImage;
    TextView gameName;
    TextView gameInfo;

    public GameViewHolder(View itemView) {
        super(itemView);
        gameImage = itemView.findViewById(R.id.gameImage);
        gameName = itemView.findViewById(R.id.gameName);
        gameInfo = itemView.findViewById(R.id.gameYearAndGenre);
    }

}

