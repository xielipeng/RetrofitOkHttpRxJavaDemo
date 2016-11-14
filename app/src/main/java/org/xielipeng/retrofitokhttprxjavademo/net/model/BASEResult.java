package org.xielipeng.retrofitokhttprxjavademo.net.model;

/**
 * Created by xielipeng on 2016/8/12.
 */

public class BASEResult<T> {
    public boolean status;
    public String msg;
    public T data;

    public boolean success() {
        return status;
    }

}
