package com.yankon.smart.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yankon.smart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzhenfu on 2015/8/18.
 */
public class CustomContextMenu extends PopupWindow {
    private Context context;
    private List<PopupItem> datas = new ArrayList<>();
    private ListView listView;
    private TextView titleText;
    private OnItemOnClickListener onItemOnClickListener;
    private int screenWidth, screenHeight;
    private boolean mIsDirty;
    private int popupGravity = Gravity.CENTER;
    private LayoutInflater mInflater;

    public CustomContextMenu(Context context){
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CustomContextMenu(Context context, int width, int height){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        setWidth(screenWidth * 2 / 3);
        setHeight(screenHeight * 5/12);

        setBackgroundDrawable(new BitmapDrawable());
        setContentView(LayoutInflater.from(context).inflate(R.layout.popup_window, null));
        setClippingEnabled(true);
        initUI();
    }

    private void initUI() {
        titleText = (TextView) getContentView().findViewById(R.id.popup_title_text);
        listView = (ListView) getContentView().findViewById(R.id.popup_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                setFocusable(false);
                if (onItemOnClickListener != null)
                    onItemOnClickListener.onItemClick(datas.get(i), i);
            }
        });
    }

    public void setHeaderTitle(String title){
        if (!TextUtils.isEmpty(title)){
            titleText.setText(title);
        }
    }

    public void show(View view){
        if (mIsDirty) {
            populateActions();
        }
        showAtLocation(view, popupGravity, 0, 0);
        setFocusable(true);
    }

    private void populateActions() {
        mIsDirty = false;
        listView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.popup_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.textView = (TextView) convertView.findViewById(R.id.name);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                PopupItem item = datas.get(position);
                viewHolder.textView.setText(item.mTitle);
                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return datas.get(position);
            }

            @Override
            public int getCount() {
                return datas.size();
            }
        }) ;
    }

    public static class ViewHolder{
        public TextView textView;
    }

    public void add(PopupItem action){
        if(action != null){
            datas.add(action);
            mIsDirty = true;
        }
    }

    public void clear(){
        if(datas.isEmpty()){
            datas.clear();
            mIsDirty = true;
        }
    }

    public PopupItem getAction(int position){
        if(position < 0 || position > datas.size())
            return null;
        return datas.get(position);
    }

    public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
        this.onItemOnClickListener = onItemOnClickListener;
    }

    public static interface OnItemOnClickListener{
        public void onItemClick(PopupItem item, int position);
    }
}
