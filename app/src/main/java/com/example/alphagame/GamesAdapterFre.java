package com.example.alphagame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GamesAdapterFre extends RecyclerView.Adapter<GamesAdapterFre.GameViewHolder> {

    private int numberItems;
    private Context parent;

    public GamesAdapterFre(int numberOfItems, Context parent) {
        numberItems = numberOfItems;
        this.parent = parent;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.games;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        GameViewHolder viewHolder = new GameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    class GameViewHolder extends RecyclerView.ViewHolder {

        Button btnGame;

        public GameViewHolder (final View itemView) {

            super(itemView);

            btnGame = itemView.findViewById(R.id.btn_game);
            btnGame.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick (View view) {
                            if (!SecondActivity.Flag){
                                Intent intent = new Intent("com.example.alphagame.SecondActivity");
                                parent.startActivity(intent);
                                ((Activity)parent).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }

                    }
            );

        }

        void bind(int listIndex) {
            switch (listIndex)
            {
                case 0:
                    btnGame.setText("Qui est plus élevé?");
                    break;
                case 1:
                    btnGame.setText("What's taller?");
                    break;
                case 2:
                    btnGame.setText("What's harder?");
                    break;
                case 3:
                    btnGame.setText("What's hotter?");
                    break;
                case 4:
                    btnGame.setText("Мокрый или\nсухой");
                    break;
                case 5:
                    btnGame.setText("Тяжелый или\nлегкий");
                    break;
                case 6:
                    btnGame.setText("Высокий или\nнизкий");
                    break;
                case 7:
                    btnGame.setText("Веселый или\nгрустный");
                    break;
                case 8:
                    btnGame.setText("Вид сзади или\nвид спереди");
                    break;
            }
        }
    }
}
