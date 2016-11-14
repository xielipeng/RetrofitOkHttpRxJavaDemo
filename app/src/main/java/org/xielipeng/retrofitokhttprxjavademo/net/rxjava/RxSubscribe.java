package org.xielipeng.retrofitokhttprxjavademo.net.rxjava;

import android.content.Context;
import android.content.DialogInterface;

import org.xielipeng.retrofitokhttprxjavademo.MyApplication;
import org.xielipeng.retrofitokhttprxjavademo.net.helper.NetUtil;
import org.xielipeng.retrofitokhttprxjavademo.net.helper.ServerException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by cool on 2016/8/12.
 */

public abstract class RxSubscribe<T> extends Subscriber<T> {
    private Context mContext;
    private SweetAlertDialog dialog;
    private String msg;
    private boolean isShow;

    /**
     * @param context context
     * @param msg     dialog msg
     * @param isShow  dialog show
     */
    public RxSubscribe(Context context, String msg, boolean isShow) {
        this.mContext = context;
        this.msg = msg;
        this.isShow = isShow;
    }

    /**
     * @param context context
     * @param msg     dialog msg
     */
    public RxSubscribe(Context context, String msg) {
        this(context, msg, true);
    }

    /**
     * @param context context
     */
    public RxSubscribe(Context context) {
        this(context, "加载中...");
    }

    @Override
    public void onCompleted() {
        if (isShow)
            dialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShow) {
            dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(msg);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!isUnsubscribed()) {
                        unsubscribe();
                    }
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!NetUtil.getNetworkIsConnected(MyApplication.getInstance())) {
            _onError("网络不可用");
        } else if (e instanceof ServerException) {
            _onError(e.getMessage());
        } else {
            _onError("请求失败，请稍后重试...");
        }
        if (isShow)
            dialog.dismiss();

    }

    public abstract void _onNext(T t);

    public abstract void _onError(String message);
}
