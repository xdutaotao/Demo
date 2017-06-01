package com.yankon.smart.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yankon.smart.R;

import java.util.List;
import java.util.Map;

/**
 * Created by sunup_002 on 2016/1/13.
 */
public class DialogArrayItemAdapter extends BaseAdapter {
    private Context context;
    private SparseArray listItems;
    private LayoutInflater listContainer;

    public DialogArrayItemAdapter(Context context, SparseArray listItems) {
        this.listItems = listItems;
        this.context = context;
        listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < listItems.size()){
            return listItems.get(position);
        }
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = new ListItemView();
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.dialog_list_view_item,
                    null);
            listItemView.value = (TextView) convertView
                    .findViewById(R.id.text_item);

            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.value.setText((String) listItems.get(position));
        return convertView;
    }

    public final class ListItemView{
        public TextView value;
    }
}
