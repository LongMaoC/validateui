package cxy.com.validate;

import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by cxy on 17-9-4.
 */

public abstract class SimpleValidateResult implements IValidateResult {

    @Override
    public void onValidateError(String s, EditText editText) {
        Toast.makeText(editText.getContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Animation onValidateErrorAnno() {
        return null;
    }
}
