package com.dang.kmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dang.kmusic.R;
import com.dang.kmusic.model.AudioModel;
import com.dang.kmusic.service.AudioPlayService;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MusicViewHolder> {

    List<AudioModel> data = new ArrayList<>();
    AudioPlayService.CallBack callBack;
    private AudioAdapter adapter;

    public AudioAdapter(AudioPlayService.CallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(List<AudioModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public AudioAdapter.MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

        AudioModel audio = data.get(position);

        View view = holder.getView();
        TextView tvtitle = (TextView) view.findViewById(R.id.tvTittleMusic_list);
        TextView tvArtist = (TextView) view.findViewById(R.id.tvArtist_list);

        if (audio != null) {
            tvtitle.setText(audio.getTitle());
            tvArtist.setText(audio.getArtist());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClickItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MusicViewHolder extends RecyclerView.ViewHolder {
        private View view;


        public MusicViewHolder(View Itemview) {
            super(Itemview);
            view = Itemview;

        }

        View getView() {
            return view;
        }
    }

}

