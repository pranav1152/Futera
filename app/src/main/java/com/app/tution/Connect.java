package com.app.tution;

import android.content.Context;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connect {

    Requests requests;
    Retrofit retrofit;
    Context context;

    public Connect(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.165:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        requests = retrofit.create(Requests.class);
    }


    public void judge(String filename, String data) {
        MultipartBody.Part body = uploadBody(data, filename);

        Call<String> call = requests.uploadFile(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code() == 200) {

                } else {
                    Toast.makeText(context, "Error :(" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private MultipartBody.Part uploadBody(String data, String filename) {

        byte[] byteArray = data.getBytes();


        RequestBody reqFile = RequestBody.create(MediaType.parse("text/*"), byteArray);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", filename, reqFile);

        return body;
    }
}
