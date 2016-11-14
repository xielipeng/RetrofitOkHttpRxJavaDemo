package org.xielipeng.retrofitokhttprxjavademo.modle;

import java.io.Serializable;

/**
 * Created by xielipeng on 2016/9/22.
 */

public class LoginResult implements Serializable {

    private int uid;
    private String userName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
