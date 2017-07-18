package com.dljonesapps.search_o_matic.view;

import com.dljonesapps.search_o_matic.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textView;

    public ResultViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        textView = (TextView) itemView.findViewById(R.id.headline);
    }

}
