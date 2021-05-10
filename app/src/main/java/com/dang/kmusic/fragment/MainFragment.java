package com.dang.kmusic.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dang.kmusic.R;
import com.dang.kmusic.adapter.AudioAdapter;
import com.dang.kmusic.model.AudioModel;
import com.dang.kmusic.service.AudioPlayService;
import com.dang.kmusic.utils.Utils;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private AudioAdapter adapter;
    Utils utils;
    ArrayList<AudioModel> list;
    private boolean audioBound = false;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getActivity());
        list = utils.loadAudios();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setupView(view);
        return view;
    }

    public void setupView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rcvListMusic);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AudioAdapter(new AudioPlayService.CallBack() {
            @Override
            public void onClickItem(int index) {
                onMusicClick(index);
            }

            @Override
            public void updateSeekBar(long currentPosition) {

            }
        });
        adapter.setData(list);
        recyclerView.setAdapter(adapter);

    }

    public void onMusicClick(int index) {
        reLoadFragment(index);
        startService(index);
    }

    private void reLoadFragment(int index) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = ControlPlayerFragment.newInstance(false,index);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frm_control,fragment,ControlPlayerFragment.AUDIO_CONTROL_FRAGMENT_TAG);
        transaction.commit();

    }

    private  void startService(int index) {
        Intent intent = new Intent(getActivity(), AudioPlayService.class);
        intent.setAction(AudioPlayService.AUDIO_ACTION_PLAY);
        intent.putExtra(AudioPlayService.AUDIO_INDEX, index);
        getActivity().startService(intent);
    }

   /* public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayService.AudioBinder binder = (AudioPlayService.AudioBinder) service;
            AudioPlayService audioService = binder.getService();
            audioBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioBound = false;
        }
    };*/

}