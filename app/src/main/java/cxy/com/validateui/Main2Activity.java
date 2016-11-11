package cxy.com.validateui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cxy.com.validate.IValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.ValidateAnimation;
import cxy.com.validate.annotation.NotNull2;

/**
 * Created by CXY on 2016/11/10.
 */

public class Main2Activity extends Activity implements IValidateResult {
    @NotNull2(msg = "C1, 且不为空", index = 1)
    EditText etC;
    @NotNull2(msg = "A3, 且不为空", index = 3)
    EditText etA;
    @NotNull2(msg = "B2, 且不为空", index = 2)
    EditText etB;

    Button button;


    private void init() {
        etA = (EditText) findViewById(R.id.et_a);
        etB = (EditText) findViewById(R.id.et_b);
        etC = (EditText) findViewById(R.id.et_c);
        button = (Button) findViewById(R.id.button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        init();

        Validate.reg(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate.check(Main2Activity.this, Main2Activity.this);
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
    public void onValidateError( String msg,EditText editText) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Animation onValidateErrorAnno() {
        return ValidateAnimation.horizontalTranslate();
    }

}
