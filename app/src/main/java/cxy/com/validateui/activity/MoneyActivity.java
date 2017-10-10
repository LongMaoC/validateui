package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cxy.com.validate.SimpleValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.Money;
import cxy.com.validateui.R;

/**
 * 注解 @Money keey 参数，会自动处理值，0<= keey <= 2
 *
 * 当 初始化好控件，再调用Validate.reg(this)方法时 ，程序内部会添加输入类型规则处理
 * 所以，xml中仅
 * <EditText
 *      android:id="@+id/et_money"
 *      android:layout_width="300dp"
 *      android:layout_height="wrap_content"/>
 * 就可以了，不用添加
 *      android:inputType="number|numberDecimal"
 *
 * 当Validate.reg(this) 在初始化之前调用，则不会添加规则
 */
public class MoneyActivity extends AppCompatActivity {
    @Index(10)
    @Money(msg = "格式不正确，请输入小数点后两位的金额", keey = 2)
    @Bind(R.id.et_money)
    EditText etMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        Validate.reg(this);
        ButterKnife.bind(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate.check(MoneyActivity.this, new SimpleValidateResult() {
                    @Override
                    public void onValidateSuccess() {
                        Toast.makeText(MoneyActivity.this, "验证通过", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//        etMoney.setText("1.2345");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Validate.unreg(this);
    }
}
