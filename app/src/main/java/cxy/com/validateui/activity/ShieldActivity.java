package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cxy.com.validate.IValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.ValidateAnimation;
import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.MaxLength;
import cxy.com.validate.annotation.MinLength;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.Shield;
import cxy.com.validateui.R;


public class ShieldActivity extends AppCompatActivity {

    @Index(1)
    @NotNull(msg = "请输入手机号")
    @MinLength(length = 11, msg = "请输入正确的手机号")
    @Bind(R.id.et_phone)
    EditText etPhone;


    @Index(2)
    @NotNull(msg = "请输入密码")
    @MinLength(length = 6, msg = "请输入6位以上的密码")
    @MaxLength(length = 12, msg = "请输入12位以下的密码")
    @Shield
    @Bind(R.id.et_pwd)
    EditText etPwd;

    @Shield
    @Index(3)
    @NotNull(msg = "请输入性别")
    @Bind(R.id.et_sex)
    EditText etSex;

    @Index(4)
    @NotNull(msg = "请输入地址")
    @Bind(R.id.et_address)
    EditText etAddress;

    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.button)
    Button button;

    private boolean isShield = true;//是否屏蔽标志位


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shield);
        ButterKnife.bind(this);

        Validate.reg(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbtn_no_shield:
                        isShield = false;
                        break;
                    case R.id.rbtn_shield:
                        isShield = true;
                        break;
                }
            }
        });

     init();
    }

    private void init() {
        etPhone.setText("18310765821");
        etAddress.setText("address");
        etPwd.setText("123456");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        Validate.check(this, isShield, new IValidateResult() {
            @Override
            public void onValidateSuccess() {
                Toast.makeText(ShieldActivity.this, "验证通过", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValidateError(String msg, EditText editText) {
                Toast.makeText(ShieldActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Animation onValidateErrorAnno() {
                return ValidateAnimation.horizontalTranslate();
            }
        });
    }
}
