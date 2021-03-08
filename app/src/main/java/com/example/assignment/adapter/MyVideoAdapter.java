package com.example.assignment.adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;
import com.example.assignment.databinding.ListItemsBinding;
import com.example.assignment.model.VideoDetails;
import com.example.assignment.util.ShareUtils;
import com.example.assignment.viewmodel.DataItemViewModel;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.List;



public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.DataViewHolder> {

    private List<VideoDetails> data;
    Context mContext;

    public MyVideoAdapter(Context nContext) {
        this.mContext = nContext;
        this.data = new ArrayList<>();
        this.mContext = nContext;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, new FrameLayout(parent.getContext()), false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        VideoDetails dataModel = data.get(position);
        holder.setViewModel(new DataItemViewModel(dataModel));
        Log.d("Data", "onBindViewHolder: " + dataModel.getName());
        dataModel.setSelected(true);
        holder.setExoplayer(data.get(position).getVideo_path());

    }



    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public void onViewRecycled(@NonNull DataViewHolder holder) {
        super.onViewRecycled(holder);
        holder.startPlayer();
    }


    @Override
    public void onViewAttachedToWindow(DataViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
        holder.startPlayer();
    }

    @Override
    public void onViewDetachedFromWindow(DataViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
        holder.pausePlayer();
        holder.releasePlayer();
    }

    public void updateData(@Nullable List<VideoDetails> data) {
        if (data == null || data.isEmpty()) {
            this.data.clear();
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        ListItemsBinding binding;
        PlayerView playerView;
        private long mWhenPauseTime = 0L;
        public SimpleExoPlayer player;
        public void startPlayer() {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
        public void pausePlayer() {

            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
        public void setExoplayer(String url) {
            player = new SimpleExoPlayer.Builder(itemView.getContext()).build();
            player.setRepeatMode(player.REPEAT_MODE_ONE);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(itemView.getContext(), Util.getUserAgent(itemView.getContext(), "com.example.assignment"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(ShareUtils.getUri(itemView.getContext(), url));
            player.prepare(videoSource);
            playerView.setPlayer(player);
            player.seekTo(mWhenPauseTime);
        }
        public void releasePlayer() {
            player.setPlayWhenReady(false);
            player.release();
            player.stop(true);
        }
        DataViewHolder(View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.exoplayerView);
            bind();
        }
        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }
        void unbind() {
            if (binding != null) {
                binding.unbind();
            }
        }
        void setViewModel(DataItemViewModel viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }
    }
}
