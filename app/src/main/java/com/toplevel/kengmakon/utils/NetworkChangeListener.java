package com.toplevel.kengmakon.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import com.toplevel.kengmakon.ui.dialogs.BaseDialog;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) {

            BaseDialog baseDialog = new BaseDialog(context);
            baseDialog.makeBtnInvisible();
            baseDialog.changeBtnText("", "Ok");
            baseDialog.setTitle("Internet o'chib qoldi", "Iltimos intertingizni ulang.", "");
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setView(baseDialog);
            AlertDialog dialog = alertBuilder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
                @Override
                public void onClickOk() {
                    BaseDialog.ClickListener.super.onClickOk();
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            };
            baseDialog.setClickListener(clickListener);



//            new AlertDialog.Builder(context)
//                    .setTitle("인터넷 연결을 확인하세요")
//                 //   .setMessage("Are you sure you want to delete this entry?")
//
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            onReceive(context, intent);
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                   // .setNegativeButton(android.R.string.no, null)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setCancelable(false)
//                    .show();
        }
    }
}
