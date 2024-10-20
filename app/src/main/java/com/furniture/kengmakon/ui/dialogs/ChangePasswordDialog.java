package com.furniture.kengmakon.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.databinding.DialogChangePasswordBinding;

public class ChangePasswordDialog extends FrameLayout {

    DialogChangePasswordBinding binding;
    Context context;
    ClickListener clickListener;

    public ChangePasswordDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_change_password, this, true);

        binding.changePasswordBtn.setOnClickListener(view -> clickListener.onClickChangePasswordBtn(binding.oldPassword.getText().toString(),
                binding.newPassword.getText().toString(),
                binding.confirmPassword.getText().toString()));

    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setError(String error) {
        binding.error.setText(error);
    }



    public interface ClickListener {
        default void onClickChangePasswordBtn(String oldPassword, String newPassword, String confirmPassword) {

        }
    }
}
