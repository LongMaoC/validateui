package cxy.com.validate;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.MaxLength;
import cxy.com.validate.annotation.MinLength;
import cxy.com.validate.annotation.Money;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.Password1;
import cxy.com.validate.annotation.Password2;
import cxy.com.validate.annotation.RE;
import cxy.com.validate.bean.AttrBean;
import cxy.com.validate.bean.Basebean;
import cxy.com.validate.bean.LengthBean;
import cxy.com.validate.bean.MoneyBean;
import cxy.com.validate.bean.NotNullBean;
import cxy.com.validate.bean.PasswordBean;
import cxy.com.validate.bean.REBean;

/**
 * Created by CXY on 2016/11/2.
 */
public class Validate {
    private static final String TAG = "ValidateUI";
    private static Map<Activity, List<AttrBean>> activitys = new HashMap<>();

    private static final String TYPE_NOTNULL = "NotNull";
    private static final String TYPE_RE = "RE";
    private static final String TYPE_MAXLENGTH = "MaxLength";
    private static final String TYPE_MINLENGTH = "MinLength";
    private static final String TYPE_MONEY = "MONEY";
    private static final String TYPE_PASSWORD1 = "PASSWORD1";
    private static final String TYPE_PASSWORD2 = "PASSWORD2";


    public static void check(Activity activity, IValidateResult validateResult) {

        if (validateResult == null) {
            Log.e(TAG, "activity must register IValidateResult");
            return;
        }

        List<AttrBean> list = activitys.get(activity);

        if (list == null) {
            Log.e(TAG, "must be regedit validate in activity or this activity have annotation");
            return;
        }
        for (AttrBean attrBean : list) {
            if (attrBean.index == null) {
                Log.e(TAG, "EditText " + attrBean.name + " must set @Index");
                return;
            }
        }
        Collections.sort(list, new Comparator<AttrBean>() {
            public int compare(AttrBean arg0, AttrBean arg1) {
                return arg0.index.compareTo(arg1.index);
            }
        });


        //key : @Repeat -> flag
        //value : @Repeat ->RepeatBean
        for (AttrBean attrBean : list) {
            for (Basebean bean : attrBean.annos) {
                if (TYPE_NOTNULL.equals(bean.type)) {
                    if (ValidateCore.notNull(attrBean.view, attrBean.isEt, bean.msg, validateResult)) {
                        return;
                    }
                } else if (TYPE_RE.equals(bean.type)) {
                    if (ValidateCore.re(attrBean.view, attrBean.isEt, (REBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MAXLENGTH.equals(bean.type)) {
                    if (ValidateCore.max(attrBean.view, attrBean.isEt, (LengthBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MINLENGTH.equals(bean.type)) {
                    if (ValidateCore.min(attrBean.view, attrBean.isEt, (LengthBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MONEY.equals(bean.type)) {
                    if (ValidateCore.money(attrBean.view, attrBean.isEt, (MoneyBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_PASSWORD1.equals(bean.type)) {
                    if (ValidateCore.password1(attrBean.view, attrBean.isEt, validateResult)) {
                        return;
                    }
                    pwd1Attr = attrBean;
                } else if (TYPE_PASSWORD2.equals(bean.type)) {
                    if (ValidateCore.password2(attrBean, pwd1Attr, attrBean.isEt, (PasswordBean) bean, validateResult)) {
                        return;
                    }
                }
            }
        }


        validateResult.onValidateSuccess();
    }

    static AttrBean pwd1Attr = null;

    public static void reg(final Activity activity) {
        new Thread(new ValidateRegRunnable(activity)).start();
    }

    public static void unreg(Activity activity) {
        activitys.remove(activity);
    }


    static class ValidateRegRunnable implements Runnable {
        Activity activity;

        public ValidateRegRunnable(Activity activity) {
            this.activity = activity;
        }

        private Basebean validateType(Field field, Activity activity, String type) throws IllegalAccessException {
            if (type.equals(TYPE_NOTNULL)) {
                NotNull notnull = field.getAnnotation(NotNull.class);
                NotNullBean bean = new NotNullBean();
                bean.msg = notnull.msg();
                bean.type = type;
                return bean;
            } else if (type.equals(TYPE_RE)) {
                RE re = field.getAnnotation(RE.class);
                REBean reBean = new REBean();
                reBean.msg = re.msg();
                reBean.type = TYPE_RE;
                reBean.re = re.re();
                return reBean;
            } else if (type.equals(TYPE_MAXLENGTH)) {
                MaxLength anno = field.getAnnotation(MaxLength.class);
                LengthBean bean = new LengthBean();
                bean.msg = anno.msg();
                bean.type = TYPE_MAXLENGTH;
                bean.length = anno.length();
                return bean;
            } else if (type.equals(TYPE_MINLENGTH)) {
                MinLength anno = field.getAnnotation(MinLength.class);
                LengthBean bean = new LengthBean();
                bean.msg = anno.msg();
                bean.type = TYPE_MINLENGTH;
                bean.length = anno.length();
                return bean;
            } else if (type.equals(TYPE_MONEY)) {
                Money anno = field.getAnnotation(Money.class);
                MoneyBean bean = new MoneyBean();
                bean.msg = anno.msg();
                bean.type = TYPE_MONEY;
                bean.keep = anno.keey();
                return bean;
            } else if (type.equals(TYPE_PASSWORD1)) {
                PasswordBean bean = new PasswordBean();
                bean.type = TYPE_PASSWORD1;
                return bean;
            } else if (type.equals(TYPE_PASSWORD2)) {
                Password2 anno = field.getAnnotation(Password2.class);
                PasswordBean bean = new PasswordBean();
                bean.msg = anno.msg();
                bean.type = TYPE_PASSWORD2;
                return bean;
            }

            return null;
        }

        @Override
        public void run() {
            try {
                Class<? extends Activity> clazz = activity.getClass();

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(NotNull.class) ||
                            field.isAnnotationPresent(RE.class) ||
                            field.isAnnotationPresent(MaxLength.class) ||
                            field.isAnnotationPresent(MinLength.class) ||
                            field.isAnnotationPresent(Index.class) ||
                            field.isAnnotationPresent(Money.class) ||
                            field.isAnnotationPresent(Password1.class) ||
                            field.isAnnotationPresent(Password2.class)
                            ) {

                        if (!(field.getType() == EditText.class || field.getType() == TextView.class)) {
                            throw new RuntimeException("annotation must be on the EditText or TextView");
                        }
                        field.setAccessible(true);

                        List<AttrBean> editTextMap = activitys.get(activity);
                        if (editTextMap == null) {
                            editTextMap = new LinkedList<>();
                            activitys.put(activity, editTextMap);
                        }
                        AttrBean attr = new AttrBean();
                        attr.name = field.getName();
                        attr.view = field.get(activity);

                        if (field.getType() == EditText.class) {
                            attr.isEt = true;
                        } else if (field.getType() == TextView.class) {
                            attr.isEt = false;
                        }
                        if (attr.annos == null) {
                            attr.annos = new LinkedList<>();
                        }
                        editTextMap.add(attr);
                        if (field.isAnnotationPresent(NotNull.class))
                            attr.annos.add(validateType(field, activity, TYPE_NOTNULL));

                        if (field.isAnnotationPresent(RE.class))
                            attr.annos.add(validateType(field, activity, TYPE_RE));
                        if (field.isAnnotationPresent(MaxLength.class))
                            attr.annos.add(validateType(field, activity, TYPE_MAXLENGTH));
                        if (field.isAnnotationPresent(MinLength.class))
                            attr.annos.add(validateType(field, activity, TYPE_MINLENGTH));
                        if (field.isAnnotationPresent(Money.class))
                            attr.annos.add(validateType(field, activity, TYPE_MONEY));
                        if (field.isAnnotationPresent(Password1.class))
                            attr.annos.add(validateType(field, activity, TYPE_PASSWORD1));
                        if (field.isAnnotationPresent(Password2.class))
                            attr.annos.add(validateType(field, activity, TYPE_PASSWORD2));
                        if (field.isAnnotationPresent(Index.class))
                            attr.index = field.getAnnotation(Index.class).value();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
