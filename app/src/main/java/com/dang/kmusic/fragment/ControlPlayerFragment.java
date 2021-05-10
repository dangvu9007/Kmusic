package com.dang.kmusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dang.kmusic.R;
import com.dang.kmusic.activity.DetailActivity;
import com.dang.kmusic.model.AudioModel;
import com.dang.kmusic.service.AudioPlayService;
import com.dang.kmusic.utils.Utils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ControlPlayerFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM_DETAIL = "isDetail";
    private static final String ARG_PARAM_AUDIO = "audio";
    public static final String AUDIO_CONTROL_FRAGMENT_TAG = "control_frm";
    public static final String AUDIO_INTENT_FILTER = "intentFilter";

    private boolean isDetail, isPlaying = false;
    private int currentIndex = -1;
    private int current_position, duration;
    private AudioModel audioModel;
    private ArrayList<AudioModel> list;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    AudioPlayService audioService;
    Utils utils;
    RotateAnimation rotateUpAnim;

    TextView tvAudioTitle, tvAudioArtirst, tvAudioDuration, tvAudioTime;
    ImageButton imageButtonPlay, imageButtonNext, imageButtonPrevious;
    ImageView imgRepeat, imgFavorite, imgShowDetail;
    ShapeableImageView imgCover;
    SeekBar seekBar;

    public ControlPlayerFragment() {
        // Required empty public constructor
    }

    public static ControlPlayerFragment newInstance(boolean isDetail, int index) {
        ControlPlayerFragment fragment = new ControlPlayerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_DETAIL, isDetail);
        args.putInt(AudioPlayService.AUDIO_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }


    public void initView(View view) {

        tvAudioTitle = view.findViewById(R.id.tvTittleMusic_detail);
        tvAudioArtirst = view.findViewById(R.id.tvArtist_detail);
        tvAudioDuration = view.findViewById(R.id.tvTimeMusic_detail);
        tvAudioTime = view.findViewById(R.id.tvTittleMusic_detail);

        imageButtonNext = view.findViewById(R.id.imgButton_next_detail);
        imageButtonPlay = view.findViewById(R.id.imgButton_play_detail);
        imageButtonPrevious = view.findViewById(R.id.imgButton_Previous_detail);

        imgRepeat = view.findViewById(R.id.imgButton_repeat_detail);
        imgFavorite = view.findViewById(R.id.imgButton_favorite_detail);
        imgShowDetail = view.findViewById(R.id.imgShowDetail);

        seekBar = view.findViewById(R.id.seekBar_detail);
        imgCover = view.findViewById(R.id.imgCover);

        imageButtonPlay.setOnClickListener(this);
        imageButtonNext.setOnClickListener(this);
        imageButtonPrevious.setOnClickListener(this);
    }

    private void seekBarProgress(int position) {
        current_position = 0;
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current_position = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setDataToView(int currIndex) {
        audioModel = list.get(currIndex);
        duration = audioModel.getDuration();
        tvAudioTitle.setText(audioModel.getTitle());
        tvAudioArtirst.setText(audioModel.getArtist());
        if (!isPlaying) {
            imageButtonPlay.setBackground(getActivity().getDrawable(R.drawable.ic_baseline_pause_24));
        }

        if (duration > 1000 && isDetail) {
            seekBar.setMax(duration / 1000);
        }
        if (isDetail) {
            tvAudioDuration.setText(utils.timerConversion(duration));
        }
        if (audioModel.isIsfavorite()) {
            imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }


    }

    private void reloadView() {
        if (getActivity().getSupportFragmentManager() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_control,this,ControlPlayerFragment.AUDIO_CONTROL_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgButton_play_detail:
                playAudio();
            case R.id.imgButton_next_detail:
               nextAudio();
            case R.id.imgButton_Previous_detail:
                startService(currentIndex,AudioPlayService.AUDIO_ACTION_PREVIOUS);

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isDetail = getArguments().getBoolean(ARG_PARAM_DETAIL);
            currentIndex = getArguments().getInt(AudioPlayService.AUDIO_INDEX, 0);
            loadData();
        }
    }

    private void loadData() {
        utils = new Utils(getActivity());
        list = utils.loadAudios();
        audioService = new AudioPlayService();
        intentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(AudioPlayService.AUDIO_BROADCAST_RECEIVER);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.endsWith(AudioPlayService.AUDIO_BROADCAST_RECEIVER)) {
                    current_position = intent.getIntExtra(AudioPlayService.AUDIO_CURRENT_POSITION, -1);
                    if (isDetail) {
                        seekBar.setProgress(current_position);
                        tvAudioTime.setText(utils.timerConversion(current_position));
                    }
                }
            }
        };
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

    }

    private void playAudio() {
        if(currentIndex ==-1){
            currentIndex = utils.loadAudioLastIndex();
        }
        if (isPlaying) {
            isPlaying = true;
            imageButtonPlay.setBackground(getActivity().getDrawable(R.drawable.ic_baseline_pause_24));
            startService(currentIndex, AudioPlayService.AUDIO_ACTION_PAUSE);
        } else {
            isPlaying = false;
            imageButtonPlay.setBackground(getActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24));
            startService(currentIndex, AudioPlayService.AUDIO_ACTION_PLAY);
        }
        utils.storeIndex(currentIndex);
    }


    private void startService(int index, String action) {
        Intent intent = new Intent(getActivity(), AudioPlayService.class);
        intent.setAction(action);
        intent.putExtra(AudioPlayService.AUDIO_INDEX, index);
        getActivity().startService(intent);
    }
    private void nextAudio() {
        if (currentIndex < list.size() - 1) {
            currentIndex++;
            setDataToView(currentIndex);
            startService(currentIndex,AudioPlayService.AUDIO_ACTION_PLAY);
        } else {
            currentIndex = 0;
            setDataToView(currentIndex);
            startService(currentIndex,AudioPlayService.AUDIO_ACTION_PLAY);
        }

    }
    private void previousAudio() {
        if (currentIndex > 0) {
            currentIndex--;
            setDataToView(currentIndex);
            startService(currentIndex,AudioPlayService.AUDIO_ACTION_PLAY);
        } else {
            currentIndex = list.size() - 1;
            setDataToView(currentIndex);
            startService(currentIndex,AudioPlayService.AUDIO_ACTION_PLAY);
        }
    }

    private void showDetail() {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout = isDetail ? R.layout.fragment_control_player : R.layout.fragment_control_player_small;
        View view = inflater.inflate(layout, container, false);
        initView(view);
        setDataToView(currentIndex);

        return view;
    }


    private void rotateDicse() {
        rotateUpAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setInterpolator(new LinearInterpolator());
        rotateUpAnim.setRepeatCount(Integer.MAX_VALUE);
        rotateUpAnim.setDuration(1200);
        rotateUpAnim.setFillAfter(true);
        imgCover.startAnimation(rotateUpAnim);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);


    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}