package com.yankon.smart.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yankon.smart.R;

import java.util.List;
import java.util.Map;

/**
 * Created by guzhenfu on 2015/8/20.
 */
public class DialogItemsAdapter extends BaseAdapter{

    private Context context;
    private List<Map<String,Object>> listItems;
    private LayoutInflater listContainer;

    public DialogItemsAdapter(Context context,List<Map<String,Object>> listItems){
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    public final class ListItemView{
        public TextView value;
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
        return null;
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
        listItemView.value.setText((String) listItems.get(position)
                .get("value"));
        return convertView;
    }
}
