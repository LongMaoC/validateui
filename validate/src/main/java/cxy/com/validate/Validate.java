package cxy.com.validate;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.MaxLength;
import cxy.com.validate.annotation.MinLength;
import cxy.com.validate.annotation.Money;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.RE;
import cxy.com.validate.annotation.Repeat;
import cxy.com.validate.annotation.RepeatLast;
import cxy.com.validate.bean.AttrBean;
import cxy.com.validate.bean.Basebean;
import cxy.com.validate.bean.LengthBean;
import cxy.com.validate.bean.MoneyBean;
import cxy.com.validate.bean.NotNullBean;
import cxy.com.validate.bean.REBean;
import cxy.com.validate.bean.RepeatBean;

/**
 * Created by CXY on 2016/11/2.
 */
public class Validate {
    private static final String TAG = "ValidateUI";
    private static Map<Activity, List<AttrBean>> activitys = new HashMap<>();

    private static final String TYPE_NOTNULL = "NotNull";
    private static final String TYPE_REPEAT = "Repeat";
    private static final String TYPE_REPEAT2 = "RepeatLast";
    private static final String TYPE_RE = "RE";
    private static final String TYPE_MAXLENGTH = "MaxLength";
    private static final String TYPE_MINLENGTH = "MinLength";
    private static final String TYPE_MONEY = "MONEY";


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
        Map<String, List<RepeatBean>> repeatList = new LinkedHashMap<>();
        for (AttrBean attrBean : list) {
            for (Basebean bean : attrBean.annos) {
                if (TYPE_NOTNULL.equals(bean.type)) {
                    if (ValidateCore.notNull(bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_REPEAT.equals(bean.type)) {
                    String flag = ((RepeatBean) bean).flag;
                    List<RepeatBean> repeatBeen = repeatList.get(flag);
                    if (repeatBeen == null) {
                        repeatBeen = new LinkedList<>();
                        repeatList.put(flag, repeatBeen);
                    }
                    repeatBeen.add((RepeatBean) bean);
                    if (ValidateCore.repeat1((RepeatBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_REPEAT2.equals(bean.type)) {
                    String flag = ((RepeatBean) bean).flag;
                    List<RepeatBean> repeatBeen = repeatList.get(flag);
                    if (repeatBeen == null) {
                        Log.e(TAG, "If you want to use the 【@RepeatLast】, you must first use 【@Repeat】, or change their order of 【@Index】");
                        return;
                    }
                    if (ValidateCore.repeat2((RepeatBean) bean, repeatBeen, validateResult)) {
                        return;
                    }
                } else if (TYPE_RE.equals(bean.type)) {
                    if (ValidateCore.re((REBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MAXLENGTH.equals(bean.type)) {
                    if (ValidateCore.max((LengthBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MINLENGTH.equals(bean.type)) {
                    if (ValidateCore.min((LengthBean) bean, validateResult)) {
                        return;
                    }
                }else if (TYPE_MONEY.equals(bean.type)) {
                    if (ValidateCore.money((MoneyBean) bean, validateResult)) {
                        return;
                    }
                }
            }
        }


        validateResult.onValidateSuccess();
    }


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
                bean.editText = (EditText) field.get(activity);
                bean.msg = notnull.msg();
                bean.type = type;
                return bean;
            } else if (type.equals(TYPE_REPEAT)) {
                Repeat password1 = field.getAnnotation(Repeat.class);
                RepeatBean repeatBean = new RepeatBean();
                repeatBean.flag = password1.flag();
                repeatBean.type = TYPE_REPEAT;
                repeatBean.isLast = false;
                repeatBean.editText = (EditText) field.get(activity);
                return repeatBean;
            } else if (type.equals(TYPE_REPEAT2)) {
                RepeatLast password2 = field.getAnnotation(RepeatLast.class);
                RepeatBean repeatBean = new RepeatBean();
                repeatBean.editText = (EditText) field.get(activity);
                repeatBean.flag = password2.flag();
                repeatBean.msg = password2.msg();
                repeatBean.type = TYPE_REPEAT2;
                repeatBean.isLast = true;
                return repeatBean;
            } else if (type.equals(TYPE_RE)) {
                RE re = field.getAnnotation(RE.class);
                REBean reBean = new REBean();
                reBean.editText = (EditText) field.get(activity);
                reBean.msg = re.msg();
                reBean.type = TYPE_RE;
                reBean.re = re.re();
                return reBean;
            } else if (type.equals(TYPE_MAXLENGTH)) {
                MaxLength anno = field.getAnnotation(MaxLength.class);
                LengthBean bean = new LengthBean();
                bean.editText = (EditText) field.get(activity);
                bean.msg = anno.msg();
                bean.type = TYPE_MAXLENGTH;
                bean.length = anno.length();
                return bean;
            } else if (type.equals(TYPE_MINLENGTH)) {
                MinLength anno = field.getAnnotation(MinLength.class);
                LengthBean bean = new LengthBean();
                bean.editText = (EditText) field.get(activity);
                bean.msg = anno.msg();
                bean.type = TYPE_MINLENGTH;
                bean.length = anno.length();
                return bean;
            }else if (type.equals(TYPE_MONEY)) {
                Money anno = field.getAnnotation(Money.class);
                MoneyBean bean = new MoneyBean();
                bean.editText = (EditText) field.get(activity);
                bean.msg = anno.msg();
                bean.type = TYPE_MONEY;
                bean.keep = anno.keey();
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
                            field.isAnnotationPresent(Repeat.class) ||
                            field.isAnnotationPresent(RepeatLast.class) ||
                            field.isAnnotationPresent(RE.class) ||
                            field.isAnnotationPresent(MaxLength.class) ||
                            field.isAnnotationPresent(MinLength.class) ||
                            field.isAnnotationPresent(Index.class)||
                            field.isAnnotationPresent(Money.class)
                            ) {

                        if (!(field.getType().getName().equals("android.widget.EditText"))) {
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
                        if (attr.annos == null) {
                            attr.annos = new LinkedList<>();
                        }
                        editTextMap.add(attr);
                        if (field.isAnnotationPresent(NotNull.class))
                            attr.annos.add(validateType(field, activity, TYPE_NOTNULL));
                        if (field.isAnnotationPresent(Repeat.class))
                            attr.annos.add(validateType(field, activity, TYPE_REPEAT));
                        if (field.isAnnotationPresent(RepeatLast.class))
                            attr.annos.add(validateType(field, activity, TYPE_REPEAT2));
                        if (field.isAnnotationPresent(RE.class))
                            attr.annos.add(validateType(field, activity, TYPE_RE));
                        if (field.isAnnotationPresent(MaxLength.class))
                            attr.annos.add(validateType(field, activity, TYPE_MAXLENGTH));
                        if (field.isAnnotationPresent(MinLength.class))
                            attr.annos.add(validateType(field, activity, TYPE_MINLENGTH));
                        if (field.isAnnotationPresent(Money.class))
                            attr.annos.add(validateType(field, activity, TYPE_MONEY));
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
