package com.hit.gamespotlight;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder>{
    private LayoutInflater inflater;
    // TODO: Implement with proper game object
    private List<String> gamesPlaceholder;

    public GameAdapter(Context context, List<String> gamesPlaceholder) {
        inflater = LayoutInflater.from(context);
        this.gamesPlaceholder = gamesPlaceholder;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        holder.gameName.setText(gamesPlaceholder.get(position));
    }

    @Override
    public int getItemCount() {
        return gamesPlaceholder.size();
    }
}

class GameViewHolder extends RecyclerView.ViewHolder
{
    TextView gameName;

    public GameViewHolder(View itemView) {
        super(itemView);
        gameName = (TextView) itemView.findViewById(R.id.gameName);
    }

}

