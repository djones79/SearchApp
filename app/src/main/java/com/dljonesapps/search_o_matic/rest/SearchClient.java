package com.dljonesapps.search_o_matic.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchClient {

  private static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
  private static Retrofit retrofit;

  public static Retrofit getClient() {
    if (retrofit == null) {
      retrofit =
          new Retrofit.Builder()
              .addConverterFactory(GsonConverterFactory.create())
              .baseUrl(BASE_URL)
              .build();
    }
    return retrofit;
  }
}
