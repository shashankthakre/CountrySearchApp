package com.oodles.countrysearchapp;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<Countries> getCountryDetail(
           @Url String url
    );



    @GET
    Call<List<Countries>> getAllCountryDetail(@Url String url);
}

