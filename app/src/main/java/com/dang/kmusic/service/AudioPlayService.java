package com.dang.kmusic.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.dang.kmusic.model.AudioModel;
import com.dang.kmusic.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class AudioPlayService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    public static final String AUDIO_URL_KEY = "Url_key";

    public static final String AUDIO_KEY = "Audio";
    public static final String AUDIO_INDEX = "Index";
    public static final String AUDIO_IS_PLAYING = "isplaying";
    public static final String AUDIO_CURRENT_POSITION = "currentPosition";
    public static final String AUDIO_BROADCAST_RECEIVER = "MyBroadcast";

    public static final String AUDIO_ACTION_PLAY = "com.dang.kmusic.PLAY";
    public static final String AUDIO_ACTION_PAUSE = "com.dang.kmusic.PAUSE";
/*

    public static final int AUDIO_CODE_PLAY = 101;
    public static final int AUDIO_CODE_PAUSE = 102;
    public static final int AUDIO_CODE_RESUME = 103;
    public static final int AUDIO_CODE_STOP = 104;
*/

    private final IBinder audioBinder = new AudioBinder();

    ArrayList<AudioModel> listAudio;
    private MediaPlayer mediaPlayer;
    private int audioIndex = 0;
    private AudioModel currentAudio;
    private Intent broadcastIntent;
    long currentPosition, duration;
    Utils utils;
    Handler handler = new Handler();

    public AudioPlayService() {

    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new Utils(getApplicationContext());
        listAudio = utils.loadAudios();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }


    public void setAudio(int index) {
        this.audioIndex = index;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        audioIndex = intent.getIntExtra(AUDIO_INDEX, 0);
        switch (action) {
            case AUDIO_ACTION_PLAY: {
                playAudio(audioIndex);
            }
            break;
            case AUDIO_ACTION_PAUSE:
                pauseAudio();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    private void playAudio(int index) {
        try {
            mediaPlayer.reset();
            Uri uri = Uri.parse(listAudio.get(index).getPath());
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepareAsync();
            audioIndex = index;
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioIndex = index;
        setProgress();


    }

    private void setProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                broadcastIntent = new Intent();
                broadcastIntent.setAction(AUDIO_BROADCAST_RECEIVER);
                broadcastIntent.putExtra(AUDIO_IS_PLAYING, mediaPlayer.isPlaying());
                broadcastIntent.putExtra(AUDIO_CURRENT_POSITION, mediaPlayer.getCurrentPosition());
                sendBroadcast(broadcastIntent);

            }
        }).start();
    }

    private void nextAudio() {
        if (audioIndex < listAudio.size() - 1) {
            audioIndex++;
            playAudio(audioIndex);
        } else {
            audioIndex = 0;
            playAudio(audioIndex);
        }

    }

    private void previousAudio() {
        if (audioIndex > 0) {
            audioIndex--;
            playAudio(audioIndex);
        } else {
            audioIndex = listAudio.size() - 1;
            playAudio(audioIndex);
        }
    }

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            currentPosition = (long) mediaPlayer.getCurrentPosition();
            utils.storeDuration(audioIndex, (int) currentPosition);
            mediaPlayer.pause();

        }
    }

    private void setAudio() {
        mediaPlayer.setOnCompletionListener(this);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public long seek(long position) {
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        audioIndex++;
        if (audioIndex < listAudio.size()) {
            playAudio(audioIndex);
        } else {
            audioIndex = 0;
            playAudio(audioIndex);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        utils.storeIndex(audioIndex);
    }


    public class AudioBinder extends Binder {
        public AudioPlayService getService() {
            return AudioPlayService.this;
        }

      /*  @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case AUDIO_CODE_PLAY:
                    mediaPlayer.start();
                    break;
                case AUDIO_CODE_PAUSE:
                    mediaPlayer.pause();
                    break;
                case AUDIO_CODE_RESUME:
                    mediaPlayer.start();
                    break;
                case AUDIO_CODE_STOP:
                    mediaPlayer.stop();
                    break;

            }

            return super.onTransact(code, data, reply, flags);
        }*/
    }

    public interface CallBack {
        void onClickItem(int index);

        void updateSeekBar(long currentPosition);
    }
}
