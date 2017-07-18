package com.dljonesapps.search_o_matic.utils;

import com.dljonesapps.search_o_matic.R;
import com.dljonesapps.search_o_matic.model.Doc;
import com.dljonesapps.search_o_matic.view.ResultViewHolder;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ResultViewHolder> {

  private List<Doc> docs;
  private LayoutInflater inflater;
  private Context context;

  public SearchAdapter(Context context) {
    this.context = context;
    inflater = LayoutInflater.from(context);
    docs = new ArrayList<>();
  }

  @Override
  public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = inflater.inflate(R.layout.item_result, parent, false);

    return new ResultViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ResultViewHolder holder, int position) {

    Doc doc = docs.get(position);

    if (doc.getMultimedia() != null && doc.getMultimedia().size() > 0) {
      String imageUrl = doc.getMultimedia().get(1).getUrl();

      if (!TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context)
            .load(imageUrl)
            .placeholder(R.color.colorPrimary)
            .into(holder.imageView);
      }
    }

    holder.textView.setText(doc.getHeadline().getPrintHeadline());
  }

  @Override
  public int getItemCount() {
    return (docs == null) ? 0 : docs.size();
  }

  public void setDocsList(List<Doc> docsList) {
    this.docs.clear();
    this.docs.addAll(docsList);
    notifyDataSetChanged();
  }

  public List<Doc> getResponseList() {
    return docs;
  }
}
