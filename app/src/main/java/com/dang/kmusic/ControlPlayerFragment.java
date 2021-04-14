package com.dang.kmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import model.AudioModel;

public class ControlPlayerFragment extends Fragment {

    private static final String ARG_PARAM_DETAIL = "isDetail";
    private static final String ARG_PARAM_AUDIO = "audio";


    private boolean isDetail;
    private AudioModel audioModel;

    public ControlPlayerFragment() {
        // Required empty public constructor
    }

    public static ControlPlayerFragment newInstance(boolean isDetail, AudioModel audio) {
        ControlPlayerFragment fragment = new ControlPlayerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_DETAIL, isDetail);
        args.putSerializable(ARG_PARAM_AUDIO, audio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isDetail = getArguments().getBoolean(ARG_PARAM_DETAIL);
            audioModel = (AudioModel) getArguments().getSerializable(ARG_PARAM_AUDIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout = isDetail?R.layout.fragment_control_player:R.layout.fragment_control_player_small;
        return inflater.inflate(layout, container, false);
    }
}