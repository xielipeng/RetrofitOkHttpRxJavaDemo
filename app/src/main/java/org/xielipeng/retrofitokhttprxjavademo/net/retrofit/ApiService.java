package org.xielipeng.retrofitokhttprxjavademo.net.retrofit;


import org.xielipeng.retrofitokhttprxjavademo.modle.LoginResult;
import org.xielipeng.retrofitokhttprxjavademo.net.model.BASEResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 接口定义
 *
 * @author xielipeng 2016-9-23
 */
public interface ApiService {

    /**
     * 登陆
     */
    @POST("apiPublic/userLogin")
    Observable<BASEResult<LoginResult>> login(@QueryMap Map<String, String> map);

    /**
     * 文件上传
     *
     * @return
     */
    @Multipart
    @POST("apiPublic/avatarStore")
    Call<BASEResult> uploadFile(@QueryMap Map<String, String> map,
                                @Part("images") RequestBody pictureName, @Part MultipartBody.Part picture);

}
