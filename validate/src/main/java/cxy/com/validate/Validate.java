package cxy.com.validate;

import android.app.Activity;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.RE;
import cxy.com.validate.annotation.Repeat;
import cxy.com.validate.annotation.RepeatLast;
import cxy.com.validate.bean.Basebean;
import cxy.com.validate.bean.NotNullBean;
import cxy.com.validate.bean.REBean;
import cxy.com.validate.bean.RepeatBean;

/**
 * Created by CXY on 2016/11/2.
 */
public class Validate {

    private static Map<Activity, List<Basebean>> activitys = new HashMap<>();

    private static final String TYPE_NOTNULL = "NotNull";
    private static final String TYPE_REPEAT = "repeat";
    private static final String TYPE_REPEAT1 = "Repeat";
    private static final String TYPE_REPEAT2 = "RepeatLast";
    private static final String TYPE_RE = "RE";

    public static void check(Activity activity, IValidateResult validateResult) {

        if (validateResult == null) {
            throw new RuntimeException("activity must register IValidateResult");
        }

        List<Basebean> list = activitys.get(activity);

        if (list == null) {
            throw new RuntimeException("must be regedit validate in activity");
        }

        Map<String, List<RepeatBean>> repeatList = new LinkedHashMap<>();
        for (Basebean bean : list) {
            if (TYPE_NOTNULL.equals(bean.type)) {
                if (ValidateCore.notNull((NotNullBean) bean, validateResult)) {
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
            } else if (TYPE_RE.equals(bean.type)) {
                if (ValidateCore.re((REBean) bean, validateResult)) {
                    return;
                }
            }
        }

        if (repeatList != null) {
            if (ValidateCore.repeat(repeatList, validateResult)) {
                return;
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
            } else if (type.equals(TYPE_REPEAT1)) {
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
                repeatBean.type = TYPE_REPEAT;
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
                            field.isAnnotationPresent(RE.class)
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
                            editTextMap.add(validateType(field, activity, TYPE_REPEAT1));
                        if (field.isAnnotationPresent(RepeatLast.class))
                            editTextMap.add(validateType(field, activity, TYPE_REPEAT2));
                        if (field.isAnnotationPresent(RE.class))
                            editTextMap.add(validateType(field, activity, TYPE_RE));
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
