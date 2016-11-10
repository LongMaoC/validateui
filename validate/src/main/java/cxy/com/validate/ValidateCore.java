package cxy.com.validate;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cxy.com.validate.bean.NotNullBean;
import cxy.com.validate.bean.REBean;
import cxy.com.validate.bean.RepeatBean;

/**
 * Created by CXY on 2016/11/9.
 */
public class ValidateCore {

    public static boolean notNull(NotNullBean bean, IValidateResult validateResult) {
        if (TextUtils.isEmpty(bean.editText.getText().toString())) {
            setEditText(bean.editText,validateResult);
            validateResult.onValidateError(bean.editText, bean.msg);
            return true;
        }
        return false;
    }


    public static boolean repeat(Map<String, List<RepeatBean>> repeatList, IValidateResult validateResult) {
        for (Map.Entry<String, List<RepeatBean>> entry : repeatList.entrySet()) {
            List<RepeatBean> values = entry.getValue();
            if (values.size() > 1) {
                boolean isReturn = false;
                RepeatBean temp = null;
                for (int i = 1; i < values.size(); i++) {
                    if(i ==1){
                        if (TextUtils.isEmpty(values.get(0).editText.getText().toString())) {
                            setEditText(temp.editText,validateResult);
                            validateResult.onValidateError(values.get(0).editText, "must set @NotNull ");
                            return true;
                        }
                        if (TextUtils.isEmpty(values.get(i).editText.getText().toString())) {
                            setEditText(temp.editText,validateResult);
                            validateResult.onValidateError(values.get(i).editText, "must set @NotNull ");
                            return true;
                        }
                    }

                    if (values.get(0).editText.getText().toString().equals(values.get(i).editText.getText().toString())) {
                        isReturn = false;
                    } else {
                        isReturn = true;
                    }
                    if (values.get(i).isLast) {
                        temp = values.get(i);
                    }
                }
                if (isReturn) {
                    setEditText(temp.editText,validateResult);
                    validateResult.onValidateError(temp.editText, temp.msg);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean re(REBean bean, IValidateResult validateResult) {
        if (TextUtils.isEmpty(bean.editText.getText().toString())) {
            validateResult.onValidateError(bean.editText, "must set @NotNull ");
            return true;
        }

        Pattern r = Pattern.compile(bean.re);
        Matcher m = r.matcher(bean.editText.getText().toString());
        if (!m.matches()) {
            setEditText(bean.editText,validateResult);
            validateResult.onValidateError(bean.editText, bean.msg);
            return true;
        }

        return false;
    }


    private static void setEditText(EditText editText, IValidateResult validateResult) {
        if (validateResult.onValidateErrorAnno() != null) {
            editText.startAnimation(validateResult.onValidateErrorAnno());
        }
        editText.selectAll();
    }
}
