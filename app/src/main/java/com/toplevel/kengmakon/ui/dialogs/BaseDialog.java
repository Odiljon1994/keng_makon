package com.toplevel.kengmakon.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.DialogBaseBinding;

public class BaseDialog extends FrameLayout {
    DialogBaseBinding binding;
    Context context;
    ClickListener clickListener;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_base, this, true);

        binding.okBtn.setOnClickListener(view -> clickListener.onClickOk());
        binding.noBtn.setOnClickListener(view -> clickListener.onClickNo());

    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public void setTitle(String title, String body) {
        binding.title.setText(title);
        binding.body.setText(body);
    }
    public void setBtnTitle(String okBtnTitle, String noBtnTitle) {
        binding.okBtn.setText(okBtnTitle);
        binding.noBtn.setText(noBtnTitle);
    }

    public void makeBtnInvisible() {
        binding.noBtn.setVisibility(GONE);
    }
    public void changeBtnText(String noBtn, String okBtn) {
        binding.noBtn.setText(noBtn);
        binding.okBtn.setText(okBtn);
    }

    public interface ClickListener {
        default void onClickOk() {

        }

        default void onClickNo() {

        }
    }
}
