package com.dljonesapps.search_o_matic.view;


import com.dljonesapps.search_o_matic.R;
import com.dljonesapps.search_o_matic.utils.CustomTypeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTypefaceTextView extends android.support.v7.widget.AppCompatTextView {

  private static final int DEFAULT_FONT = CustomTypeface.KRINKES_REGULAR.getValue();

  private CustomTypeface defaultCustomTypeface = null;

  private CustomTypeface selectedCustomTypeface = null;

  private Typeface defaultTypeface = null;

  public CustomTypefaceTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTypeface);

    int defaultCustomTypefaceValue =
        styledAttributes.getInt(R.styleable.CustomTypeface_textTypeface, DEFAULT_FONT);
    defaultCustomTypeface = CustomTypeface.getCustomTypeface(defaultCustomTypefaceValue);
    if (!isInEditMode()) {
      setTypeface(defaultCustomTypeface);
    }

    int selectedCustomTypefaceValue =
        styledAttributes.getInt(R.styleable.CustomTypeface_textTypefaceSelected, DEFAULT_FONT);
    selectedCustomTypeface = CustomTypeface.getCustomTypeface(selectedCustomTypefaceValue);

    styledAttributes.recycle();
  }

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);

    if (selected) {
      if (selectedCustomTypeface != null) {
        setTypeface(selectedCustomTypeface);
      }
    } else {
      if (defaultCustomTypeface != null) {
        setTypeface(defaultCustomTypeface);
      } else {
        setTypeface(defaultTypeface);
      }
    }
  }

  public void setTypeface(@NonNull CustomTypeface customTypeface) {
    setTypeface(customTypeface.getTypeface(getContext()));
  }
}
