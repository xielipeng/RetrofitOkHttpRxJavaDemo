package org.xielipeng.retrofitokhttprxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xielipeng.retrofitokhttprxjavademo.modle.LoginResult;
import org.xielipeng.retrofitokhttprxjavademo.net.helper.RequestParams;
import org.xielipeng.retrofitokhttprxjavademo.net.model.BASEResult;
import org.xielipeng.retrofitokhttprxjavademo.net.retrofit.Api;
import org.xielipeng.retrofitokhttprxjavademo.net.retrofit.ApiService;
import org.xielipeng.retrofitokhttprxjavademo.net.retrofit.RetrofitClient;
import org.xielipeng.retrofitokhttprxjavademo.net.rxjava.RxSubscribe;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login("admin", "123456");
    }

    private void login(String userName, String passWord) {
        Api.getInstance().login(userName, passWord, new RxSubscribe<LoginResult>(this) {
            @Override
            public void _onNext(LoginResult loginResult) {
                // loginResult 是预先设定你希望返回的类型，直接操作即可
            }

            @Override
            public void _onError(String message) {

            }
        });
    }

    private void uploadFile(int uid, String filePath) {
        ApiService apiService = RetrofitClient.getInstance().retrofit().create(ApiService.class);
        RequestParams params = Api.getInstance().getParams();
        params.add("uid", uid);

        RequestBody pictureNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), "pictureName");
        File picture = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), picture);
        MultipartBody.Part picturePart = MultipartBody.Part.createFormData("images", picture.getName(), requestFile);

        Call<BASEResult> call = apiService.uploadFile(params.get(), pictureNameBody, picturePart);
        call.enqueue(new Callback<BASEResult>() {
            @Override
            public void onResponse(Call<BASEResult> call, Response<BASEResult> response) {
                if (response.isSuccessful()) {
                    ToastUtils.show(getApplicationContext(), response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<BASEResult> call, Throwable t) {

            }
        });

    }


}
