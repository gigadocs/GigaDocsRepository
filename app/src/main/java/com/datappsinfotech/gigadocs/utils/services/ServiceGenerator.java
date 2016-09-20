package com.datappsinfotech.gigadocs.utils.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heman on 9/8/2016.
 */
public class ServiceGenerator {
    public static String BASE_URL = "http://www.gigadocs.com/giga_api/";
    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
}
