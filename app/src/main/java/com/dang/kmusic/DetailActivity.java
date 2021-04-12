package com.dang.kmusic;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButtonPlay, imageButtonNext, imageButtonPrevious;
    ImageView imgRepeat, imgFavorite;
    ShapeableImageView imgCover;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    public void initView() {
        imageButtonNext = findViewById(R.id.imgButton_next_detail);
        imageButtonPlay=findViewById(R.id.imgButton_play_detail);
        imageButtonPrevious = findViewById(R.id.imgButton_Previous_detail);

        imgRepeat = findViewById(R.id.imgButton_repeat_detail);
        imgFavorite = findViewById(R.id.imgButton_favorite_detail);

        seekBar = findViewById(R.id.seekBar_detail);
        imgCover = findViewById(R.id.imgCover);

        imageButtonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgButton_play_detail:
                rotateDicse();
                break;
        }

    }

    private void rotateDicse() {
       RotateAnimation  mRotateUpAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setInterpolator(new LinearInterpolator());
        mRotateUpAnim.setRepeatCount(Integer.MAX_VALUE);
        mRotateUpAnim.setDuration(1200);
        mRotateUpAnim.setFillAfter(true);
        imgCover.startAnimation(mRotateUpAnim);

    }
}