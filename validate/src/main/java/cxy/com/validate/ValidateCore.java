package cxy.com.validate;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cxy.com.validate.bean.Basebean;
import cxy.com.validate.bean.LengthBean;
import cxy.com.validate.bean.MoneyBean;
import cxy.com.validate.bean.REBean;
import cxy.com.validate.bean.RepeatBean;

/*****************************************
 * @author cxy
 *         created at  2016/11/9 11:51
 ****************************************/
public class ValidateCore {

    private static void setEditText(EditText editText, IValidateResult validateResult) {
        if (validateResult.onValidateErrorAnno() != null) {
            editText.startAnimation(validateResult.onValidateErrorAnno());
        }
        editText.selectAll();
    }

    public static boolean notNull(EditText editText, String msg, IValidateResult validateResult) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            if (validateResult.onValidateErrorAnno() != null) {
                editText.startAnimation(validateResult.onValidateErrorAnno());
            }
            validateResult.onValidateError(msg, editText);
            return true;
        }
        return false;
    }


    public static boolean notNull(Basebean bean, IValidateResult validateResult) {
        if (TextUtils.isEmpty(bean.editText.getText().toString())) {
            setEditText(bean.editText, validateResult);
            validateResult.onValidateError(bean.msg, bean.editText);
            return true;
        }
        return false;
    }


    public static boolean re(REBean bean, IValidateResult validateResult) {
        if (notNull(bean.editText, "EditText content is null or \"\" ,  You can add 【@NotNull】 ", validateResult)) {
            return true;
        }

        Pattern r = Pattern.compile(bean.re);
        Matcher m = r.matcher(bean.editText.getText().toString());
        if (!m.matches()) {
            setEditText(bean.editText, validateResult);
            validateResult.onValidateError(bean.msg, bean.editText);
            return true;
        }

        return false;
    }

    public static boolean repeat1(RepeatBean bean, IValidateResult validateResult) {
        if (notNull(bean.editText, "EditText content is null or \"\" ,  You can add 【@NotNull】 ", validateResult)) {
            return true;
        }
        return false;
    }

    public static boolean repeat2(RepeatBean repeatLastbean, List<RepeatBean> repeatBeen, IValidateResult validateResult) {
        if (notNull(repeatLastbean.editText, "EditText content is null or \"\" ,  You can add 【@NotNull】 ", validateResult)) {
            return true;
        }

        for (int i = 1; i <= repeatBeen.size(); i++) {
            RepeatBean one = null;
            RepeatBean two = null;

            if (i != repeatBeen.size()) {
                one = repeatBeen.get(i - 1);
                two = repeatBeen.get(i);
            } else {
                one = repeatBeen.get(i - 1);
                two = repeatLastbean;
            }

            if (!one.editText.getText().toString().equals(two.editText.getText().toString())) {
                if (validateResult.onValidateErrorAnno() != null) {
                    two.editText.startAnimation(validateResult.onValidateErrorAnno());
                }
                two.editText.selectAll();
                validateResult.onValidateError(repeatLastbean.msg, two.editText);
                return true;
            }

            one = null;
            two = null;
        }


        return false;
    }

    public static boolean max(LengthBean bean, IValidateResult validateResult) {
        if (notNull(bean.editText, "EditText content is null or \"\" ,  You can add 【@NotNull】 ", validateResult)) {
            return true;
        }
        if (bean.editText.getText().toString().length() > bean.length) {
            setEditText(bean.editText, validateResult);
            validateResult.onValidateError(bean.msg, bean.editText);
            return true;
        }
        return false;
    }

    public static boolean min(LengthBean bean, IValidateResult validateResult) {
        if (notNull(bean.editText, "EditText content is null or \"\" ,  You can add 【@NotNull】 ", validateResult)) {
            return true;
        }
        if (bean.editText.getText().toString().length() < bean.length) {
            setEditText(bean.editText, validateResult);
            validateResult.onValidateError(bean.msg, bean.editText);
            return true;
        }
        return false;
    }

    public static boolean money(MoneyBean bean, IValidateResult validateResult) {
        if (notNull(bean.editText, "EditText content is null or \"\" ,  You can add 【@NotNull】 ", validateResult)) {
            return true;
        }
        String keep = null;
        keep = String.valueOf(bean.keep);
        if (bean.keep > 2) {
            keep = String.valueOf(2);
        }
        if (bean.keep <= 0) {
            keep = String.valueOf(1);
        }

        String re = "^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)\\.(?:\\d{"+keep+"})?$";
        Pattern r = Pattern.compile(re);
        Matcher m = r.matcher(bean.editText.getText().toString());
        if (!m.matches()) {
            setEditText(bean.editText, validateResult);
            validateResult.onValidateError(bean.msg, bean.editText);
            return true;
        }
        return false;
    }
}
