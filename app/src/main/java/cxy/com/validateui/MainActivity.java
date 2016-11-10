package cxy.com.validateui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cxy.com.validate.IValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.ValidateAnimation;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.RE;
import cxy.com.validate.annotation.Repeat;
import cxy.com.validate.annotation.RepeatLast;


//annotation
public class MainActivity extends AppCompatActivity implements IValidateResult {


    Button button;

    @NotNull(msg = "不能为空！")
    private EditText etNotnull;

    @NotNull(msg = "两次密码验证->密码一不为能空！")
    @Repeat(flag = "AA")
    private EditText etPw1;

    @NotNull(msg = "两次密码验证->密码二不为能空！")
    @RepeatLast(flag = "AA", msg = "两次密码不一致！！！")
    private EditText etPw2;

    @NotNull(msg = "多次密码验证->密码一不为能空！")
    @Repeat(flag = "BB")
    private EditText etPwFlag1;
    @NotNull(msg = "多次密码验证->密码二不为能空！")
    @Repeat(flag = "BB")
    private EditText etPwFlag2;
    @NotNull(msg = "多次密码验证->密码三不为能空！")
    @RepeatLast(flag = "BB", msg = "三次密码不同")
    private EditText etPwFlag3;

    @NotNull(msg = "请填写邮箱")
    @RE(re = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", msg = "格式错误！")
    private EditText etRe;

    private void init() {
        etNotnull = (EditText) findViewById(R.id.et_notnull);
        etPw1 = (EditText) findViewById(R.id.et_pw1);
        etPw2 = (EditText) findViewById(R.id.et_pw2);
        etPwFlag1 = (EditText) findViewById(R.id.et_pw_flag1);
        etPwFlag2 = (EditText) findViewById(R.id.et_pw_flag2);
        etPwFlag3 = (EditText) findViewById(R.id.et_pw_flag3);


        etRe = (EditText) findViewById(R.id.et_re);

    }

    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Validate.reg(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                Validate.check(MainActivity.this,MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }


    @Override
    public void onValidateSuccess() {
        Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidateError(EditText editText, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Animation onValidateErrorAnno() {
        return ValidateAnimation.horizontalTranslate();
    }
}
