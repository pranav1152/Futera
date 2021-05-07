package com.app.tution;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Requests {

    @Multipart
    @POST("judge/")
    Call<String> uploadFile(@Part MultipartBody.Part image_file);
}
