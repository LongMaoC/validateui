package cxy.com.validateui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cxy.com.validateui.activity.CommonActivity;
import cxy.com.validateui.activity.FragmentActivity;
import cxy.com.validateui.activity.ReturnActivity;
import cxy.com.validateui.activity.ShieldActivity;

/**
 * Created by cxy on 17-9-6.
 */

public class MainActivity extends android.app.ListActivity {
    String[] strs = new String[]{"常用注解Demo", "@Shield 屏蔽注解Demo", "两种返回接口","Fragment使用Validate"};
    Class[] activities = new Class[]{CommonActivity.class, ShieldActivity.class,ReturnActivity.class, FragmentActivity.class};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, strs);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, activities[position]));
    }
}
