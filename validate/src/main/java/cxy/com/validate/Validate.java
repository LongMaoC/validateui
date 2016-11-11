package cxy.com.validate;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cxy.com.validate.annotation.*;
import cxy.com.validate.bean.*;

/**
 * Created by CXY on 2016/11/2.
 */
public class Validate {

    private static Map<Activity, List<Basebean>> activitys = new HashMap<>();

    private static final String TYPE_NOTNULL = "NotNull";
    private static final String TYPE_REPEAT = "Repeat";
    private static final String TYPE_REPEAT2 = "RepeatLast";
    private static final String TYPE_RE = "RE";
    private static final String TYPE_MAXLENGTH = "MaxLength";
    private static final String TYPE_MINLENGTH = "MinLength";


    public static void check(Activity activity, IValidateResult validateResult) {

        if (validateResult == null) {
            throw new RuntimeException("activity must register IValidateResult");
        }

        List<Basebean> list = activitys.get(activity);

        if (list == null) {
            throw new RuntimeException("must be regedit validate in activity or this activity have annotation");
        }

        //key : @Repeat -> flag
        //value : @Repeat ->RepeatBean
        Map<String, List<RepeatBean>> repeatList = new LinkedHashMap<>();
        for (Basebean bean : list) {
            Log.e("TAG", "type = " + bean.type);
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
                    throw new RuntimeException("if you want to use【@RepeatLast】，mast use 【@Repeat】 first");
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
                            field.isAnnotationPresent(Index.class)
                            ) {
                        field.setAccessible(true);

                        List<Basebean> editTextMap = activitys.get(activity);
                        if (editTextMap == null) {
                            editTextMap = new LinkedList<>();
                            activitys.put(activity, editTextMap);
                        }

                        if (field.isAnnotationPresent(NotNull.class))
                            editTextMap.add(validateType(field, activity, TYPE_NOTNULL));
                        if (field.isAnnotationPresent(Repeat.class))
                            editTextMap.add(validateType(field, activity, TYPE_REPEAT));
                        if (field.isAnnotationPresent(RepeatLast.class))
                            editTextMap.add(validateType(field, activity, TYPE_REPEAT2));
                        if (field.isAnnotationPresent(RE.class))
                            editTextMap.add(validateType(field, activity, TYPE_RE));
                        if (field.isAnnotationPresent(MaxLength.class))
                            editTextMap.add(validateType(field, activity, TYPE_MAXLENGTH));
                        if (field.isAnnotationPresent(MinLength.class))
                            editTextMap.add(validateType(field, activity, TYPE_MINLENGTH));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void text() {
//                field.getType() = class android.widget.EditText
//                field.getName() = et_phone
//                field.getModifiers() = 0
//                field.getType().getName() = android.widget.EditText
//                field.getType().getSimpleName() = EditText
//                field.getType().getCanonicalName() = android.widget.EditText
//                field.getType().getPackage() = package android.widget
//                EditText editText = (EditText)field.get(activity);
    }
}
