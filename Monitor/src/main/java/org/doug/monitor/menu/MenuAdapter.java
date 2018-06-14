package org.doug.monitor.menu;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.doug.monitor.R;

import java.util.List;

/**
 * Created by wesine on 2018/6/14.
 */

public class MenuAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {

    public MenuAdapter(int layoutResId, @Nullable List<MenuItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuItem item) {
        helper.setText(R.id.text, item.getTitle());
        helper.setImageResource(R.id.icon, item.getImageResource());
    }
}
