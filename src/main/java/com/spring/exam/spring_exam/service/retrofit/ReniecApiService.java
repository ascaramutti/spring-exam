package com.spring.exam.spring_exam.service.retrofit;

import com.spring.exam.spring_exam.aggregates.response.ReniecApiServiceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ReniecApiService {

    @GET("/v2/reniec/dni")
    Call<ReniecApiServiceResponse> getPersonaReniec(@Header("Authorization") String token,
                                                    @Query("numero") String numero);
}
