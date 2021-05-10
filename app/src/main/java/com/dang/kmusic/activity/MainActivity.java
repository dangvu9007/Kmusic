package com.dang.kmusic.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dang.kmusic.R;
import com.dang.kmusic.adapter.ViewPagerAdapter;
import com.dang.kmusic.fragment.ControlPlayerFragment;
import com.dang.kmusic.utils.Utils;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public static final String AUDIO_MAIN_FRAGMENT_TAG = "main_frm";
    public static final int REQUEST_WRITE_STORAGE = 1;
    Utils utils;

    FrameLayout frameLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private int lastIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new Utils(this);
        checkPermission();
        addView();
        addFragment();


    }

    private void addView() {
        frameLayout = findViewById(R.id.frm_control);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        View root = tabLayout.getChildAt(0);
        if (root instanceof ConstraintLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.design_default_color_on_secondary));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

    }

    private void addFragment() {
        lastIndex = utils.loadAudioLastIndex();
        if (lastIndex!=-1){
            frameLayout.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            ControlPlayerFragment fragment = ControlPlayerFragment.newInstance(false, lastIndex);
            fragmentManager.beginTransaction()
                    .replace(R.id.frm_control, fragment,ControlPlayerFragment.AUDIO_CONTROL_FRAGMENT_TAG)
                    .commit();
        }
    }

    private boolean hasStoragePermission() {
        int permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermission() {
        if (hasStoragePermission()) {
            utils.getAllAudios();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            utils.getAllAudios();
        }
    }

}