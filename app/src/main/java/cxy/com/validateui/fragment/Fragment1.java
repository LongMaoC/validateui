package cxy.com.validateui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cxy.com.validate.SimpleValidateResult;
import cxy.com.validate.Validate;
import cxy.com.validate.annotation.Index;
import cxy.com.validate.annotation.NotNull;
import cxy.com.validateui.R;

/**
 * Created by cxy on 17-9-12.
 */

public class Fragment1 extends Fragment {
    @Index(1)
    @NotNull(msg = "请输入姓名")
    @Bind(R.id.et_f1_name)
    EditText etF1Name;
    @Index(2)
    @NotNull(msg = "请输入手机号")
    @Bind(R.id.et_f1_phone)
    EditText etF1Phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_1, null);
        ButterKnife.bind(this, rootView);
        etF1Name.setText("张三");
        Validate.reg(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Validate.unreg(this);
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_f1_submit)
    public void onViewClicked() {
        Validate.check(this, new SimpleValidateResult() {
            @Override
            public void onValidateSuccess() {
                Toast.makeText(getActivity(), "验证成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
