package cxy.com.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by CXY on 2016/11/7.
 */
@Target(ElementType.FIELD)
public @interface Index {
    int value();
}
