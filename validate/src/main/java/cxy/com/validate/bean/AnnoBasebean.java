package cxy.com.validate.bean;

import android.widget.EditText;

/**
 * Created by CXY on 2016/11/9.
 */

public class AnnoBasebean {
    public String msg;
    public String type;
    public EditText editText;
    public Integer index;

    @Override
    public String toString() {
        return "Basebean{" +
                "msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                ", editText=" + editText +
                ", index=" + index +
                '}';
    }
}
