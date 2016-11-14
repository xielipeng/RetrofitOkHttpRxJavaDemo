package org.xielipeng.retrofitokhttprxjavademo.net.retrofit;

import org.xielipeng.retrofitokhttprxjavademo.modle.LoginResult;
import org.xielipeng.retrofitokhttprxjavademo.net.helper.RequestParams;
import org.xielipeng.retrofitokhttprxjavademo.net.rxjava.RxResultHelper;
import org.xielipeng.retrofitokhttprxjavademo.net.rxjava.RxSubscribe;


/**
 * Created by xielipeng on 2016/8/12.
 */

public class Api {
    private static Api mApi;
    private ApiService mApiService;

    private Api() {
        RetrofitClient.getInstance().retrofit();
        mApiService = RetrofitClient.getInstance().createApi(ApiService.class);
    }

    public static Api getInstance() {
        if (mApi == null) {
            mApi = new Api();
        }
        return mApi;
    }

    public RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.add("token", "");
        return params;
    }

    public void login(String userName, String passWord, RxSubscribe<LoginResult> subscribe) {
        RequestParams params = getParams();
        params.add("userName", userName);
        params.add("passWord", passWord);
        mApiService.login(params.get()).compose(RxResultHelper.<LoginResult>handleResult())
                .subscribe(subscribe);
    }

}
