package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cxy.com.validate.IValidateResult;
import cxy.com.validate.SimpleValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.MinLength;
import cxy.com.validate.annotation.Money;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validate.annotation.StartsWith;
import cxy.com.validateui.R;

/**
 * 注解 @StartsWith
 * ignoreCase 参数，是否忽略大小写
 * value 参数，以 value做开头校验
 */
public class StartsWithActivity extends AppCompatActivity {
    @Index(1)
    @NotNull(msg = "内容不能为空1")
    @MinLength(msg = "请输入至少4位长度的内容",length = 4)
    @StartsWith(msg = "格式不正确1，请以[COM+数组]方式命名！", ignoreCase = true, value = "COM")
    @Bind(R.id.et_1)
    EditText et_1;
    @Index(2)
    @NotNull(msg = "内容不能为空2")
    @MinLength(msg = "请输入至少4位长度的内容",length = 4)
    @StartsWith(msg = "格式不正确2，请以[COM+数组]方式命名！", ignoreCase = false, value = "")
    @Bind(R.id.et_2)
    EditText et_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_with);
        ButterKnife.bind(this);
        Validate.reg(this);

        et_1.setText("coM1");
        et_2.setText("coM2");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate.check(StartsWithActivity.this, new IValidateResult() {
                    @Override
                    public void onValidateSuccess() {
                        Toast.makeText(StartsWithActivity.this, "验证通过", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onValidateError(String msg, View view) {
                        Toast.makeText(StartsWithActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public Animation onValidateErrorAnno() {
                        return null;
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }
}
