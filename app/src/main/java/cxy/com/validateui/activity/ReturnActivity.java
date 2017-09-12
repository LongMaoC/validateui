package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cxy.com.validate.IValidateResult;
import cxy.com.validate.SimpleValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.ValidateAnimation;
import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.MaxLength;
import cxy.com.validate.annotation.MinLength;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validateui.R;


public class ReturnActivity extends AppCompatActivity {

    @Index(1)
    @NotNull(msg = "请输入手机号")
    @MinLength(length = 11, msg = "请输入正确的手机号")
    @Bind(R.id.et_phone)
    EditText etPhone;

    @Index(2)
    @NotNull(msg = "请输入密码")
    @MinLength(length = 6, msg = "请输入6位以上的密码")
    @MaxLength(length = 12, msg = "请输入12位以下的密码")
    @Bind(R.id.et_pwd)
    EditText etPwd;


    private boolean isShield = true;//是否屏蔽标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        ButterKnife.bind(this);
        Validate.reg(this);
    }

    @OnClick({R.id.button, R.id.btn_simple})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:

                /**
                 * 不常用
                 * 需要重写全部的实现方法
                 */
                Validate.check(this, isShield, new IValidateResult() {
                    @Override
                    public void onValidateSuccess() {
                        Toast.makeText(ReturnActivity.this, "IValidateResult:" + "验证通过", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onValidateError(String msg, EditText editText) {
                        Toast.makeText(ReturnActivity.this, "IValidateResult:" + msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public Animation onValidateErrorAnno() {
                        return ValidateAnimation.horizontalTranslate();
                    }
                });
                break;
            case R.id.btn_simple:
                /**
                 * 常用
                 * 默认动画：无
                 * 默认失败方法：通过View获取context，进行Toast提示
                 */
                Validate.check(this, new SimpleValidateResult() {
                    @Override
                    public void onValidateSuccess() {
                        Toast.makeText(ReturnActivity.this, "验证通过", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }

}
