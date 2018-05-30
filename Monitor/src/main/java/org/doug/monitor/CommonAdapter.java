package org.doug.monitor;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by wesine on 2018/5/23.
 */

public class CommonAdapter extends BaseQuickAdapter<DeviceItem, BaseViewHolder> {
    public CommonAdapter(@Nullable List<DeviceItem> data) {
        super(R.layout.menu_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceItem item) {
        if (helper != null) {
            helper.setText(R.id.tv_title, item.getTitle());
            helper.setText(R.id.tv_desc, item.getDesc());
            helper.setChecked(R.id.sb_text_state, item.isOn());
        }
    }
}
