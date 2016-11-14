package org.xielipeng.retrofitokhttprxjavademo;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static Toast mToast;
    private static Context context;

    public static void show(Context ctx, String text) {
        if (mToast == null || context == null
                || !context.getClass().equals(ctx.getClass())) {
            // 同步块，线程安全的创建实例
            synchronized (ToastUtils.class) {
                // 再次检查实例是否存在，如果不存在才真正的创建实例
                mToast = Toast.makeText(ctx, text, Toast.LENGTH_LONG);
                context = ctx;
            }
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
