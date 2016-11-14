package org.xielipeng.retrofitokhttprxjavademo.net.rxjava;


import org.xielipeng.retrofitokhttprxjavademo.net.helper.ServerException;
import org.xielipeng.retrofitokhttprxjavademo.net.model.BASEResult;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by xielipeng on 2016/8/12.
 */

public class RxResultHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BASEResult<T>, T> handleResult() {
        return new Observable.Transformer<BASEResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<BASEResult<T>> tObservable) {
                return tObservable.flatMap(new Func1<BASEResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BASEResult<T> result) {
                        if (result.success()) {
                            return createData(result.data);
                        } else {
                            return Observable.error(new ServerException(result.msg));
                        }
                    }
                }).compose(RxSchedulersHelper.<T>io_main());
            }
        };

    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }
}
