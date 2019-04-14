package android.ly.business.domain;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    /**
     * 错误码
     */
    public boolean error;

    /**
     * 提示语
     */
    public String msg;

    public BaseEntity() {
    }
}