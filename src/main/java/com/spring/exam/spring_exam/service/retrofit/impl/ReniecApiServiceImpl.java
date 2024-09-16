package com.spring.exam.spring_exam.service.retrofit.impl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.spring.exam.spring_exam.util.constans.ServiceConstant.BASE_URL_RENIEC_API_SERVICE;

public class ReniecApiServiceImpl {

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_RENIEC_API_SERVICE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
