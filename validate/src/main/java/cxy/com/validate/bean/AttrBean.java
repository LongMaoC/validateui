package cxy.com.validate.bean;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by CXY on 2016/11/10.
 */

public class AttrBean {
    public Integer index ;
    public String name ;
    public LinkedList<Basebean> annos ;

    @Override
    public String toString() {
        return "AttrBean{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", annos=" + annos +
                '}';
    }
}
