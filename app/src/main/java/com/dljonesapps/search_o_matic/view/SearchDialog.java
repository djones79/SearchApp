package com.dljonesapps.search_o_matic.view;

import com.dljonesapps.search_o_matic.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchDialog extends Dialog implements View.OnClickListener {

  @BindView(R.id.search_view)
  SearchView searchView;

  public SearchDialog(Context context) {
    super(context);
    setContentView(R.layout.dialog_search);
    ButterKnife.bind(this);

    SearchView searchView = (SearchView)findViewById(R.id.search_view);

    Typeface face = Typeface.createFromAsset(getContext().getAssets(),
            getContext().getResources().getString(R.string.font_path_vertigo_regular));

    int id =
        searchView
            .getContext()
            .getResources()
            .getIdentifier(getContext().getResources().getString(R.string.search_src_text),
                    null, null);
    TextView searchText = (TextView) searchView.findViewById(id);
    searchText.setTypeface(face);
  }

  @Override
  public void onClick(View v) {}

}
