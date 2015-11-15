package kr.edcan.knock.utils;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Copyright by 2015 Sunrin Internet High School EDCAN
 * Created by Junseok on 2015-11-02.
 */
public interface NetworkInterface {
    @POST("/login")
    @FormUrlEncoded
    void userLogin(@Field("id") String id, @Field("pw") String password, Callback<User> callback);

    @POST("/logout")
    @FormUrlEncoded
    void userLogout(@Field("id") String id, @Field("pw") String password, Callback<String> callback);

    @POST("/loginValidate")
    @FormUrlEncoded
    void loginValid(@Field("id") String id, Callback<String> callback);

    @POST("/signin")
    @FormUrlEncoded
    void registerUser(@Field("id") String id, @Field("pw") String pw, @Field("name") String name, @Field("card") String card, Callback<User> callback);

    @POST("/list")
    void listArticle(Callback<List<Article>> callback);

    @POST("/deleteconfirm")
    @FormUrlEncoded
    void killArticle(@Field("id")String id, Callback<String> callback);


}
