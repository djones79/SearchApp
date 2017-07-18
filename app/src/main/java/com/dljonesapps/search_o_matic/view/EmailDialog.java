package com.dljonesapps.search_o_matic.view;

import com.dljonesapps.search_o_matic.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class EmailDialog extends Dialog implements View.OnClickListener {

    public EmailDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_email);
    }

    @Override
    public void onClick(View v) {

    }
}
