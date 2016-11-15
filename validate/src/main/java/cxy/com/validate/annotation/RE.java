package cxy.com.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by CXY on 2016/11/7.
 */
@Target(ElementType.FIELD)
public @interface RE {
    String only_Chinese="^[\\u4e00-\\u9fa5]{0,}$";
    String only_number="^[0-9]*$";
    String number_letter_underline="^\\w+$";
    String email="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    String re()  ;
    String msg()  ;
}
