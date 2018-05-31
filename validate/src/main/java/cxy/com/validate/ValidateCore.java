package cxy.com.validate;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cxy.com.validate.bean.AttrBean;
import cxy.com.validate.bean.LengthBean;
import cxy.com.validate.bean.MoneyBean;
import cxy.com.validate.bean.PasswordBean;
import cxy.com.validate.bean.REBean;
import cxy.com.validate.bean.StartsWithBean;

/*****************************************
 * @author cxy
 *         created at  2016/11/9 11:51
 ****************************************/
public class ValidateCore {
    private static final String msg = " content is null or \"\" ,  You can add 【@NotNull】 ";

    private static void setEditText(Object view, boolean isEt, String msg, IValidateResult validateResult) {
        if (isEt) {
            if (validateResult.onValidateErrorAnno() != null) {
                ((EditText) view).startAnimation(validateResult.onValidateErrorAnno());
            }
            validateResult.onValidateError(msg, (View) view);
        } else {
            validateResult.onValidateError(msg, (View) view);
        }
    }

    private static boolean isNull(Object view, boolean isEt, IValidateResult validateResult) {
        if (view == null)
            throw new NullPointerException("ValidateUI : " + (isEt ? "EditText" : "TextView") + " can not be empty!");
        if (TextUtils.isEmpty(((TextView) view).getText().toString())) {
            if (isEt) {
                if (validateResult.onValidateErrorAnno() != null) {
                    ((EditText) view).startAnimation(validateResult.onValidateErrorAnno());
                }
                Log.e("ValidateUI", "EditText " + msg);
            } else {
                Log.e("ValidateUI", "TextView " + msg);
            }
            return true;
        }
        return false;
    }


    public static boolean notNull(Object view, boolean isEt, String msg, IValidateResult validateResult) {
        if (TextUtils.isEmpty(((TextView) view).getText().toString())) {
            setEditText(view, isEt, msg, validateResult);
            return true;
        }
        return false;
    }


    public static boolean re(Object view, boolean isEt, REBean bean, IValidateResult validateResult) {
        if (isNull(view, isEt, validateResult)) return true;

        Pattern r = Pattern.compile(bean.re);
        Matcher m = r.matcher(((TextView) view).getText().toString());
        if (!m.matches()) {
            setEditText(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }


    public static boolean max(Object view, boolean isEt, LengthBean bean, IValidateResult validateResult) {
        if (isNull(view, isEt, validateResult)) return true;

        if (((TextView) view).getText().toString().length() > bean.length) {
            setEditText(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    public static boolean min(Object view, boolean isEt, LengthBean bean, IValidateResult validateResult) {
        if (isNull(view, isEt, validateResult)) return true;

        if (((TextView) view).getText().toString().length() < bean.length) {
            setEditText(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    public static boolean money(Object view, boolean isEt, MoneyBean bean, IValidateResult validateResult) {
        if (isNull(view, isEt, validateResult)) return true;

        String viewText = ((TextView) view).getText().toString();
        if (viewText.indexOf(".") == -1) return false;//money: 123

        String keep = String.valueOf(bean.keep);
        if (bean.keep > 2) {
            keep = String.valueOf(2);
        }
        if (bean.keep <= 0) {
            keep = String.valueOf(1);
        }

        String re = "^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(\\.\\d{" + keep + "})?$";
        Pattern r = Pattern.compile(re);
        Matcher m = r.matcher(viewText);
        if (!m.matches()) {
            setEditText(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    public static boolean password1(Object view, boolean isEt, IValidateResult validateResult) {
        if (isNull(view, isEt, validateResult)) {
            return true;
        }
        return false;
    }

    public static boolean password2(AttrBean view, AttrBean pwd1Attr, boolean isEt, PasswordBean bean, IValidateResult validateResult) {
        if (isNull(view.view, isEt, validateResult)) {
            return true;
        }
        if (!((TextView) view.view).getText().toString().equals(((EditText) pwd1Attr.view).getText().toString())) {
            setEditText(view.view, view.isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    public static boolean startsWith(Object view, boolean isEt, StartsWithBean bean, IValidateResult validateResult) {
        if (isNull(view, isEt, validateResult)) return true;

        String viewStr = null;
        if (isEt) {
            viewStr = ((EditText) view).getText().toString();
        } else {
            viewStr = ((TextView) view).getText().toString();
        }

        if (bean.value != null) {

            if (bean.value.length() > viewStr.length()) {
                setEditText(view, isEt, bean.msg, validateResult);
                return true;
            }

            if (bean.ignoreCase) {//忽略大小写比较
                if (!viewStr.toUpperCase().startsWith(bean.value.toUpperCase())) {
                    setEditText(view, isEt, bean.msg, validateResult);
                    return true;
                }
            } else {
                if (bean.value.equals("") && viewStr.length() > 0) {
                    setEditText(view, isEt, bean.msg, validateResult);
                    return true;
                }
                if (!(viewStr.substring(0, bean.value.length()).equals(bean.value))) {
                    setEditText(view, isEt, bean.msg, validateResult);
                    return true;
                }
            }
        }
        return false;
    }
}
