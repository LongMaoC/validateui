package cxy.com.validateui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cxy.com.validateui.R;
import cxy.com.validateui.fragment.Fragment1;

/**
 * 支持fragment 内使用ValidateUI
 * Created by cxy on 17-9-12.
 */
public class FragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Fragment1 frg = new Fragment1();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main, frg).commit();
        getSupportFragmentManager().beginTransaction().show(frg);

    }
}
