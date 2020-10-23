package com.example.apphorarios.RetroFit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IMyService {

    @POST ("auth/signup")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("nomEmp") String nomEmp,
                                    @Field("apeEmp") String apeEmp,
                                    @Field("username") String username,
                                    @Field("pass") String pass,
                                    @Field("confirm") String confirm,
                                    @Field("email") String email,
                                    @Field("tokenFCM") String tokenFCM);

    @POST ("auth/login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("username") String username,
                                 @Field("pass") String pass);

    @POST ("registros")
    @FormUrlEncoded
    Observable<String> registerData(@Header("Authorization") String token,
                                    @Field("horaEntrada") String horaEntrada,
                                    @Field("horaSalida") String horaSalida,
                                    @Field("user") String user,
                                    @Field("descripcion") String descripcion);

    @PUT("registros/{id}")
    @FormUrlEncoded
    Observable<String> updateData(@Header("Authorization") String token,
                                    @Path(value = "id", encoded = false) String id,
                                    @Field("horaEntrada") String horaEntrada,
                                    @Field("horaSalida") String horaSalida,
                                    @Field("user") String user,
                                    @Field("descripcion") String descripcion);

    @GET("empleados/username/{username}")
    Observable<String> getUser(@Header("Authorization") String token,
                               @Path(value = "username", encoded = false) String username);

    @GET("registros/week/{id}")
    Observable<String> getWeek(@Header("Authorization") String token,
                               @Path(value = "id", encoded = false) String id);

    @GET("registros/date/{id}")
    Observable<String> getByDate(@Header("Authorization") String token,
                               @Path(value = "id", encoded = false) String id);
}
