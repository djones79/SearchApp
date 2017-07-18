package com.dljonesapps.search_o_matic.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.dljonesapps.search_o_matic.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.SparseArray;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

// This class interacts with and depends on attrs.xml::CustomTypeface. {mValue} must match the value in the XML.
public enum CustomTypeface {
  KRINKES_DECOR(0x11, R.raw.krinkes_decor),
  KRINKES_REGULAR(0x12, R.raw.krinkes_regular),
  VERTIGO_BOLD(0x13, R.raw.vertigo_bold),
  VERTIGO_REGULAR(0x14, R.raw.vertigo_regular);

  private static final String TAG = CustomTypeface.class.getSimpleName();

  private static final SparseArray<Typeface> TYPEFACES = new SparseArray<>();

  private static final BiMap<Integer, CustomTypeface> VALUE_MAP = HashBiMap.create();

  static {
    for (CustomTypeface customTypeface : CustomTypeface.values()) {
      VALUE_MAP.put(customTypeface.getValue(), customTypeface);
    }
  }

  private int mValue;
  private int mResource;

  CustomTypeface(int value, int resource) {
    mValue = value;
    mResource = resource;
  }

  public int getValue() {
    return mValue;
  }

  public int getResource() {
    return mResource;
  }

  public Typeface getTypeface(Context context) {
    if (TYPEFACES.indexOfKey(mResource) >= 0) {
      return TYPEFACES.get(mResource);
    }

    Typeface typeface;
    InputStream inputStream;
    try {
      inputStream = context.getResources().openRawResource(mResource);
    } catch (Resources.NotFoundException e) {
      Timber.e(TAG, context.getResources().getString(R.string.no_font), name());
      throw new RuntimeException(e);
    }

    String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

    try {
      byte[] buffer = new byte[inputStream.available()];
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

      int l;
      while ((l = inputStream.read(buffer)) > 0) {
        bos.write(buffer, 0, l);
      }

      bos.close();

      typeface = Typeface.createFromFile(outPath);
      TYPEFACES.append(mResource, typeface);

      // clean up
      new File(outPath).delete();
    } catch (IOException e) {
      Timber.e(TAG, context.getResources().getString(R.string.error_reading_font), name());
      return null;
    }

    Timber.v(TAG, context.getResources().getString(R.string.loaded_font));
    return typeface;
  }

  public static CustomTypeface getCustomTypeface(int value) {
    return VALUE_MAP.get(value);
  }
}
