package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
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
import cxy.com.validate.annotation.Money;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.Password1;
import cxy.com.validate.annotation.Password2;
import cxy.com.validate.annotation.RE;
import cxy.com.validateui.R;


public class CommonActivity extends AppCompatActivity {


    @Index(1)
    @NotNull(msg = "不能为空！")
    @Bind(R.id.et_notnull)
    EditText etNotnull;

    @Index(3)
    @Bind(R.id.et_max)
    @MaxLength(length = 3, msg = "超出最大长度")
    EditText et_max;

    @Index(2)
    @Bind(R.id.et_min)
    @MinLength(length = 3, msg = "错误，字符数目不够")
    EditText et_min;

    @Index(4)
    @NotNull(msg = "两次密码验证->密码一不为能空！")
    @Password1()
    @Bind(R.id.et_pw1)
    EditText etPw1;

    @Index(5)
    @NotNull(msg = "两次密码验证->密码二不为能空！")
    @Password2(msg = "两次密码不一致！！！")
    @Bind(R.id.et_pw2)
    EditText etPw2;


    @Index(11)
    @RE(re = RE.only_Chinese, msg = "仅可输入中文，请重新输入")
    @Bind(R.id.et_only_Chinese)
    EditText etOnlyChinese;

    @Index(12)
    @RE(re = RE.only_number, msg = "仅可输入数字，请重新输入")
    @Bind(R.id.et_only_number)
    EditText etOnlyNumber;

    @Index(13)
    @RE(re = RE.number_letter_underline, msg = "仅可输入 数字 字母 下划线")
    @Bind(R.id.et_number_letter_underline)
    EditText etNumberLetterUnderline;

    @Index(14)
    @RE(re = RE.email, msg = "请输入正确的邮箱")
    @Bind(R.id.et_email)
    EditText etEmail;

    @Index(15)
    @NotNull(msg = "tv不能为空")
    @Bind(R.id.tv_textview)
    TextView tvTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        ButterKnife.bind(this);
        Validate.reg(this);

//        init();
    }

    private void init() {
        etNotnull.setText("chenxingyu1112@gmail.com");
        etPw1.setText("111");
        etPw2.setText("111");
        et_max.setText("11");
        et_min.setText("1111");
        etEmail.setText("chenxingyu1112@gmail.com");
        etOnlyChinese.setText("重");
        etOnlyNumber.setText("1");
        etNumberLetterUnderline.setText("1");
    }

    public void buttonClick() {
        final long statrtTime = System.currentTimeMillis();
        Validate.check(CommonActivity.this, new IValidateResult() {
            @Override
            public void onValidateSuccess() {
                long runTime = (System.currentTimeMillis() - statrtTime);
                Log.e("TAG", "time = " + (runTime));
                Toast.makeText(CommonActivity.this, "验证成功,耗时 " + runTime + " 毫秒", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValidateError(String msg, View view) {
                view.setFocusable(true);
                Toast.makeText(CommonActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Animation onValidateErrorAnno() {
                return ValidateAnimation.horizontalTranslate();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }

    @OnClick({R.id.btn_setTxt_tv, R.id.btn_clear_tv, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setTxt_tv:
                tvTextview.setText("测试文字");
                break;
            case R.id.btn_clear_tv:
                tvTextview.setText("");
                break;
            case R.id.button:
                buttonClick();
                break;
        }
    }
}
