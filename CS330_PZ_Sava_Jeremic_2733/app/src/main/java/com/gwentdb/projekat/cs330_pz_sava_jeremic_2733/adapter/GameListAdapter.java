package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.model.Game;

import java.util.ArrayList;

public class GameListAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<Game> gameList;

    public GameListAdapter(Context context, int layout, ArrayList<Game> gameList) {
        this.context = context;
        this.layout = layout;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int i) {
        return gameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtGenre, txtDescription, txtCompany, txtPrice;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtGenre = row.findViewById(R.id.txtGenre);
            holder.txtDescription = row.findViewById(R.id.txtDescription);
            holder.txtCompany = row.findViewById(R.id.txtCompany);
            holder.txtPrice = row.findViewById(R.id.txtPrice);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Game game = gameList.get(i);

        holder.txtName.setText(game.getName());
        holder.txtGenre.setText(game.getGenre());
        holder.txtDescription.setText(game.getDescription());
        holder.txtCompany.setText(game.getCompany());
        holder.txtPrice.setText(String.valueOf(game.getPrice()));

        byte[] gameImg = game.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(gameImg, 0, gameImg.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

}
