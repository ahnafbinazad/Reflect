package com.reflect.reflect.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.reflect.reflect.databinding.ItemHistoryBinding;
import com.reflect.reflect.model.MyDay;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyDayAdapter extends RecyclerView.Adapter<MyDayAdapter.ViewHolder> {

    public void setMyDayList(List<MyDay> myDayList) {
        this.myDayList = myDayList;
        notifyDataSetChanged();
    }

    private List<MyDay> myDayList=new ArrayList<>();
    private static String DATE_PATTERN = "d' 'MMMM', 'yyyy";



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDay myDay = myDayList.get(position);
        holder.bind(myDay);
    }

    @Override
    public int getItemCount() {
        return myDayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemHistoryBinding binding;

        public ViewHolder(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MyDay myDay) {
         binding.txtFeelingLikeEmoji.setText(myDay.getEmoji());
         binding.txtGrateFulfor.setText(myDay.getGrateFulForToday());
         binding.txtJournal.setText(myDay.getJournalToday());

         if (!myDay.getSpotifySongName().isEmpty())
         {
             binding.songLyt.songLyt.setVisibility(View.VISIBLE);
             binding.songLyt.imgCross.setVisibility(View.GONE);
             binding.songLyt.txtSongArtist.setText(myDay.getSpotifySongArtist());
             binding.songLyt.txtSongName.setText(myDay.getSpotifySongName());
             if(!myDay.getSpotifySongCover().isEmpty())
                Picasso.get().load(myDay.getSpotifySongCover()).into(binding.songLyt.imageView);
         }
         else {
             binding.songLyt.songLyt.setVisibility(View.GONE);
         }
        }
    }
}
