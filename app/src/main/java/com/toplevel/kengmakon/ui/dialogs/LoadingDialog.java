package com.toplevel.kengmakon.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.DialogLoadingBinding;
import com.toplevel.kengmakon.ui.ForgotPasswordActivity;

public class LoadingDialog extends FrameLayout {
    DialogLoadingBinding binding;
    Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_loading, this, true);
        String videoPath = "android.resource://" + context.getPackageName() + "/" + R.raw.loading_video;
        Uri uri = Uri.parse(videoPath);
        binding.videoView.setVideoURI(uri);
        binding.videoView.start();



        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

    }


}
