package com.reflect.reflect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reflect.reflect.R;
import com.reflect.reflect.model.Artist;
import com.reflect.reflect.model.Item;

import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;

    public CustomAutoCompleteAdapter(Context context, List<Item> songs) {
        super(context, 0, songs);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lyt_song, parent, false);
        }

        Item song = getItem(position);

        convertView.setVisibility(View.VISIBLE);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        ImageView imageCross = convertView.findViewById(R.id.imgCross);
        imageView.setVisibility(View.GONE);
        imageCross.setVisibility(View.GONE);
        TextView txtSongName = convertView.findViewById(R.id.txtSongName);
        TextView txtSongArtist = convertView.findViewById(R.id.txtSongArtist);

        if (song != null) {
            // Set your data to views here
//            imageView.setImageResource(song.getImageResource());
            txtSongName.setText(song.getName());
            txtSongArtist.setText(getArtists(song).toString());
        }

        return convertView;
    }

    @NonNull
    private StringBuilder getArtists(Item item) {
        StringBuilder artists= new StringBuilder();
        if (item.getArtists()!=null)
        {
            for (Artist artist : item.getArtists())
            {
                if(artists.length() == 0)
                {
                    artists = new StringBuilder(artist.getName());
                }
                else
                    artists.append(",").append(artist.getName());

            }
        }
        return artists;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
