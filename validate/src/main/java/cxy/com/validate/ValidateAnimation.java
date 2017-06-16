package cxy.com.validate;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by CXY on 2016/11/9.
 */
public class ValidateAnimation {

    public static Animation horizontalTranslate(){
        TranslateAnimation  animation = new TranslateAnimation(0, -15, 0, 0);
        animation.setInterpolator(new CycleInterpolator(1));
        animation.setDuration(120);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        return animation ;
    }
}
