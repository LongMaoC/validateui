package cxy.com.validate;

import android.view.animation.Animation;
import android.widget.EditText;

/**
 * Created by CXY on 2016/11/9.
 */
public interface IValidateResult {
    void onValidateSuccess();

    void onValidateError(EditText editText, String msg);

    Animation onValidateErrorAnno();
}
