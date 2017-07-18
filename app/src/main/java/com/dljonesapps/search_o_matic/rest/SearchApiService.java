package com.dljonesapps.search_o_matic.rest;

import com.dljonesapps.search_o_matic.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApiService {

  @GET("articlesearch.json")
  Call<ApiResponse> getResponse(
          @Query("api_key") String apiKey,
          @Query("q") String searchParam
  );
}
