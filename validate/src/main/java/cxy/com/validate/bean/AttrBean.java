package cxy.com.validate.bean;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by CXY on 2016/11/10.
 */

public class AttrBean {
    public static final int ET = 0x1100;
    public static final int TV = 0x0011;

    public Integer index ;
    public String name ;
    public Object view ;
    public boolean isEt ;

    public LinkedList<Basebean> annos ;
}
