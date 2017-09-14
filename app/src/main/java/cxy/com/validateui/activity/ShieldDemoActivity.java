package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cxy.com.validate.IValidateResult;
import cxy.com.validate.SimpleValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.ValidateAnimation;
import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.MinLength;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.Password1;
import cxy.com.validate.annotation.Password2;
import cxy.com.validate.annotation.Shield;
import cxy.com.validateui.R;


/**
 * https://github.com/LongMaoC/validateui
 *
 * @Shield 注解在实际中的使用demo
 * <p>
 * 注意 ： 1. Shield 注解 放在想屏蔽的控件上
 * 2. 校验时，调用check()方法，注意传递参数
 */
public class ShieldDemoActivity extends AppCompatActivity {

    @Shield
    @Index(1)
    @NotNull(msg = "请输入安全答案")
    @Bind(R.id.et_answer)
    EditText etAnswer;

    @Shield
    @Index(2)
    @NotNull(msg = "请输入新密码")
    @MinLength(length = 6, msg = "密码长度最少为6位")
    @Password1
    @Bind(R.id.et_new_pwd1)
    EditText etNewPwd1;

    @Shield
    @Index(3)
    @MinLength(length = 6, msg = "密码长度最少为6位")
    @NotNull(msg = "请输入确认密码")
    @Password2(msg = "两次密码不一致，请重新输入")
    @Bind(R.id.et_new_pwd2)
    EditText etNewPwd2;

    @Index(4)
    @MinLength(length = 11, msg = "请输入正确的手机号")
    @NotNull(msg = "请输入手机号")
    @Bind(R.id.et_phone)
    EditText etPhone;

    @Shield
    @Index(5)
    @NotNull(msg = "请输入验证码")
    @Bind(R.id.et_code)
    EditText etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shield_demo);
        ButterKnife.bind(this);
        Validate.reg(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }


    @OnClick({R.id.tv_countDown, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_countDown:
                countDownClick();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        Validate.check(this, new SimpleValidateResult() {
            @Override
            public void onValidateSuccess() {
                Toast.makeText(ShieldDemoActivity.this, "提交按钮，回调校验成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void countDownClick() {
        Validate.check(this, true, new IValidateResult() {
            @Override
            public void onValidateSuccess() {
                Toast.makeText(ShieldDemoActivity.this, "发送验证码按钮，回调校验成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValidateError(String msg, EditText editText) {
                Toast.makeText(ShieldDemoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Animation onValidateErrorAnno() {
                return ValidateAnimation.horizontalTranslate();
            }
        });
    }
}
